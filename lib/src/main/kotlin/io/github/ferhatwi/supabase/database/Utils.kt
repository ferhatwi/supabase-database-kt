package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase
import io.github.ferhatwi.supabase.database.snapshots.RowSnapshot
import io.github.ferhatwi.supabase.database.snapshots.TableSnapshot
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.utils.*
import io.ktor.http.*

internal fun databaseURL() = "https://${Supabase.PROJECT_ID}.supabase.co/rest/v1"

internal fun getClient(): HttpClient {
    return HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }
}


internal suspend inline fun <reified T> HttpClient.request(
    schema: String,
    url: String,
    method: HttpMethod,
    range: Pair<Int, Int>? = null,
    count: Count? = null,
    body: Any = EmptyContent,
    merge: Boolean = false,
    noinline headers: HeadersBuilder.() -> Unit = {}
): T {
    return request(url) {
        this.method = method
        this.body = body
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


internal suspend fun runCatching(block: suspend () -> Unit, onFailure: (HttpStatusCode) -> Unit) =
    runCatching { block() }.getOrElse {
        when (it) {
            is ResponseException -> onFailure(it.response.status)
            else -> throw it
        }
    }

internal suspend fun <A, B>runCatchingTransformation(
    block: suspend () -> A,
    transform : (A) -> B,
    onErrorValue : A,
    onSuccess: (B) -> Unit
) = runCatching { onSuccess(transform(block())) }.getOrElse {
        when (it) {
            is NoTransformationFoundException -> onSuccess(transform(onErrorValue))
            else -> throw it
        }
    }


@JvmName("asQueryStringSelect")
internal fun List<String>.asQueryString() = if (isEmpty()) {
    ""
} else {
    "select=${joinToString(separator = "&")}"
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

internal fun appendQueryString(vararg queryStrings: String): String {
    queryStrings.toMutableList().removeIf {
        it.isEmpty()
    }
    return queryStrings.joinToString(separator = "&")
}


/*
internal fun HeadersBuilder.preferences(presentation: Boolean, merge: Boolean, count: Count?) {
    val array = mutableListOf<String>()
    if (presentation) {
        array.add("return=representation")
    }
    if (merge) {
        array.add("resolution=merge-duplicates")
    }
    if (count != null) {
        array.add("count=$count")
    }
    append(HttpHeaders.Prefer, array.joinToString(separator = ","))
}*/
