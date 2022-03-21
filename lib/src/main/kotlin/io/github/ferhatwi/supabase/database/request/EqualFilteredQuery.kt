package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Event
import io.github.ferhatwi.supabase.database.Filter
import io.github.ferhatwi.supabase.database.snapshots.ListenSnapshot

open class EqualFilteredQuery internal constructor(
    name: String,
    filter: Filter.EqualTo<*>
) : FilterableQuery(name, mutableListOf(), null, null, mutableListOf(filter)), ListenableI {
    private val listenable = Listenable("public", name, mutableListOf(), filter)

    override fun on(event: Event) = listenable.on(event)
    override suspend fun listen(onSuccess: (ListenSnapshot) -> Unit) = listenable.listen(onSuccess)
}
