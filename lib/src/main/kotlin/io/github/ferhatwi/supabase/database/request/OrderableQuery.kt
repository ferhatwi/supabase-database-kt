package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Count
import io.github.ferhatwi.supabase.database.Filter
import io.github.ferhatwi.supabase.database.Order
import io.github.ferhatwi.supabase.database.OrderBy

private fun <A : LimitedQueryC> A.order(column: String, orderBy: OrderBy, nullsFirst: Boolean): A =
    apply { orders.add(Order(column, orderBy, nullsFirst)) }

open class OrderableQueryR internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>,
    orders: MutableList<Order>,
) : LimitableQueryR(name, selections, range, count, filters, orders) {
    fun order(column: String, orderBy: OrderBy = OrderBy.Ascending, nullsFirst: Boolean = false) =
        order<OrderableQueryR>(column, orderBy, nullsFirst)
}

open class OrderableQuery internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>,
    orders: MutableList<Order>,
) : LimitableQuery(name, selections, range, count, filters, orders) {
    fun order(column: String, orderBy: OrderBy = OrderBy.Ascending, nullsFirst: Boolean = false) =
        order<OrderableQuery>(column, orderBy, nullsFirst)
}

open class OrderableQueryX internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    orders: MutableList<Order>,
) : LimitableQueryX(name, selections, range, count, orders) {
    fun order(column: String, orderBy: OrderBy = OrderBy.Ascending, nullsFirst: Boolean = false) =
        order<OrderableQueryX>(column, orderBy, nullsFirst)
}