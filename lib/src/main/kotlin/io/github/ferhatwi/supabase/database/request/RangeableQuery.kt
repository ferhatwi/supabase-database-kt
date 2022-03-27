package io.github.ferhatwi.supabase.database.request

private fun <A : LimitedQueryC> A.range(from: Int, to: Int): A =
    apply { this.range = Pair(from, to) }

open class RangeableQueryR internal constructor(
    schema: String,
    function: String,
    selections: MutableList<String>
) : CountableQueryR(schema, function, selections, null) {
    fun range(from: Int, to: Int) = range<CountableQueryR>(from, to)
}

open class RangeableQuery internal constructor(
    schema: String,
    table: String,
    selections: MutableList<String>
) : CountableQuery(schema, table, selections, null) {

    fun range(from: Int, to: Int) = range<CountableQuery>(from, to)
}