package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.*
import io.github.ferhatwi.supabase.database.query.Count
import io.github.ferhatwi.supabase.database.query.Filter
import io.github.ferhatwi.supabase.database.query.Order
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.flow.flow

class Update private constructor() {

    companion object {
        fun getInstance() = Update()
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
        limit: Int?,
        data: Any
    ) = flow {
        val url = "$databaseURL/$table?${
            appendQueries(
                selections.toQuery(),
                filters.toQuery(),
                orders.toQuery(),
                limit?.let { "limit=$it" } ?: ""
            )
        }"

        val response = client.patch(
            schema = schema,
            url = url,
            count = count,
            range = range,
            body = data
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
        range: Pair<Int, Int>? = null,
        data: Any
    ) = flow<T> {
        request(
            schema = schema,
            table = table,
            selections = selections,
            count = count,
            filters = filters,
            orders = orders,
            range = range,
            limit = null,
            data = data
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
        limit: Int? = null,
        data: Any
    ) = flow<T> {
        request(
            schema = schema,
            table = table,
            selections = selections,
            count = count,
            filters = filters,
            orders = orders,
            range = null,
            limit = limit,
            data = data
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
        data: Any
    ) = flow<T> {
        request(
            schema = schema,
            table = table,
            selections = selections,
            count = count,
            filters = filters,
            orders = orders,
            range = null,
            limit = null,
            data = data
        ).collect { emit(it.body()) }
    }


    @OptIn(InternalSupabaseDatabaseAPI::class)
    fun request(
        schema: String = "public",
        table: String,
        count: Count? = null,
        filters: List<Filter>,
        orders: List<Order> = emptyList(),
        range: Pair<Int, Int>? = null,
        data: Any
    ) = flow {
        request(
            schema = schema,
            table = table,
            selections = emptyList(),
            count = count,
            filters = filters,
            orders = orders,
            range = range,
            limit = null,
            data = data
        ).collect { emit(SupabaseDatabaseSuccess) }
    }

    @OptIn(InternalSupabaseDatabaseAPI::class)
    fun request(
        schema: String = "public",
        table: String,
        count: Count? = null,
        filters: List<Filter>,
        orders: List<Order> = emptyList(),
        limit: Int? = null,
        data: Any
    ) = flow {
        request(
            schema = schema,
            table = table,
            selections = emptyList(),
            count = count,
            filters = filters,
            orders = orders,
            range = null,
            limit = limit,
            data = data
        ).collect { emit(SupabaseDatabaseSuccess) }
    }

    @OptIn(InternalSupabaseDatabaseAPI::class)
    fun request(
        schema: String = "public",
        table: String,
        count: Count? = null,
        filters: List<Filter>,
        orders: List<Order> = emptyList(),
        data: Any
    ) = flow {
        request(
            schema = schema,
            table = table,
            selections = emptyList(),
            count = count,
            filters = filters,
            orders = orders,
            range = null,
            limit = null,
            data = data
        ).collect { emit(SupabaseDatabaseSuccess) }
    }

}


val SupabaseDatabase.update get() = Update.getInstance()