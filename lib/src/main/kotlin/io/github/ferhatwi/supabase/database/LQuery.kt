package io.github.ferhatwi.supabase.database

class LQuery internal constructor(
    private val schema: Schema?,
    private val table: TableReference?,
    private val filter: Filter.EqualTo<*>?,
    private val events: MutableList<Event>
) {

    constructor(events: MutableList<Event>) : this(null, null, null, events)

    constructor(schema: Schema, events: MutableList<Event>) : this(schema, null, null, events)

    constructor(table: TableReference, events: MutableList<Event>) : this(
        table.schema,
        table,
        null,
        events
    )

    fun on(event: Event): LQuery {
        return this.apply { events.add(event) }
    }

    internal fun topic() = if (schema == null) {
        "realtime:*"
    } else {
        if (table == null) {
            "realtime:${schema.name}"
        } else {
            if (filter == null) {
                "realtime:${schema.name}:${table.table}"
            } else {
                "realtime:${schema.name}:${table.table}:${filter.toString()}"
            }
        }

    }

    //TODO on delete and update there are old_records
    suspend fun listen(onSuccess: (ListenSnapshot) -> Unit) =
        listen(topic = arrayOf(topic()), events = events, onSuccess = onSuccess)

}