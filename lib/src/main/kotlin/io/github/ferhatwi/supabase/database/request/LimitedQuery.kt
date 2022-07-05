package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.*
import io.github.ferhatwi.supabase.database.snapshots.RowSnapshot
import io.github.ferhatwi.supabase.database.snapshots.TableSnapshot
import io.ktor.client.call.*
import io.ktor.client.utils.*
import io.ktor.http.*
import kotlinx.coroutines.flow.flow

open class LimitedQueryC internal constructor(
    internal val schema: String,
    internal val name: String,
    internal var selections: List<String>,
    internal var range: Pair<Int, Int>?,
    internal var count: Count?,
    internal val filters: MutableList<Filter>,
    internal val orders: MutableList<Order>,
    internal var limit: Int?
) {

    internal open fun call(data: Map<String, Any?>? = null) = flow {
        val url = "${databaseURL()}/rpc/$name?${
            appendQueryString(
                selections.asQueryString(),
                filters.asQueryString(),
                orders.asQueryString(),
                limitToString(limit)
            )
        }"

        val request = getClient().request(
            schema = schema,
            url = url,
            method = if (selections.isEmpty()) HttpMethod.Head else HttpMethod.Post,
            range = range,
            count = count,
            body = data ?: EmptyContent
        ) {
            applicationJson()
        }

        emit(request)
    }


    internal open fun callTableSnapshot(data: Map<String, Any?>? = null) = flow {
        call(data).collect {
            TableSnapshot(it.body<List<Map<String, Any?>>>().map { RowSnapshot(it) }).also {
                emit(it)
            }
        }
    }

    internal open fun get() = flow {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                filters.asQueryString(),
                orders.asQueryString(),
                limitToString(limit)
            )
        }"

        val request = getClient().request(
            schema = schema,
            url = url,
            method = if (selections.isEmpty()) HttpMethod.Head else HttpMethod.Get,
            range = range,
            count = count
        )

        emit(
            TableSnapshot(
                if (selections.isEmpty())
                    emptyList()
                else
                    request.body<List<Map<String, Any?>>>().map {
                        RowSnapshot(it)
                    }
            )
        )
    }

    internal open fun delete() = flow {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                filters.asQueryString(),
                orders.asQueryString(),
                limitToString(limit)
            )
        }"

        val request = getClient().request(
            schema = schema,
            url = url,
            method = HttpMethod.Delete,
            range = range,
            count = count
        )

        emit(
            TableSnapshot(
                if (selections.isEmpty())
                    emptyList()
                else
                    request.body<List<Map<String, Any?>>>().map {
                        RowSnapshot(it)
                    }
            )
        )
    }

    internal open fun update(
        column: String,
        value: Any?
    ) = update(mapOf(column to value))

    internal open fun update(data: Map<String, Any?>) = flow {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                filters.asQueryString(),
                orders.asQueryString(),
                limitToString(limit)
            )
        }"

        val request = getClient().request(
            schema = schema,
            url = url,
            method = HttpMethod.Patch,
            range = range,
            count = count,
            body = data
        ) {
            applicationJson()
        }

        emit(
            TableSnapshot(
                if (selections.isEmpty())
                    emptyList()
                else
                    request.body<List<Map<String, Any?>>>().map {
                        RowSnapshot(it)
                    }
            )
        )
    }

    internal open fun insert(
        column: String,
        value: Any?
    ) = insert(data = arrayOf(mapOf(column to value)))

    internal open fun insert(vararg data: Map<String, Any?>) = flow {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                orders.asQueryString(),
                limitToString(limit),
            )
        }"

        val request = getClient().request(
            schema = schema,
            url = url,
            method = HttpMethod.Post,
            range = range,
            count = count,
            body = data.ifEmpty {
                mapOf<String, Any?>()
            }
        ) {
            applicationJson()
        }

        emit(
            TableSnapshot(
                if (selections.isEmpty())
                    emptyList()
                else
                    request.body<List<Map<String, Any?>>>().map {
                        RowSnapshot(it)
                    }
            )
        )
    }

    internal open fun upsert(
        column: String,
        value: Any?
    ) = insert(data = arrayOf(mapOf(column to value)))

    internal open fun upsert(vararg data: Map<String, Any?>) = flow {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                orders.asQueryString(),
                limitToString(limit),
            )
        }"

        val request = getClient().request(
            schema = schema,
            url = url,
            method = HttpMethod.Post,
            range = range,
            count = count,
            body = data.ifEmpty {
                mapOf<String, Any?>()
            },
            merge = true
        ) {
            applicationJson()
        }

        emit(
            TableSnapshot(
                if (selections.isEmpty())
                    emptyList()
                else
                    request.body<List<Map<String, Any?>>>().map {
                        RowSnapshot(it)
                    }
            )
        )
    }
}

open class LimitedQueryR internal constructor(
    schema: String,
    function: String,
    selections: List<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>,
    orders: MutableList<Order>,
    limit: Int?
) : LimitedQueryC(schema, function, selections, range, count, filters, orders, limit) {
    public override fun call(data: Map<String, Any?>?) = super.call(data)
    public override fun callTableSnapshot(data: Map<String, Any?>?) = super.callTableSnapshot(data)
}

open class LimitedQuery internal constructor(
    schema: String,
    table: String,
    selections: List<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>,
    orders: MutableList<Order>,
    limit: Int?
) : LimitedQueryC(schema, table, selections, range, count, filters, orders, limit) {
    public override fun get() = super.get()
    public override fun delete() = super.delete()
    public override fun update(column: String, value: Any?) = super.update(column, value)
    public override fun update(data: Map<String, Any?>) = super.update(data)
}

open class LimitedQueryX internal constructor(
    schema: String,
    table: String,
    selections: List<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    orders: MutableList<Order>,
    limit: Int?
) : LimitedQueryC(schema, table, selections, range, count, mutableListOf(), orders, limit) {
    public override fun get() = super.get()
    public override fun insert(column: String, value: Any?) = super.insert(column, value)
    public override fun insert(vararg data: Map<String, Any?>) = super.insert(*data)
    public override fun upsert(column: String, value: Any?) = super.upsert(column, value)
    public override fun upsert(vararg data: Map<String, Any?>) = super.upsert(*data)
}