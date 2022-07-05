package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Event
import io.github.ferhatwi.supabase.database.Filter


private fun <A : LimitedQueryC> A.select(vararg columns: String): A =
    apply {
        val mutableList = columns.toMutableList()
        mutableList.ifEmpty {
            mutableList.add("*")
        }
        selections = mutableList
    }

open class SelectableQueryR internal constructor(
    schema: String,
    function: String,
) : RangeableQueryR(schema, function, mutableListOf()) {
    fun select(vararg columns: String) = select<RangeableQueryR>(*columns)
}

open class SelectableQuery internal constructor(
    schema: String,
    table: String
) : RangeableQuery(schema, table, mutableListOf()), ListenableI {
    fun select(vararg columns: String) = select<RangeableQuery>(*columns)

    override fun <T : Any> equalTo(column: String, value: T) =
        EqualFilteredQuery(schema, name, selections, range, count, Filter.EqualTo(column, value))

    private fun listenable() = Listenable(schema, name, mutableListOf(), null)

    override fun on(event: Event) = listenable().on(event)
    override fun listen() = listenable().listen()
}