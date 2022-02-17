package io.github.ferhatwi.supabase.database

import com.google.gson.Gson
import io.github.ferhatwi.supabase.Supabase
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow


internal fun Modifier.asQueryString(): String {
    return when (this) {
        is Modifier.Limit -> "limit=$count"
        is Modifier.Order -> "order=$column.${
            when (orderBy) {
                OrderBy.Ascending -> "asc"
                OrderBy.Descending -> "desc"
            }
        }.${if (nullsFirst) "nullfirst" else "nullslast"}"
    }
}


internal fun getClient(): HttpClient {
    return HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }
}

@JvmName("asQueryStringSelect")
internal fun MutableList<String>.asQueryString(): String {
    return if (isEmpty()) {
        "select=*"
    } else {
        "select=${joinToString(separator = "&")}"
    }
}

@JvmName("asQueryStringFilter")
internal fun MutableList<Filter>.asQueryString(): String {
    return if (isEmpty()) {
        ""
    } else {
        joinToString("&") {
            it.toString()
        }
    }
}

@JvmName("asQueryStringModifier")
internal fun MutableList<Modifier>.asQueryString(): String {
    return if (isEmpty()) {
        ""
    } else {
        joinToString("&") {
            it.asQueryString()
        }
    }
}

internal fun appendQueryString(vararg queryStrings: String): String {
    return queryStrings.joinToString(separator = "&").replace("&&", "&").replace("&&", "&")
}


internal suspend fun listen(
    vararg topic: String,
    events: List<Event> = listOf(),
    onSuccess: (ListenSnapshot) -> Unit
) {
    val request =
        "wss://${Supabase.PROJECT_ID}.supabase.co/realtime/v1/websocket?vsn=1.0.0&apikey=${Supabase.API_KEY}"

    val client = HttpClient(CIO) {
        install(WebSockets)
    }


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
            incoming.receiveAsFlow().mapNotNull { it as? Frame.Text }
                .map { it.readText() }.collect {
                    val whole = Gson().fromJson<Map<String, Any?>>(it, Map::class.java)

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

                            if (events.isEmpty()) {
                                onSuccess(ListenSnapshot(RowSnapshot(data), event!!))
                            } else {
                                if (events.contains(event)) {
                                    onSuccess(ListenSnapshot(RowSnapshot(data), event!!))
                                }
                            }
                        }
                    }
                }
        }
    }
}



