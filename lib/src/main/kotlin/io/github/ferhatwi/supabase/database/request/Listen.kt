package io.github.ferhatwi.supabase.database.request

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.github.ferhatwi.supabase.Supabase
import io.github.ferhatwi.supabase.database.InternalSupabaseDatabaseAPI
import io.github.ferhatwi.supabase.database.SupabaseDatabase
import io.github.ferhatwi.supabase.database.query.Event
import io.github.ferhatwi.supabase.database.query.Filter
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.gson.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.flow

data class ListenResponse<Record : Any?, OldRecord : Any?> @InternalSupabaseDatabaseAPI constructor(
    val event: Event,
    val record: Record,
    val old_record: OldRecord
)

class Listen private constructor() {

    companion object {
        fun getInstance() = Listen()
    }

    fun topic(
        schema: String?,
        table: String?,
        filter: Filter.EqualTo<*>?
    ) = if (schema == null) {
        "realtime:*"
    } else {
        if (table == null) {
            "realtime:${schema}"
        } else {
            if (filter == null) {
                "realtime:${schema}:${table}"
            } else {
                "realtime:${schema}:${table}:${filter}"
            }
        }
    }

    @InternalSupabaseDatabaseAPI
    suspend fun WebSocketSession.send(
        schema: String?,
        table: String?,
        filter: Filter.EqualTo<*>?
    ) {
        send(
            Gson().toJson(
                mapOf(
                    "topic" to topic(schema, table, filter),
                    "event" to "phx_join",
                    "payload" to mapOf("user_token" to Supabase.AUTHORIZATION),
                    "ref" to "None"
                )
            )
        )
    }

    @OptIn(InternalSupabaseDatabaseAPI::class)
    inline fun <reified Record : Any?, reified OldRecord : Any?> request(
        schema: String? = "public",
        table: String?,
        events: List<Event> = listOf(Event.INSERT, Event.UPDATE, Event.DELETE),
        filter: Filter.EqualTo<*>? = null
    ) = flow {
        val client = HttpClient(CIO) {
            install(WebSockets) {
                contentConverter = GsonWebsocketContentConverter()
            }
        }

        val request =
            "wss://${Supabase.PROJECT_ID}.supabase.co/realtime/v1/websocket?vsn=1.0.0&apikey=${Supabase.API_KEY}"

        var session = client.webSocketSession(request)

        session.send(schema, table, filter)

        while (true) {
            try {
                session.receiveDeserialized<JsonObject>().let {

                    val event = Gson().fromJson(it.get("event"), Event::class.java)

                    if (events.contains(event)) {
                        val payload = it.getAsJsonObject("payload")
                        emit(
                            ListenResponse(
                                event = event,
                                record = Gson().fromJson(
                                    payload.get("record"),
                                    Record::class.java
                                ),
                                old_record = Gson().fromJson(
                                    payload.get("old_record"),
                                    OldRecord::class.java
                                ),
                            )
                        )
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
                session = client.webSocketSession(request)
                session.send(schema, table, filter)
            }
        }
    }
}

val SupabaseDatabase.listen get() = Listen.getInstance()