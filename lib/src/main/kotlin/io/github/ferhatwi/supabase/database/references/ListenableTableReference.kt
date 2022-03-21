package io.github.ferhatwi.supabase.database.references

import io.github.ferhatwi.supabase.database.Event
import io.github.ferhatwi.supabase.database.Filter
import io.github.ferhatwi.supabase.database.request.Listenable

class ListenableTableReference internal constructor(
    schema: String?,
    name: String,
    events: MutableList<Event>
) : Listenable(schema, name, events, null) {
    fun <T : Any> equalTo(column: String, value: T): Listenable =
        apply { filter = Filter.EqualTo(column, value) }
}