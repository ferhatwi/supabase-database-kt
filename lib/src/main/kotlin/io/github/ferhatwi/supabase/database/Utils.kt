package io.github.ferhatwi.supabase.database

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*


internal fun Modifier.asQueryString(): String {
    return when (this) {
        is Modifier.Limit -> "limit=$count"
        is Modifier.Order -> "order=$column.${
            when (orderBy) {
                OrderBy.Ascending -> "asc"
                OrderBy.Descending -> "desc"
            }
        }.${if (nullsFirst) "nullfirst" else "nullslast"}"
    }
}

internal fun Filter.asQueryString(): String {

    return when (this) {
        is Filter.Or -> {
            "or=(${
                filter.joinToString(",") {
                    it.asQueryString().replace("=", ",")
                }
            })"
        }
        is Filter.And -> "and(${
            filter.joinToString(",") {
                it.asQueryString().replace("=", ",")
            }
        })"
        is Filter.Not -> {
            when (filter) {
                is Filter.And -> "not.${filter.asQueryString()}"
                is Filter.Not -> filter.asQueryString().replace("not.", "")
                else -> filter.asQueryString().replaceFirst("=", "=not.")
            }
        }
        is Filter.EqualTo<*> -> "${column}=eq.${value}"
        is Filter.NotEqualTo<*> -> "${column}=neq.${value}"
        is Filter.GreaterThan<*> -> "${column}=gt.${value}"
        is Filter.GreaterThanOrEqualTo<*> -> "${column}=gte.${value}"
        is Filter.LessThan<*> -> "${column}=lt.${value}"
        is Filter.LessThanOrEqualTo<*> -> "${column}=lte.${value}"
        is Filter.MatchesPattern.CaseSensitive -> "${column}=like.${pattern}"
        is Filter.MatchesPattern.CaseInsensitive -> "${column}=ilike.${pattern}"
        is Filter.Is -> "${column}=is.${value}"
        is Filter.In<*> -> "${column}=in.${value.joinToString(prefix = "(", postfix = ")")}"
        is Filter.Contains<*> -> "${column}=in.${value.joinToString(prefix = "[", postfix = "]")}"
        is Filter.ContainedBy<*> -> "${column}=in.${
            value.joinToString(
                prefix = "[",
                postfix = "]"
            )
        }"
        is Filter.RangeLessThan -> "${column}=sr.${
            when (value.leftInterval) {
                Interval.Open -> "("
                Interval.Closed -> "["
            }
        }${value.from},${value.to}${
            when (value.rightInterval) {
                Interval.Open -> ")"
                Interval.Closed -> "]"
            }
        }"
        is Filter.RangeLessThanOrEqualTo -> "${column}=nxl.${
            when (value.leftInterval) {
                Interval.Open -> "("
                Interval.Closed -> "["
            }
        }${value.from},${value.to}${
            when (value.rightInterval) {
                Interval.Open -> ")"
                Interval.Closed -> "]"
            }
        }"
        is Filter.RangeGreaterThan -> "${column}=sl.${
            when (value.leftInterval) {
                Interval.Open -> "("
                Interval.Closed -> "["
            }
        }${value.from},${value.to}${
            when (value.rightInterval) {
                Interval.Open -> ")"
                Interval.Closed -> "]"
            }
        }"
        is Filter.RangeGreaterThanOrEqualTo -> "${column}=nxr.${
            when (value.leftInterval) {
                Interval.Open -> "("
                Interval.Closed -> "["
            }
        }${value.from},${value.to}${
            when (value.rightInterval) {
                Interval.Open -> ")"
                Interval.Closed -> "]"
            }
        }"
        is Filter.RangeAdjacentTo -> "${column}=adj.${
            when (value.leftInterval) {
                Interval.Open -> "("
                Interval.Closed -> "["
            }
        }${value.from},${value.to}${
            when (value.rightInterval) {
                Interval.Open -> ")"
                Interval.Closed -> "]"
            }
        }"
        is Filter.RangeOverlaps -> "${column}=ov.${
            when (value.leftInterval) {
                Interval.Open -> "("
                Interval.Closed -> "["
            }
        }${value.from},${value.to}${
            when (value.rightInterval) {
                Interval.Open -> ")"
                Interval.Closed -> "]"
            }
        }"
        is Filter.TextSearch -> {
            "$column=${
                when (config) {
                    is TextConfig.Plain -> "p1"
                    is TextConfig.Phrase -> "ph"
                    is TextConfig.Website -> "w1"
                    is TextConfig.None -> ""
                }
            }fts${config.config}.${value}"
        }

    }
}


internal fun getClient(): HttpClient {
    return HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }
}

@JvmName("asQueryStringSelect")
internal fun MutableList<String>.asQueryString(): String {
    return if (isEmpty()) {
        "select=*"
    } else {
        "select=${joinToString(separator = "&")}"
    }
}

@JvmName("asQueryStringFilter")
internal fun MutableList<Filter>.asQueryString(): String {
    return if (isEmpty()) {
        ""
    } else {
        joinToString("&") {
            it.asQueryString()
        }
    }
}

@JvmName("asQueryStringModifier")
internal fun MutableList<Modifier>.asQueryString(): String {
    return if (isEmpty()) {
        ""
    } else {
        joinToString("&") {
            it.asQueryString()
        }
    }
}

internal fun appendQueryString(vararg queryStrings: String): String {
    return queryStrings.joinToString(separator = "&").replace("&&", "&").replace("&&", "&")
}



