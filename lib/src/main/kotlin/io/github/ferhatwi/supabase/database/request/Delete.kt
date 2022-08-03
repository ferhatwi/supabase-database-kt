package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.*
import io.github.ferhatwi.supabase.database.query.Count
import io.github.ferhatwi.supabase.database.query.Filter
import io.github.ferhatwi.supabase.database.query.Order
import io.ktor.client.call.*
import kotlinx.coroutines.flow.flow

class Delete private constructor() {

    companion object {
        fun getInstance() = Delete()
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

        val response = client.delete(
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
        filters: List<Filter>,
        orders: List<Order> = emptyList(),
        range: Pair<Int, Int>? = null
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
        filters: List<Filter>,
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
        filters: List<Filter>,
        orders: List<Order> = emptyList(),
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


    @OptIn(InternalSupabaseDatabaseAPI::class)
    fun request(
        schema: String = "public",
        table: String,
        count: Count? = null,
        filters: List<Filter>,
        orders: List<Order> = emptyList(),
        range: Pair<Int, Int>? = null
    ) = flow {
        request(
            schema = schema,
            table = table,
            selections = emptyList(),
            count = count,
            filters = filters,
            orders = orders,
            range = range,
            limit = null
        ).collect { emit(SupabaseDatabaseSuccess) }
    }

    @OptIn(InternalSupabaseDatabaseAPI::class)
    fun request(
        schema: String = "public",
        table: String,
        count: Count? = null,
        filters: List<Filter>,
        orders: List<Order> = emptyList(),
        limit: Int? = null
    ) = flow {
        request(
            schema = schema,
            table = table,
            selections = emptyList(),
            count = count,
            filters = filters,
            orders = orders,
            range = null,
            limit = limit
        ).collect { emit(SupabaseDatabaseSuccess) }
    }

    @OptIn(InternalSupabaseDatabaseAPI::class)
    fun request(
        schema: String = "public",
        table: String,
        count: Count? = null,
        filters: List<Filter>,
        orders: List<Order> = emptyList(),
    ) = flow {
        request(
            schema = schema,
            table = table,
            selections = emptyList(),
            count = count,
            filters = filters,
            orders = orders,
            range = null,
            limit = null
        ).collect { emit(SupabaseDatabaseSuccess) }
    }

}

val SupabaseDatabase.delete get() = Delete.getInstance()