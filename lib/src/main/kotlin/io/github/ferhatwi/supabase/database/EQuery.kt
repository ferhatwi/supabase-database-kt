package io.github.ferhatwi.supabase.database

open class EQuery internal constructor(private val tableReference: TableReference) :
    Query(table = tableReference.table, filters = tableReference.filters) {

    private val events: MutableList<Event> = mutableListOf()

    fun on(event: Event): LQuery {
        return LQuery(
            tableReference.schema,
            tableReference,
            tableReference.filters.first() as Filter.EqualTo<*>?,
            events.apply { add(event) })
    }

    suspend fun listen(onSuccess: (ListenSnapshot) -> Unit) =
        listen(
            topic = arrayOf("realtime:${tableReference.schema.name}:${tableReference.table}:${tableReference.filters.asQueryString()}"),
            events = events,
            onSuccess = onSuccess
        )


}