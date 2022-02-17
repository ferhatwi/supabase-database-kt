package io.github.ferhatwi.supabase.database

class TableReference internal constructor(
    name: String,
    internal val schema: Schema = Schema("public")
) : Query(name) {


    suspend fun insert(vararg data: Map<String, Any?>): TableSnapshot = XQuery(this).insert(*data)

    suspend fun upsert(vararg data: Map<String, Any?>): TableSnapshot = XQuery(this).upsert(*data)

    private val events: MutableList<Event> = mutableListOf()

    override fun <T : Any> equalTo(column: String, value: T): EQuery {
        return EQuery(this.apply {
            filters.add(Filter.EqualTo(column, value))
        })
    }

    fun on(event: Event): LQuery {
        return LQuery(this, events.apply { add(event) })
    }

    suspend fun listen(onSuccess: (ListenSnapshot) -> Unit) =
        listen(
            topic = arrayOf("realtime:${schema.name}:$table"),
            events = events,
            onSuccess = onSuccess
        )
}