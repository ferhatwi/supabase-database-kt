package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.*
import io.github.ferhatwi.supabase.database.query.Count
import io.github.ferhatwi.supabase.database.query.Filter
import io.github.ferhatwi.supabase.database.query.Order
import io.ktor.client.call.*
import kotlinx.coroutines.flow.flow


class Get private constructor() {

    companion object {
        fun getInstance() = Get()
    }


    @InternalSupabaseDatabaseAPI
    fun request(
        schema: String,
        table: String,
        selections: List<String>,
        count: Count?,
        filters: List<Filter>,
        orders: List<Order>,
        range: Pair<Int, Int>?,
        limit: Int?
    ) = flow {

        val url = "$databaseURL/$table?${
            appendQueries(
                selections.toQuery(),
                filters.toQuery(),
                orders.toQuery(),
                limit?.let { "limit=$it" } ?: ""
            )
        }"

        val response = client.get(
            schema = schema,
            url = url,
            count = count,
            range = range,
        )

        emit(response)

    }

    @OptIn(InternalSupabaseDatabaseAPI::class)
    inline fun <reified T> request(
        schema: String = "public",
        table: String,
        selections: List<String> = listOf("*"),
        count: Count? = null,
        filters: List<Filter> = emptyList(),
        orders: List<Order> = emptyList(),
        range: Pair<Int, Int>? = null,
    ) = flow<T> {
        request(
            schema = schema,
            table = table,
            selections = selections,
            count = count,
            filters = filters,
            orders = orders,
            range = range,
            limit = null
        ).collect { emit(it.body()) }
    }

    @OptIn(InternalSupabaseDatabaseAPI::class)
    inline fun <reified T> request(
        schema: String = "public",
        table: String,
        selections: List<String> = listOf("*"),
        count: Count? = null,
        filters: List<Filter> = emptyList(),
        orders: List<Order> = emptyList(),
        limit: Int? = null
    ) = flow<T> {
        request(
            schema = schema,
            table = table,
            selections = selections,
            count = count,
            filters = filters,
            orders = orders,
            range = null,
            limit = limit
        ).collect { emit(it.body()) }
    }

    @OptIn(InternalSupabaseDatabaseAPI::class)
    inline fun <reified T> request(
        schema: String = "public",
        table: String,
        selections: List<String> = listOf("*"),
        count: Count? = null,
        filters: List<Filter> = emptyList(),
        orders: List<Order> = emptyList()
    ) = flow<T> {
        request(
            schema = schema,
            table = table,
            selections = selections,
            count = count,
            filters = filters,
            orders = orders,
            range = null,
            limit = null
        ).collect { emit(it.body()) }
    }

}

val SupabaseDatabase.get get() = Get.getInstance()