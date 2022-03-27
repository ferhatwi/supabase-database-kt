package io.github.ferhatwi.supabase.database.request

import io.github.ferhatwi.supabase.database.Count
import io.github.ferhatwi.supabase.database.Filter
import io.github.ferhatwi.supabase.database.Range
import io.github.ferhatwi.supabase.database.TextConfig

private fun <A : LimitedQueryC> A.or(vararg filters: Filter): A =
    apply { this.filters.add(Filter.Or(*filters)) }

private fun <A : LimitedQueryC> A.and(vararg filters: Filter): A =
    apply { this.filters.add(Filter.And(*filters)) }

private fun <A : LimitedQueryC> A.not(filter: Filter): A =
    apply { filters.add(Filter.Not(filter)) }

private fun <T : Any, A : LimitedQueryC> A.equalTo(column: String, value: T): A =
    apply { filters.add(Filter.EqualTo(column, value)) }

private fun <T : Any, A : LimitedQueryC> A.notEqualTo(column: String, value: T): A =
    apply { filters.add(Filter.NotEqualTo(column, value)) }

private fun <T : Any, A : LimitedQueryC> A.greaterThan(column: String, value: T): A =
    apply { filters.add(Filter.GreaterThan(column, value)) }

private fun <T : Any, A : LimitedQueryC> A.greaterThanOrEqualTo(column: String, value: T): A =
    apply { filters.add(Filter.GreaterThanOrEqualTo(column, value)) }

private fun <T : Any, A : LimitedQueryC> A.lessThan(column: String, value: T): A =
    apply { filters.add(Filter.LessThan(column, value)) }

private fun <T : Any, A : LimitedQueryC> A.lessThanOrEqualTo(column: String, value: T): A =
    apply { filters.add(Filter.LessThanOrEqualTo(column, value)) }

private fun <A : LimitedQueryC> A.matchesPattern(
    column: String,
    pattern: String,
    caseSensitive: Boolean
): A =
    apply {
        filters.add(
            if (caseSensitive) {
                Filter.MatchesPattern.CaseSensitive(column, pattern)
            } else {
                Filter.MatchesPattern.CaseInsensitive(column, pattern)
            }
        )
    }

private fun <A : LimitedQueryC> A.`is`(column: String, value: Boolean?): A =
    apply { filters.add(Filter.Is(column, value)) }

private fun <T : Any, A : LimitedQueryC> A.`in`(column: String, value: Array<T>): A =
    apply { filters.add(Filter.In(column, value)) }

private fun <T : Any, A : LimitedQueryC> A.contains(column: String, value: Array<T>): A =
    apply { filters.add(Filter.Contains(column, value)) }

private fun <T : Any, A : LimitedQueryC> A.containedBy(column: String, value: Array<T>): A =
    apply { filters.add(Filter.ContainedBy(column, value)) }

private fun <A : LimitedQueryC> A.rangeLessThan(column: String, value: Range): A =
    apply { filters.add(Filter.RangeLessThan(column, value)) }

private fun <A : LimitedQueryC> A.rangeLessThanOrEqualTo(column: String, value: Range): A =
    apply { filters.add(Filter.RangeLessThanOrEqualTo(column, value)) }

private fun <A : LimitedQueryC> A.rangeGreaterThan(column: String, value: Range): A =
    apply { filters.add(Filter.RangeGreaterThan(column, value)) }

private fun <A : LimitedQueryC> A.rangeGreaterThanOrEqualTo(column: String, value: Range): A =
    apply { filters.add(Filter.RangeGreaterThanOrEqualTo(column, value)) }

private fun <A : LimitedQueryC> A.rangeAdjacentTo(column: String, value: Range): A =
    apply { filters.add(Filter.RangeAdjacentTo(column, value)) }

private fun <A : LimitedQueryC> A.rangeOverlaps(column: String, value: Range): A =
    apply { filters.add(Filter.RangeOverlaps(column, value)) }

private fun <A : LimitedQueryC> A.textSearch(column: String, value: String, config: TextConfig): A =
    apply { filters.add(Filter.TextSearch(column, value, config)) }


