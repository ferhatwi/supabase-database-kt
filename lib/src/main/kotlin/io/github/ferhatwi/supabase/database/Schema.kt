package io.github.ferhatwi.supabase.database

class Schema internal constructor(internal val name: String) {

    private val events: MutableList<Event> = mutableListOf()

    fun table(name: String): TableReference {
        return TableReference(name, this)
    }

    fun on(event: Event): LQuery {
        return LQuery(this, events.apply { add(event) })
    }

    suspend fun listen(onSuccess: (ListenSnapshot) -> Unit) =
        listen(topic = kotlin.arrayOf("realtime:${name}"), events = events, onSuccess = onSuccess)
}