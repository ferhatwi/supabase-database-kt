package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.*
import io.github.ferhatwi.supabase.database.query.Count
import io.github.ferhatwi.supabase.database.query.Filter
import io.github.ferhatwi.supabase.database.query.Order
import io.ktor.client.call.*
import io.ktor.client.utils.*
import kotlinx.coroutines.flow.flow

class RPC private constructor() {

    companion object {
        fun getInstance() = RPC()
    }

    @InternalSupabaseDatabaseAPI
    fun request(
        schema: String,
        function: String,
        selections: List<String>,
        count: Count?,
        filters: List<Filter>,
        orders: List<Order>,
        range: Pair<Int, Int>?,
        limit: Int?,
        data: Any
    ) = flow {
        val url = "$databaseURL/rpc/$function?${
            appendQueries(
                selections.toQuery(),
                filters.toQuery(),
                orders.toQuery(),
                limit?.let { "limit=$it" } ?: ""
            )
        }"

        val response = client.post(
            schema = schema,
            url = url,
            count = count,
            range = range,
            body = data,
            mergeDuplicated = false
        )

        emit(response)
    }

    @OptIn(InternalSupabaseDatabaseAPI::class)
    inline fun <reified T : Any> request(
        schema: String = "public",
        function: String,
        selections: List<String> = listOf("*"),
        count: Count? = null,
        filters: List<Filter> = emptyList(),
        orders: List<Order> = emptyList(),
        limit: Int? = null,
        data: Any = EmptyContent
    ) = flow<T> {
        request(
            schema = schema,
            function = function,
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
    inline fun <reified T : Any> request(
        schema: String = "public",
        function: String,
        selections: List<String> = listOf("*"),
        count: Count? = null,
        filters: List<Filter> = emptyList(),
        orders: List<Order> = emptyList(),
        data: Any = EmptyContent
    ) = flow<T> {
        request(
            schema = schema,
            function = function,
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
        function: String,
        count: Count? = null,
        filters: List<Filter> = emptyList(),
        orders: List<Order> = emptyList(),
        range: Pair<Int, Int>? = null,
        data: Any = EmptyContent
    ) = flow {
        request(
            schema = schema,
            function = function,
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
        function: String,
        count: Count? = null,
        filters: List<Filter> = emptyList(),
        orders: List<Order> = emptyList(),
        limit: Int? = null,
        data: Any = EmptyContent
    ) = flow {
        request(
            schema = schema,
            function = function,
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
        function: String,
        count: Count? = null,
        filters: List<Filter> = emptyList(),
        orders: List<Order> = emptyList(),
        data: Any = EmptyContent
    ) = flow {
        request(
            schema = schema,
            function = function,
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

val SupabaseDatabase.rpc get() = RPC.getInstance()