open class FilterableQueryR internal constructor(
    schema: String,
    function: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>
) : OrderableQueryR(schema, function, selections, range, count, filters, mutableListOf()) {

    fun or(vararg filters: Filter) = or<FilterableQueryR>(*filters)
    fun and(vararg filters: Filter) = and<FilterableQueryR>(*filters)
    fun not(filter: Filter) = not<FilterableQueryR>(filter)
    fun <T : Any> equalTo(column: String, value: T) = equalTo<T, FilterableQueryR>(column, value)
    fun <T : Any> notEqualTo(column: String, value: T) =
        notEqualTo<T, FilterableQueryR>(column, value)

    fun <T : Any> greaterThan(column: String, value: T) =
        greaterThan<T, FilterableQueryR>(column, value)

    fun <T : Any> greaterThanOrEqualTo(column: String, value: T) =
        greaterThanOrEqualTo<T, FilterableQueryR>(column, value)

    fun <T : Any> lessThan(column: String, value: T) = lessThan<T, FilterableQueryR>(column, value)
    fun <T : Any> lessThanOrEqualTo(column: String, value: T) =
        lessThanOrEqualTo<T, FilterableQueryR>(column, value)

    fun matchesPattern(column: String, pattern: String, caseSensitive: Boolean) =
        matchesPattern<FilterableQueryR>(column, pattern, caseSensitive)

    fun `is`(column: String, value: Boolean?) = `is`<FilterableQueryR>(column, value)
    fun <T : Any> `in`(column: String, value: Array<T>) = `in`<T, FilterableQueryR>(column, value)

    fun <T : Any> contains(column: String, value: Array<T>) =
        contains<T, FilterableQueryR>(column, value)

    fun <T : Any> containedBy(column: String, value: Array<T>) =
        containedBy<T, FilterableQueryR>(column, value)

    fun rangeLessThan(column: String, value: Range) = rangeLessThan<FilterableQueryR>(column, value)

    fun rangeLessThanOrEqualTo(column: String, value: Range) =
        rangeLessThanOrEqualTo<FilterableQueryR>(column, value)

    fun rangeGreaterThan(column: String, value: Range) =
        rangeGreaterThan<FilterableQueryR>(column, value)

    fun rangeGreaterThanOrEqualTo(column: String, value: Range) =
        rangeGreaterThanOrEqualTo<FilterableQueryR>(column, value)

    fun rangeAdjacentTo(column: String, value: Range) =
        rangeAdjacentTo<FilterableQueryR>(column, value)

    fun rangeOverlaps(column: String, value: Range) = rangeOverlaps<FilterableQueryR>(column, value)

    fun textSearch(column: String, value: String, config: TextConfig) =
        textSearch<FilterableQueryR>(column, value, config)
}

