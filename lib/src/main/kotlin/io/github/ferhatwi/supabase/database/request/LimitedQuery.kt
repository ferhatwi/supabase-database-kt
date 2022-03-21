package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.*
import io.github.ferhatwi.supabase.database.snapshots.RowSnapshot
import io.github.ferhatwi.supabase.database.snapshots.TableSnapshot
import io.ktor.http.*

open class LimitedQueryC internal constructor(
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
        onFailure: (HttpStatusCode) -> Unit,
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
            val result : String = if (data == null) {
                getClient().request(url, HttpMethod.Post) {
                    preferences(presentation = true, merge = false, count = count)
                }
            } else {
                getClient().request(url, HttpMethod.Post, data) {
                    applicationJson()
                    preferences(presentation = true, merge = false, count = count)
                }
            }
            onSuccess(result)
        }, onFailure)

    }


    internal open suspend fun get(
        onFailure: (HttpStatusCode) -> Unit,
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
            val result: List<Map<String, Any?>> = getClient().request(url, HttpMethod.Get) {
                preferences(presentation = false, merge = false, count = count)
            }
            onSuccess(TableSnapshot(result.map { RowSnapshot(it) }))
        }, onFailure)
    }

    internal open suspend fun delete(
        onFailure: (HttpStatusCode) -> Unit,
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
            val result: List<Map<String, Any?>> = getClient().request(url, HttpMethod.Delete) {
                preferences(presentation = true, merge = false, count = count)
            }
            onSuccess(TableSnapshot(result.map { RowSnapshot(it) }))
        }, onFailure)
    }

    internal open suspend fun update(
        column: String,
        value: Any?,
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = update(mapOf(column to value), onFailure, onSuccess)

    internal open suspend fun update(
        data: Map<String, Any?>,
        onFailure: (HttpStatusCode) -> Unit,
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
            val result: List<Map<String, Any?>> = getClient().request(url, HttpMethod.Patch, data) {
                applicationJson()
                preferences(presentation = true, merge = false, count = count)
            }
            onSuccess(TableSnapshot(result.map { RowSnapshot(it) }))
        }, onFailure)
    }


    internal open suspend fun insert(
        column: String,
        value: Any?,
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = insert(data = arrayOf(mapOf(column to value)), onFailure, onSuccess)

    internal open suspend fun insert(
        vararg data: Map<String, Any?>,
        onFailure: (HttpStatusCode) -> Unit,
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
            val result: List<Map<String, Any?>> = getClient().request(url, HttpMethod.Post, data) {
                applicationJson()
                preferences(presentation = true, merge = false, count = count)
            }
            onSuccess(TableSnapshot(result.map { RowSnapshot(it) }))
        }, onFailure)
    }

    internal open suspend fun upsert(
        column: String,
        value: Any?,
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = upsert(data = arrayOf(mapOf(column to value)), onFailure, onSuccess)

    internal open suspend fun upsert(
        vararg data: Map<String, Any?>,
        onFailure: (HttpStatusCode) -> Unit,
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
            val result: List<Map<String, Any?>> = getClient().request(url, HttpMethod.Post, data) {
                applicationJson()
                preferences(presentation = true, merge = true, count = count)
            }
            onSuccess(TableSnapshot(result.map { RowSnapshot(it) }))
        }, onFailure)
    }


}

open class LimitedQueryR internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>,
    orders: MutableList<Order>,
    limit: Int?
) : LimitedQueryC(name, selections, range, count, filters, orders, limit) {
    public override suspend fun call(
        data: Any?,
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (String) -> Unit
    ) = super.call(data, onFailure, onSuccess)
}


open class LimitedQuery internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>,
    orders: MutableList<Order>,
    limit: Int?
) : LimitedQueryC(name, selections, range, count, filters, orders, limit) {
    public override suspend fun get(
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.get(onFailure, onSuccess)

    public override suspend fun delete(
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.delete(onFailure, onSuccess)

    public override suspend fun update(
        data: Map<String, Any?>,
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.update(data, onFailure, onSuccess)
}

open class LimitedQueryX internal constructor(
    name: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    orders: MutableList<Order>,
    limit: Int?
) : LimitedQueryC(name, selections, range, count, mutableListOf(), orders, limit) {
    public override suspend fun get(
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.get(onFailure, onSuccess)

    public override suspend fun delete(
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.delete(onFailure, onSuccess)

    public override suspend fun insert(
        column: String,
        value: Any?,
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.insert(column, value, onFailure, onSuccess)

    public override suspend fun insert(
        vararg data: Map<String, Any?>,
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.insert(data = data, onFailure, onSuccess)

    public override suspend fun upsert(
        column: String,
        value: Any?,
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.upsert(column, value, onFailure, onSuccess)

    public override suspend fun upsert(
        vararg data: Map<String, Any?>,
        onFailure: (HttpStatusCode) -> Unit,
        onSuccess: (TableSnapshot) -> Unit
    ) = super.upsert(data = data, onFailure, onSuccess)

}


