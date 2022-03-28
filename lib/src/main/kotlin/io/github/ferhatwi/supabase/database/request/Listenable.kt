package io.github.ferhatwi.supabase.database.request

import com.google.gson.Gson
import io.github.ferhatwi.supabase.Supabase
import io.github.ferhatwi.supabase.database.Event
import io.github.ferhatwi.supabase.database.Filter
import io.github.ferhatwi.supabase.database.Listener
import io.github.ferhatwi.supabase.database.snapshots.ListenSnapshot
import io.github.ferhatwi.supabase.database.snapshots.RowSnapshot
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow


internal suspend fun listen(
    vararg topic: String,
    events: List<Event> = listOf()
) = flow {
    val request =
        "wss://${Supabase.PROJECT_ID}.supabase.co/realtime/v1/websocket?vsn=1.0.0&apikey=${Supabase.AUTHORIZATION}"

    val client = HttpClient(CIO) {
        install(WebSockets)
    }

    suspend fun connect() {
        client.webSocket(request) {
            topic.forEach {
                send(
                    Gson().toJson(
                        mapOf(
                            "topic" to it,
                            "event" to "phx_join",
                            "payload" to "{}",
                            "ref" to "None"
                        )
                    )
                )
            }

            while (true) {
                incoming.tryReceive().onSuccess {
                    val frameText = (it as? Frame.Text)
                    if (frameText != null) {
                        val whole = Gson().fromJson<Map<String, Any?>>(
                            frameText.readText(),
                            Map::class.java
                        )

                        if (whole.containsKey("payload")) {
                            val payload = whole["payload"] as Map<String, Any?>

                            val data = if (payload.containsKey("record")) {
                                payload["record"] as Map<String, Any?>
                            } else {
                                if (payload.containsKey("old_record")) payload["old_record"] as Map<String, Any?> else mapOf()
                            }
                            if (data.isNotEmpty()) {

                                val event = when (whole["event"]) {
                                    "INSERT" -> Event.Insert
                                    "UPDATE" -> Event.Update
                                    "DELETE" -> Event.Delete
                                    else -> null
                                }

                                if (events.isEmpty() || events.contains(event)) {
                                    emit(ListenSnapshot(RowSnapshot(data), event!!))
                                }
                            }
                        }

                    }

                }.onClosed {
                    connect()
                }
            }
        }
    }
    connect()
}

internal fun Listenable.topic() = if (schema == null) {
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

internal interface ListenableI {
    fun on(event: Event): Listenable
    suspend fun listen(action: suspend (value: ListenSnapshot) -> Unit): Listener
}

open class Listenable internal constructor(
    internal val schema: String?,
    internal val table: String?,
    internal val events: MutableList<Event>,
    internal var filter: Filter.EqualTo<*>?
) : ListenableI {


    override fun on(event: Event) = apply { events.add(event) }
    override suspend fun listen(action: suspend (value: ListenSnapshot) -> Unit) =
        Listener(CoroutineScope(Dispatchers.IO).async {
            listen(topic = arrayOf(topic()), events = events).cancellable().collect(action)
        }.apply { await() })
}