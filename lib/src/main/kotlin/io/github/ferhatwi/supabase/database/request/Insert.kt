package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.*
import io.github.ferhatwi.supabase.database.query.Count
import io.github.ferhatwi.supabase.database.query.Order
import io.ktor.client.call.*
import kotlinx.coroutines.flow.flow

class Insert private constructor() {

    companion object {
        fun getInstance() = Insert()
    }

    @InternalSupabaseDatabaseAPI
    fun request(
        schema: String,
        table: String,
        selections: List<String>,
        count: Count?,
        orders: List<Order>,
        range: Pair<Int, Int>?,
        limit: Int?,
        data: Any,
    ) = flow {
        val url = "$databaseURL/$table?${
            appendQueries(
                selections.toQuery(),
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
    inline fun <reified T> request(
        schema: String = "public",
        table: String,
        selections: List<String> = listOf("*"),
        count: Count? = null,
        orders: List<Order> = emptyList(),
        limit: Int? = null,
        data: Any
    ) = flow<T> {
        request(
            schema = schema,
            table = table,
            selections = selections,
            count = count,
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
        orders: List<Order> = emptyList(),
        range: Pair<Int, Int>? = null,
        data: Any
    ) = flow<T> {
        request(
            schema = schema,
            table = table,
            selections = selections,
            count = count,
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
        orders: List<Order> = emptyList(),
        data: Any
    ) = flow<T> {
        request(
            schema = schema,
            table = table,
            selections = selections,
            count = count,
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
        orders: List<Order> = emptyList(),
        limit: Int? = null,
        data: Any
    ) = flow {
        request(
            schema = schema,
            table = table,
            selections = emptyList(),
            count = count,
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
        orders: List<Order> = emptyList(),
        range: Pair<Int, Int>? = null,
        data: Any
    ) = flow {
        request(
            schema = schema,
            table = table,
            selections = emptyList(),
            count = count,
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
        orders: List<Order> = emptyList(),
        data: Any
    ) = flow {
        request(
            schema = schema,
            table = table,
            selections = emptyList(),
            count = count,
            orders = orders,
            range = null,
            limit = null,
            data = data
        ).collect { emit(SupabaseDatabaseSuccess) }
    }

}

val SupabaseDatabase.insert get() = Insert.getInstance()