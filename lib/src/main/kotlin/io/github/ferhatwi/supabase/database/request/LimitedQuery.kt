package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.*
import io.github.ferhatwi.supabase.database.snapshots.RowSnapshot
import io.github.ferhatwi.supabase.database.snapshots.TableSnapshot
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
    internal open suspend fun call(data: Any? = null, head: Boolean = false) = flow {
        val url = "${databaseURL()}/rpc/$name?${
            appendQueryString(
                selections.asQueryString(),
                filters.asQueryString(),
                orders.asQueryString(),
                limitToString(limit)
            )
        }"

        runCatching({
            getClient().request(
                schema,
                url,
                if (head) HttpMethod.Head else HttpMethod.Post,
                range,
                count
            )
        }, onSuccess = {
            emit(it)
        }, "", head)
    }

    internal open suspend fun get() = flow {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                filters.asQueryString(),
                orders.asQueryString(),
                limitToString(limit)
            )
        }"

        runCatching<List<Map<String, Any?>>>({
            getClient().request(
                schema,
                url,
                if (selections.isEmpty()) HttpMethod.Head else HttpMethod.Get,
                range,
                count
            )
        }, onSuccess = {
            emit(TableSnapshot(it.map { RowSnapshot(it) }))
        }, emptyList(), selections.isEmpty())
    }

    /*internal open suspend fun get(
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                filters.asQueryString(),
                orders.asQueryString(),
                limitToString(limit)
            )
        }"

        runCatching({
            if (selections.isEmpty())
                runCatchingTransformation(
                    suspend {
                        getClient().request(
                            schema,
                            url,
                            if (selections.isEmpty()) HttpMethod.Head else HttpMethod.Get,
                            range,
                            count
                        )
                    }, { TableSnapshot(emptyList()) }, "", onSuccess
                )
            else
                runCatchingTransformation<List<Map<String, Any?>>, TableSnapshot>(
                    suspend {
                        getClient().request(
                            schema,
                            url,
                            if (selections.isEmpty()) HttpMethod.Head else HttpMethod.Get,
                            range,
                            count
                        )
                    }, { TableSnapshot(it.map { RowSnapshot(it) }) }, emptyList(), onSuccess
                )
        }, onFailure)
    }*/

    internal open suspend fun delete() = flow {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                filters.asQueryString(),
                orders.asQueryString(),
                limitToString(limit)
            )
        }"

        runCatching<List<Map<String, Any?>>>({
            getClient().request(schema, url, HttpMethod.Delete, range, count)
        }, onSuccess = {
            emit(TableSnapshot(it.map { RowSnapshot(it) }))
        }, emptyList())
    }

    internal open suspend fun update(
        column: String,
        value: Any?
    ) = update(mapOf(column to value))

    internal open suspend fun update(data: Map<String, Any?>) = flow {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                filters.asQueryString(),
                orders.asQueryString(),
                limitToString(limit)
            )
        }"

        runCatching<List<Map<String, Any?>>>({
            getClient().request(schema, url, HttpMethod.Patch, range, count, data) {
                applicationJson()
            }
        }, onSuccess = {
            emit(TableSnapshot(it.map { RowSnapshot(it) }))
        }, emptyList())
    }

    internal open suspend fun insert(
        column: String,
        value: Any?
    ) = insert(data = arrayOf(mapOf(column to value)))

    internal open suspend fun insert(vararg data: Map<String, Any?>) = flow {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                orders.asQueryString(),
                limitToString(limit),
            )
        }"

        runCatching<List<Map<String, Any?>>>({
            getClient().request(schema, url, HttpMethod.Post, range, count, data.ifEmpty {
                mapOf<String, Any?>()
            }) {
                applicationJson()
            }
        }, onSuccess = {
            emit(TableSnapshot(it.map { RowSnapshot(it) }))
        }, emptyList())
    }

    internal open suspend fun upsert(
        column: String,
        value: Any?
    ) = insert(data = arrayOf(mapOf(column to value)))

    internal open suspend fun upsert(vararg data: Map<String, Any?>) = flow {
        val url = "${databaseURL()}/$name?${
            appendQueryString(
                selections.asQueryString(),
                orders.asQueryString(),
                limitToString(limit),
            )
        }"

        runCatching<List<Map<String, Any?>>>({
            getClient().request(schema, url, HttpMethod.Post, range, count, data.ifEmpty {
                mapOf<String, Any?>()
            }, true) {
                applicationJson()
            }
        }, onSuccess = {
            emit(TableSnapshot(it.map { RowSnapshot(it) }))
        }, emptyList())
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
    public override suspend fun call(data: Any?, head: Boolean) = super.call(data, head)
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
    public override suspend fun get() = super.get()
    public override suspend fun delete() = super.delete()
    public override suspend fun update(column: String, value: Any?) = super.update(column, value)
    public override suspend fun update(data: Map<String, Any?>) = super.update(data)
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
    public override suspend fun get() = super.get()
    public override suspend fun delete() = super.delete()
    public override suspend fun insert(column: String, value: Any?) = super.insert(column, value)
    public override suspend fun insert(vararg data: Map<String, Any?>) = super.insert(*data)
    public override suspend fun upsert(column: String, value: Any?) = super.upsert(column, value)
    public override suspend fun upsert(vararg data: Map<String, Any?>) = super.upsert(*data)
}