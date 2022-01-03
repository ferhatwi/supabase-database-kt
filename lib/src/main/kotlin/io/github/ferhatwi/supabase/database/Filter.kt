package io.github.ferhatwi.supabase.database

sealed class TextConfig(val config: String?) {
    class Plain(config: String? = null) : TextConfig(config)
    class Phrase(config: String? = null) : TextConfig(config)
    class Website(config: String? = null) : TextConfig(config)
    class None(config: String? = null) : TextConfig(config)
}

sealed class Interval {
    object Closed : Interval()
    object Open : Interval()
}

data class Range(
    val leftInterval: Interval,
    val from: Int,
    val to: Int,
    val rightInterval: Interval
)

sealed class Filter {
    class Or(vararg val filter: Filter) : Filter()
    class And(vararg val filter: Filter) : Filter()
    class Not(val filter: Filter) : Filter()

    //data class Matches<T>(val json : String) : Filter()

    class EqualTo<T : Any>(val column: String, val value: T) : Filter()
    class NotEqualTo<T : Any>(val column: String, val value: T) : Filter()
    class GreaterThan<T : Any>(val column: String, val value: T) : Filter()
    class GreaterThanOrEqualTo<T : Any>(val column: String, val value: T) : Filter()
    class LessThan<T : Any>(val column: String, val value: T) : Filter()
    class LessThanOrEqualTo<T : Any>(val column: String, val value: T) : Filter()
    sealed class MatchesPattern(open val column: String, val pattern: String) : Filter() {
        class CaseSensitive(column: String, pattern: String) : MatchesPattern(column, pattern)
        class CaseInsensitive(column: String, pattern: String) : MatchesPattern(column, pattern)
    }

    class Is(val column: String, val value: Boolean?) : Filter()
    class In<T : Any>(val column: String, val value: Array<T>) : Filter()
    class Contains<T : Any>(val column: String, val value: Array<T>) : Filter()
    class ContainedBy<T : Any>(val column: String, val value: Array<T>) : Filter()
    class RangeLessThan(val column: String, val value: Range) : Filter()
    class RangeLessThanOrEqualTo(val column: String, val value: Range) : Filter()
    class RangeGreaterThan(val column: String, val value: Range) : Filter()
    class RangeGreaterThanOrEqualTo(val column: String, val value: Range) : Filter()
    class RangeAdjacentTo(val column: String, val value: Range) : Filter()
    class RangeOverlaps(val column: String, val value: Range) : Filter()
    class TextSearch(
        val column: String,
        val value: String,
        val config: TextConfig
    ) : Filter()


    //data class Filter<T>(val json : String) : Filter()
    //It's nice, leave it
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
        is Filter.EqualTo<*> -> "${column}=eq.${value})"
        is Filter.NotEqualTo<*> -> "${column}=neq.${value})"
        is Filter.GreaterThan<*> -> "${column}=gt.${value})"
        is Filter.GreaterThanOrEqualTo<*> -> "${column}=gte.${value})"
        is Filter.LessThan<*> -> "${column}=lt.${value})"
        is Filter.LessThanOrEqualTo<*> -> "${column}=lte.${value})"
        is Filter.MatchesPattern.CaseSensitive -> "${column}=like.${pattern})"
        is Filter.MatchesPattern.CaseInsensitive -> "${column}=ilike.${pattern})"
        is Filter.Is -> "${column}=is.${value})"
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
        })"
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
        })"
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
        })"
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
        })"
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
        })"
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
        })"
        is Filter.TextSearch -> {
            "$column=${
                when (config) {
                    is TextConfig.Plain -> "p1"
                    is TextConfig.Phrase -> "ph"
                    is TextConfig.Website -> "w1"
                    is TextConfig.None -> ""
                }
            }fts${config.config}.${value})"
        }

    }
}