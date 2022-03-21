package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Event
import io.github.ferhatwi.supabase.database.Filter
import io.github.ferhatwi.supabase.database.snapshots.ListenSnapshot


private fun <A : LimitedQueryC> A.select(vararg columns: String): A =
    apply { selections = columns.toList() }

open class SelectableQueryR internal constructor(
    name: String
) : RangeableQueryR(name, mutableListOf()) {
    fun select(vararg columns: String) = select<RangeableQueryR>(*columns)
}

open class SelectableQuery internal constructor(
    name: String
) : RangeableQuery(name, mutableListOf()), ListenableI {
    fun select(vararg columns: String) = select<RangeableQuery>(*columns)

    override fun <T : Any> equalTo(column: String, value: T) =
        EqualFilteredQuery(name, Filter.EqualTo(column, value))

    private val listenable = Listenable("public", name, mutableListOf(), null)

    override fun on(event: Event) = listenable.on(event)
    override suspend fun listen(onSuccess: (ListenSnapshot) -> Unit) = listenable.listen(onSuccess)
}