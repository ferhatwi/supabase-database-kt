package io.github.ferhatwi.supabase.database.request

private fun <A : LimitedQueryC> A.range(from: Int, to: Int): A =
    apply { this.range = Pair(from, to) }

open class RangeableQueryR internal constructor(
    name: String,
    selections: MutableList<String>
) : CountableQueryR(name, selections, null) {
    fun range(from: Int, to: Int) = range<CountableQueryR>(from, to)
}

open class RangeableQuery internal constructor(
    name: String,
    selections: MutableList<String>
) : CountableQuery(name, selections, null) {
    fun range(from: Int, to: Int) = range<CountableQuery>(from, to)
}