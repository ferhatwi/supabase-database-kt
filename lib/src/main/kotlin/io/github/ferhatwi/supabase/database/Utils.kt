package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

internal fun databaseURL() = "https://${Supabase.PROJECT_ID}.supabase.co/rest/v1"

internal fun getClient(): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }
}

internal suspend inline fun HttpClient.request(
    schema: String,
    url: String,
    method: HttpMethod,
    range: Pair<Int, Int>? = null,
    count: Count? = null,
    body: Any = EmptyContent,
    merge: Boolean = false,
    noinline headers: HeadersBuilder.() -> Unit = {}
): HttpResponse {
    return request(url) {
        this.method = method
        setBody(body)
        headers {
            profile(method, schema)
            apiKey()
            authorize()
            range(range)
            preference(count, url.contains("select"), merge)
            headers()
        }
    }
}

internal fun HttpRequestBuilder.profile(httpMethod: HttpMethod, name: String) {
    if (name != "public") headers.append(
        if (httpMethod == HttpMethod.Get || httpMethod == HttpMethod.Head) "Accept-Profile" else "Content-Profile",
        name
    )
}

internal fun HttpRequestBuilder.apiKey() {
    headers.append("apikey", Supabase.API_KEY)
}

internal fun HttpRequestBuilder.authorize() {
    headers.append(HttpHeaders.Authorization, "Bearer ${Supabase.AUTHORIZATION}")
}

internal fun HttpRequestBuilder.range(range: Pair<Int, Int>?) {
    if (range != null) headers.append(HttpHeaders.Range, "${range.first}-${range.second}")
}

internal fun HttpRequestBuilder.preference(count: Count?, representation: Boolean, merge: Boolean) {
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

internal fun HeadersBuilder.applicationJson() {
    append(HttpHeaders.ContentType, "application/json")
}

@JvmName("asQueryStringSelect")
internal fun List<String>.asQueryString() = if (isEmpty()) {
    ""
} else {
    "select=${joinToString(separator = ",")}"
}


@JvmName("asQueryStringFilter")
internal fun MutableList<Filter>.asQueryString() = if (isEmpty()) {
    ""
} else {
    joinToString("&") {
        it.toString()
    }
}


@JvmName("asQueryStringOrder")
internal fun MutableList<Order>.asQueryString() = if (isEmpty()) {
    ""
} else {
    joinToString("&") {
        it.toString()
    }
}

internal fun limitToString(limit: Int?) = if (limit == null) "" else "limit=$limit"

internal fun appendQueryString(vararg queryStrings: String) = queryStrings.toMutableList().apply {
    removeIf {
        it.isEmpty()
    }
}.joinToString(separator = "&")