open class FilterableQuery internal constructor(
    schema : String,
    table: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
    filters: MutableList<Filter>
) : OrderableQuery(schema, table, selections, range, count, filters, mutableListOf()) {
    fun or(vararg filters: Filter) = or<FilterableQuery>(*filters)
    fun and(vararg filters: Filter) = and<FilterableQuery>(*filters)
    fun not(filter: Filter) = not<FilterableQuery>(filter)
    fun <T : Any> equalTo(column: String, value: T) = equalTo<T, FilterableQuery>(column, value)
    fun <T : Any> notEqualTo(column: String, value: T) =
        notEqualTo<T, FilterableQuery>(column, value)

    fun <T : Any> greaterThan(column: String, value: T) =
        greaterThan<T, FilterableQuery>(column, value)

    fun <T : Any> greaterThanOrEqualTo(column: String, value: T) =
        greaterThanOrEqualTo<T, FilterableQuery>(column, value)

    fun <T : Any> lessThan(column: String, value: T) = lessThan<T, FilterableQuery>(column, value)
    fun <T : Any> lessThanOrEqualTo(column: String, value: T) =
        lessThanOrEqualTo<T, FilterableQuery>(column, value)

    fun matchesPattern(column: String, pattern: String, caseSensitive: Boolean) =
        matchesPattern<FilterableQuery>(column, pattern, caseSensitive)

    fun `is`(column: String, value: Boolean?) = `is`<FilterableQuery>(column, value)
    fun <T : Any> `in`(column: String, value: Array<T>) = `in`<T, FilterableQuery>(column, value)

    fun <T : Any> contains(column: String, value: Array<T>) =
        contains<T, FilterableQuery>(column, value)

    fun <T : Any> containedBy(column: String, value: Array<T>) =
        containedBy<T, FilterableQuery>(column, value)

    fun rangeLessThan(column: String, value: Range) = rangeLessThan<FilterableQuery>(column, value)

    fun rangeLessThanOrEqualTo(column: String, value: Range) =
        rangeLessThanOrEqualTo<FilterableQuery>(column, value)

    fun rangeGreaterThan(column: String, value: Range) =
        rangeGreaterThan<FilterableQuery>(column, value)

    fun rangeGreaterThanOrEqualTo(column: String, value: Range) =
        rangeGreaterThanOrEqualTo<FilterableQuery>(column, value)

    fun rangeAdjacentTo(column: String, value: Range) =
        rangeAdjacentTo<FilterableQuery>(column, value)

    fun rangeOverlaps(column: String, value: Range) = rangeOverlaps<FilterableQuery>(column, value)

    fun textSearch(column: String, value: String, config: TextConfig) =
        textSearch<FilterableQuery>(column, value, config)
}

open class FilterableQueryX internal constructor(
    schema: String,
    table: String,
    selections: MutableList<String>,
    range: Pair<Int, Int>?,
    count: Count?,
) : OrderableQueryX(schema, table, selections, range, count, mutableListOf()) {
    private val filterableQuery = FilterableQuery(schema, table, selections, range, count, mutableListOf())

    fun or(vararg filters: Filter) = filterableQuery.or(*filters)
    fun and(vararg filters: Filter) = filterableQuery.and(*filters)
    fun not(filter: Filter) = filterableQuery.not(filter)
    open fun <T : Any> equalTo(column: String, value: T) = filterableQuery.equalTo(column, value)
    fun <T : Any> notEqualTo(column: String, value: T) =
        filterableQuery.notEqualTo(column, value)

    fun <T : Any> greaterThan(column: String, value: T) =
        filterableQuery.greaterThan(column, value)

    fun <T : Any> greaterThanOrEqualTo(column: String, value: T) =
        filterableQuery.greaterThanOrEqualTo(column, value)

    fun <T : Any> lessThan(column: String, value: T) = filterableQuery.lessThan(column, value)
    fun <T : Any> lessThanOrEqualTo(column: String, value: T) =
        filterableQuery.lessThanOrEqualTo(column, value)

    fun matchesPattern(column: String, pattern: String, caseSensitive: Boolean) =
        filterableQuery.matchesPattern(column, pattern, caseSensitive)

    fun `is`(column: String, value: Boolean?) = filterableQuery.`is`(column, value)
    fun <T : Any> `in`(column: String, value: Array<T>) = filterableQuery.`in`(column, value)

    fun <T : Any> contains(column: String, value: Array<T>) =
        filterableQuery.contains(column, value)

    fun <T : Any> containedBy(column: String, value: Array<T>) =
        filterableQuery.containedBy(column, value)

    fun rangeLessThan(column: String, value: Range) = filterableQuery.rangeLessThan(column, value)

    fun rangeLessThanOrEqualTo(column: String, value: Range) =
        filterableQuery.rangeLessThanOrEqualTo(column, value)

    fun rangeGreaterThan(column: String, value: Range) =
        filterableQuery.rangeGreaterThan(column, value)

    fun rangeGreaterThanOrEqualTo(column: String, value: Range) =
        filterableQuery.rangeGreaterThanOrEqualTo(column, value)

    fun rangeAdjacentTo(column: String, value: Range) =
        filterableQuery.rangeAdjacentTo(column, value)

    fun rangeOverlaps(column: String, value: Range) = filterableQuery.rangeOverlaps(column, value)

    fun textSearch(column: String, value: String, config: TextConfig) =
        filterableQuery.textSearch(column, value, config)
}