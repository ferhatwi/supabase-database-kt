package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Count
import io.github.ferhatwi.supabase.database.Filter
import io.github.ferhatwi.supabase.database.Order
import io.github.ferhatwi.supabase.database.OrderBy

private fun <A : LimitedQueryC> A.order(column: String, orderBy: OrderBy, nullsFirst: Boolean): A =
    apply { orders.add(Order(column, orderBy, nullsFirst)) }

private fun <A : LimitedQueryC> A.order(order: Order): A =
    apply { orders.add(order) }

open class OrderableQueryR internal constructor(
    schema: String,
    function: String,
    selections: List<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>,
    orders: MutableList<Order>,
) : LimitableQueryR(schema, function, selections, range, count, filters, orders) {
    fun order(column: String, orderBy: OrderBy = OrderBy.Ascending, nullsFirst: Boolean = false) =
        order<OrderableQueryR>(column, orderBy, nullsFirst)

    fun order(order: Order) = order<OrderableQueryR>(order)
}

open class OrderableQuery internal constructor(
    schema: String,
    table: String,
    selections: List<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>,
    orders: MutableList<Order>,
) : LimitableQuery(schema, table, selections, range, count, filters, orders) {
    fun order(column: String, orderBy: OrderBy = OrderBy.Ascending, nullsFirst: Boolean = false) =
        order<OrderableQuery>(column, orderBy, nullsFirst)

    fun order(order: Order) = order<OrderableQuery>(order)
}

open class OrderableQueryX internal constructor(
    schema: String,
    table: String,
    selections: List<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    orders: MutableList<Order>,
) : LimitableQueryX(schema, table, selections, range, count, orders) {
    fun order(column: String, orderBy: OrderBy = OrderBy.Ascending, nullsFirst: Boolean = false) =
        order<OrderableQueryX>(column, orderBy, nullsFirst)

    fun order(order: Order) = order<OrderableQueryX>(order)
}