package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase
import io.github.ferhatwi.supabase.database.query.Count
import io.github.ferhatwi.supabase.database.query.Filter
import io.github.ferhatwi.supabase.database.query.Order
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

@InternalSupabaseDatabaseAPI
val databaseURL = "https://${Supabase.PROJECT_ID}.supabase.co/rest/v1"


@InternalSupabaseDatabaseAPI
val client
    get() = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }


@InternalSupabaseDatabaseAPI
suspend inline fun HttpClient.post(
    schema: String,
    url: String,
    count: Count?,
    range: Pair<Int, Int>?,
    body: Any = EmptyContent,
    mergeDuplicated: Boolean
) = post(url) {
    contentType(ContentType.Application.Json)
    setBody(body)
    headers {
        append("Content-Profile", schema)
        apiKey()
        authorize()
        range(range)
        preference(count, url.contains("select"), mergeDuplicated)
    }
}


@InternalSupabaseDatabaseAPI
suspend inline fun HttpClient.head(
    schema: String,
    url: String,
    count: Count?,
    range: Pair<Int, Int>?,
    body: Any = EmptyContent
) = head(url) {
    contentType(ContentType.Application.Json)
    setBody(body)
    headers {
        append("Accept-Profile", schema)
        apiKey()
        authorize()
        range(range)
        preference(count, representation = false, merge = false)
    }
}

@InternalSupabaseDatabaseAPI
suspend inline fun HttpClient.get(
    schema: String,
    url: String,
    count: Count?,
    range: Pair<Int, Int>?
) = get(url) {
    headers {
        append("Accept-Profile", schema)
        apiKey()
        authorize()
        range(range)
        preference(count, representation = url.contains("select"), merge = false)
    }
}

@InternalSupabaseDatabaseAPI
suspend inline fun HttpClient.delete(
    schema: String,
    url: String,
    count: Count?,
    range: Pair<Int, Int>?
) = delete(url) {
    headers {
        append("Content-Profile", schema)
        apiKey()
        authorize()
        range(range)
        preference(count, representation = url.contains("select"), merge = false)
    }
}


@InternalSupabaseDatabaseAPI
suspend inline fun HttpClient.patch(
    schema: String,
    url: String,
    count: Count?,
    range: Pair<Int, Int>?,
    body: Any = EmptyContent
) = patch(url) {
    contentType(ContentType.Application.Json)
    setBody(body)
    headers {
        append("Content-Profile", schema)
        apiKey()
        authorize()
        range(range)
        preference(count, representation = url.contains("select"), merge = false)
    }
}


@InternalSupabaseDatabaseAPI
fun HttpRequestBuilder.apiKey() {
    headers.append("apikey", Supabase.API_KEY)
}

@InternalSupabaseDatabaseAPI
fun HttpRequestBuilder.authorize() {
    headers.append(HttpHeaders.Authorization, "Bearer ${Supabase.AUTHORIZATION}")
}

@InternalSupabaseDatabaseAPI
fun HttpRequestBuilder.range(range: Pair<Int, Int>?) {
    if (range != null) headers.append(HttpHeaders.Range, "${range.first}-${range.second}")
}

@InternalSupabaseDatabaseAPI
fun HttpRequestBuilder.preference(count: Count?, representation: Boolean, merge: Boolean) {
    val array = mutableListOf<String>()
    if (count != null) {
        array.add("count=$count")
    }
    if (representation) {
        array.add("return=representation")
    }
    if (merge) {
        array.add("resolution=merge-duplicates")
    }
    headers.append(HttpHeaders.Prefer, array.joinToString(separator = ","))
}

@InternalSupabaseDatabaseAPI
@JvmName("toQuerySelect")
fun List<String>.toQuery() = if (isEmpty()) {
    ""
} else {
    "select=${joinToString(separator = ",")}"
}

@InternalSupabaseDatabaseAPI
@JvmName("toQueryFilter")
fun List<Filter>.toQuery() = if (isEmpty()) {
    ""
} else {
    joinToString("&") {
        it.toString()
    }
}

@InternalSupabaseDatabaseAPI
@JvmName("toQueryOrder")
fun List<Order>.toQuery() = if (isEmpty()) {
    ""
} else {
    joinToString("&") {
        it.toString()
    }
}

@InternalSupabaseDatabaseAPI
fun appendQueries(vararg queryStrings: String) = queryStrings.toMutableList().apply {
    removeIf {
        it.isEmpty()
    }
}.joinToString(separator = "&").encodeURLPath()

