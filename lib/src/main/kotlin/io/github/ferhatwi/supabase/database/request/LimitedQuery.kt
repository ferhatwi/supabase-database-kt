package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.*
import io.github.ferhatwi.supabase.database.snapshots.RowSnapshot
import io.github.ferhatwi.supabase.database.snapshots.TableSnapshot
import io.ktor.client.utils.*
import io.ktor.http.*

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
    internal open suspend fun call(
        data: Any? = null,
        head: Boolean = false,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (String) -> Unit
    ) {
        val url = "${databaseURL()}/rpc/$name?${
            appendQueryString(
                selections.asQueryString(),
                filters.asQueryString(),
                orders.asQueryString(),
                limitToString(limit)
            )
        }"

        runCatching({
            runCatchingTransformation(suspend {
                getClient().request(
                    schema,
                    url,
                    if (head) HttpMethod.Head else HttpMethod.Post,
                    range,
                    count,
                    data ?: EmptyContent
                )
            }, { it }, "", onSuccess)
        }, onFailure)

    }

    internal open suspend fun get(
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
    }

    internal open suspend fun delete(
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
            runCatchingTransformation<List<Map<String, Any?>>, TableSnapshot>(suspend {
                getClient().request(schema, url, HttpMethod.Delete, range, count)
            }, { TableSnapshot(it.map { RowSnapshot(it) }) }, emptyList(), onSuccess)
        }, onFailure)
    }

    internal open suspend fun update(
        column: String,
        value: Any?,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = update(mapOf(column to value), onFailure, onSuccess)

    internal open suspend fun update(
        data: Map<String, Any?>,
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
            runCatchingTransformation<List<Map<String, Any?>>, TableSnapshot>(suspend {
                getClient().request(schema, url, HttpMethod.Patch, range, count, data) {
                    applicationJson()
                }
            }, { TableSnapshot(it.map { RowSnapshot(it) }) }, emptyList(), onSuccess)
        }, onFailure)
    }


    internal open suspend fun insert(
        column: String,
        value: Any?,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = insert(data = arrayOf(mapOf(column to value)), onFailure, onSuccess)

    internal open suspend fun insert(
        vararg data: Map<String, Any?>,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) {
        val url =
            "${databaseURL()}/$name?${
                appendQueryString(
                    selections.asQueryString(),
                    orders.asQueryString(),
                    limitToString(limit),
                )
            }"

        runCatching({
            runCatchingTransformation<List<Map<String, Any?>>, TableSnapshot>(suspend {
                getClient().request(schema, url, HttpMethod.Post, range, count, body = data) {
                    applicationJson()
                }
            }, { TableSnapshot(it.map { RowSnapshot(it) }) }, emptyList(), onSuccess)
        }, onFailure)
    }

    internal open suspend fun upsert(
        column: String,
        value: Any?,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = upsert(data = arrayOf(mapOf(column to value)), onFailure, onSuccess)

    internal open suspend fun upsert(
        vararg data: Map<String, Any?>,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) {
        val url =
            "${databaseURL()}/$name?${
                appendQueryString(
                    selections.asQueryString(),
                    orders.asQueryString(),
                    limitToString(limit),
                )
            }"

        runCatching({
            runCatchingTransformation<List<Map<String, Any?>>, TableSnapshot>(suspend {
                getClient().request(schema, url, HttpMethod.Post, range, count, data, true) {
                    applicationJson()
                }
            }, { TableSnapshot(it.map { RowSnapshot(it) }) }, emptyList(), onSuccess)
        }, onFailure)
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
    public override suspend fun call(
        data: Any?,
        head: Boolean,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (String) -> Unit
    ) = super.call(data, head, onFailure, onSuccess)
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
    public override suspend fun get(
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.get(onFailure, onSuccess)

    public override suspend fun delete(
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.delete(onFailure, onSuccess)

    public override suspend fun update(
        column: String,
        value: Any?,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.update(column, value, onFailure, onSuccess)

    public override suspend fun update(
        data: Map<String, Any?>,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.update(data, onFailure, onSuccess)
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
    public override suspend fun get(
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.get(onFailure, onSuccess)

    public override suspend fun delete(
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.delete(onFailure, onSuccess)

    public override suspend fun insert(
        column: String,
        value: Any?,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.insert(column, value, onFailure, onSuccess)

    public override suspend fun insert(
        vararg data: Map<String, Any?>,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.insert(data = data, onFailure, onSuccess)

    public override suspend fun upsert(
        column: String,
        value: Any?,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.upsert(column, value, onFailure, onSuccess)

    public override suspend fun upsert(
        vararg data: Map<String, Any?>,
        onFailure: (message: String?, code: String?, statusCode: HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.upsert(data = data, onFailure, onSuccess)

}


