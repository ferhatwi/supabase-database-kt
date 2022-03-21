package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Count

private fun <A : LimitedQueryC> A.countType(count: Count?): A = apply { this.count = count }

open class CountableQueryR internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?
) : FilterableQueryR(name, selections, range, null, mutableListOf()) {
    fun countType(count: Count?) = countType<FilterableQueryR>(count)
}

open class CountableQuery internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?
) : FilterableQueryX(name, selections, range, null) {
    fun countType(count: Count?) = countType<FilterableQueryX>(count)
}