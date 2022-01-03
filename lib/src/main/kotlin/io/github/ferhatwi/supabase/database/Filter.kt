package io.github.ferhatwi.supabase.database

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
}

