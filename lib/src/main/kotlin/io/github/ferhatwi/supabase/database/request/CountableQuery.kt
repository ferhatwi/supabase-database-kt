package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Count

private fun <A : LimitedQueryC> A.countType(count: Count?): A = apply { this.count = count }

open class CountableQueryR internal constructor(
    schema: String,
    function: String,
    selections: List<String>,
    range: Pair<Int, Int>?
) : FilterableQueryR(schema, function, selections, range, null, mutableListOf()) {
    fun countType(count: Count?) = countType<FilterableQueryR>(count)
}

open class CountableQuery internal constructor(
    schema: String,
    table: String,
    selections: List<String>,
    range: Pair<Int, Int>?
) : FilterableQueryX(schema, table, selections, range, null) {
    fun countType(count: Count?) = countType<FilterableQueryX>(count)
}