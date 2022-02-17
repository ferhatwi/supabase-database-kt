package io.github.ferhatwi.supabase.database

sealed class Filter {

    override fun toString(): String {
        return when (this) {
            is Or -> {
                "or=(${
                    filter.joinToString(",") {
                        it.toString().replace("=", ".")
                    }
                })"
            }
            is And -> "and=(${
                filter.joinToString(",") {
                    it.toString().replace("=", ".")
                }
            })"
            is Not -> {
                when (filter) {
                    is And -> "not.${filter.toString()}"
                    is Not -> filter.toString().replace("not.", "")
                    else -> filter.toString().replaceFirst("=", "=not.")
                }
            }
            is EqualTo<*> -> "${column}=eq.${value}"
            is NotEqualTo<*> -> "${column}=neq.${value}"
            is GreaterThan<*> -> "${column}=gt.${value}"
            is GreaterThanOrEqualTo<*> -> "${column}=gte.${value}"
            is LessThan<*> -> "${column}=lt.${value}"
            is LessThanOrEqualTo<*> -> "${column}=lte.${value}"
            is MatchesPattern.CaseSensitive -> "${column}=like.${pattern}"
            is MatchesPattern.CaseInsensitive -> "${column}=ilike.${pattern}"
            is Is -> "${column}=is.${value}"
            is In<*> -> "${column}=in.${value.joinToString(prefix = "(", postfix = ")")}"
            is Contains<*> -> "${column}=in.${value.joinToString(prefix = "[", postfix = "]")}"
            is ContainedBy<*> -> "${column}=in.${
                value.joinToString(
                    prefix = "[",
                    postfix = "]"
                )
            }"
            is RangeLessThan -> "${column}=sr.${
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
            is RangeLessThanOrEqualTo -> "${column}=nxl.${
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
            is RangeGreaterThan -> "${column}=sl.${
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
            is RangeGreaterThanOrEqualTo -> "${column}=nxr.${
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
            is RangeAdjacentTo -> "${column}=adj.${
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
            is RangeOverlaps -> "${column}=ov.${
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
            is TextSearch -> {
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
}

