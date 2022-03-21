package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Count
import io.github.ferhatwi.supabase.database.Filter
import io.github.ferhatwi.supabase.database.Order

internal fun <A : LimitedQueryC> A.limit(value: Int): A = apply { limit = value }

open class LimitableQueryR internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>,
    orders: MutableList<Order>,
) : LimitedQueryR(name, selections, range, count, filters, orders, null) {
    fun limit(value: Int) = limit<LimitedQueryR>(value)
}

open class LimitableQuery internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>,
    orders: MutableList<Order>,
) : LimitedQuery(name, selections, range, count, filters, orders, null) {
    fun limit(value: Int) = limit<LimitedQuery>(value)
}

open class LimitableQueryX internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    orders: MutableList<Order>,
) : LimitedQueryX(name, selections, range, count, orders, null) {
    fun limit(value: Int) = limit<LimitedQueryX>(value)
}