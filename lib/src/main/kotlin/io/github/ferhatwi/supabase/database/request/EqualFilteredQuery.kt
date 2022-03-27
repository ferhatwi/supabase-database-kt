package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Count
import io.github.ferhatwi.supabase.database.Event
import io.github.ferhatwi.supabase.database.Filter
import io.github.ferhatwi.supabase.database.snapshots.ListenSnapshot

open class EqualFilteredQuery internal constructor(
    schema: String,
    table: String,
    selections: List<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    private val filter: Filter.EqualTo<*>
) : FilterableQuery(schema, table, selections, range, count, mutableListOf(filter)), ListenableI {
    private fun listenable() = Listenable(schema, name, mutableListOf(), filter)

    override fun on(event: Event) = listenable().on(event)
    override suspend fun listen(onSuccess: (ListenSnapshot) -> Unit) =
        listenable().listen(onSuccess)
}
