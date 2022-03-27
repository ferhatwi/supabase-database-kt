package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Event
import io.github.ferhatwi.supabase.database.Filter
import io.github.ferhatwi.supabase.database.snapshots.ListenSnapshot

open class EqualFilteredQuery internal constructor(
    schema : String,
    table: String,
    filter: Filter.EqualTo<*>
) : FilterableQuery(schema, table, mutableListOf(), null, null, mutableListOf(filter)), ListenableI {
    private val listenable = Listenable(schema, name, mutableListOf(), filter)

    override fun on(event: Event) = listenable.on(event)
    override suspend fun listen(onSuccess: (ListenSnapshot) -> Unit) = listenable.listen(onSuccess)
}
