package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase

class SupabaseDatabase {

    companion object {
        fun getInstance(): SupabaseDatabase {
            return SupabaseDatabase()
        }
    }

    fun table(name: String): TableReference {
        return TableReference(name)
    }

    fun schema(name: String): Schema {
        return Schema(name)
    }

    private val events: MutableList<Event> = mutableListOf()

    fun on(event: Event): LQuery {
        return LQuery(events.apply { add(event) })
    }

    suspend fun listen(onSuccess: (ListenSnapshot) -> Unit) =
        listen(topic = arrayOf("realtime:*"), events = events, onSuccess = onSuccess)

    suspend fun listen(vararg query: LQuery, onSuccess: (ListenSnapshot) -> Unit) = listen(
        topic = query.map { it.topic() }.toTypedArray(),
        events = events,
        onSuccess = onSuccess
    )


}

fun Supabase.database(): SupabaseDatabase {
    return SupabaseDatabase.getInstance()
}
