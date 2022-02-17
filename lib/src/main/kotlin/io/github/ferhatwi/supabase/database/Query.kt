package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase
import io.ktor.client.request.*
import io.ktor.http.*

open class Query internal constructor(
    internal val table: String,
    internal val selections: MutableList<String> = mutableListOf(),
    internal var range: Pair<Int, Int>? = null,
    internal val filters: MutableList<Filter> = mutableListOf(),
    internal val modifiers: MutableList<Modifier> = mutableListOf()
) {


    suspend fun update(data: Map<String, Any?>): TableSnapshot {
        val request = "https://${Supabase.PROJECT_ID}.supabase.co/rest/v1/$table?${
            appendQueryString(
                filters.asQueryString(),
                modifiers.asQueryString(),
                selections.asQueryString()
            )
        }"

        val result: List<Map<String, Any?>> = getClient().patch(request) {
            headers {
                append("apikey", Supabase.API_KEY)
                append(HttpHeaders.Authorization, Supabase.API_KEY)
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Prefer, "return=representation")

                range?.let {
                    append(HttpHeaders.Range, "${it.first}-${it.second}")
                }
            }

            body = data
        }
        return TableSnapshot(result)
    }

    suspend fun get(): TableSnapshot {
        val request = "https://${Supabase.PROJECT_ID}.supabase.co/rest/v1/$table?${
            appendQueryString(
                filters.asQueryString(),
                modifiers.asQueryString(),
                selections.asQueryString()
            )
        }"

        val result: List<Map<String, Any?>> = getClient().get(request) {
            headers {
                append("apikey", Supabase.API_KEY)
                append(HttpHeaders.Authorization, Supabase.API_KEY)

                range?.let {
                    append(HttpHeaders.Range, "${it.first}-${it.second}")
                }
            }
        }
        return TableSnapshot(result)
    }

    suspend fun delete(): TableSnapshot {
        val request = "https://${Supabase.PROJECT_ID}.supabase.co/rest/v1/$table?${
            appendQueryString(
                filters.asQueryString(),
                modifiers.asQueryString(),
                selections.asQueryString()
            )
        }"


        val result: List<Map<String, Any?>> = getClient().delete(request) {
            headers {
                append("apikey", Supabase.API_KEY)
                append(HttpHeaders.Authorization, Supabase.API_KEY)
                append(HttpHeaders.Prefer, "return=representation")

                range?.let {
                    append(HttpHeaders.Range, "${it.first}-${it.second}")
                }
            }
        }
        return TableSnapshot(result)
    }

    fun select(vararg columns: String): XQuery {
        return XQuery(this.apply {
            selections.addAll(columns.toMutableList())
        })
    }

    fun range(from: Int, to: Int): XQuery {
        return XQuery(this.apply {
            range = Pair(from, to)
        })
    }


    fun or(vararg filters: Filter): Query {
        return this.apply {
            this.filters.add(Filter.Or(*filters))
        }
    }

    fun and(vararg filters: Filter): Query {
        return this.apply {
            this.filters.add(Filter.And(*filters))
        }
    }

    fun not(filter: Filter): Query {
        return this.apply {
            filters.add(Filter.Not(filter))
        }
    }

    open fun <T : Any> equalTo(column: String, value: T): Query {
        return this.apply {
            filters.add(Filter.EqualTo(column, value))
        }
    }

    fun <T : Any> notEqualTo(column: String, value: T): Query {
        return this.apply {
            filters.add(Filter.NotEqualTo(column, value))
        }
    }

    fun <T : Any> greaterThan(column: String, value: T): Query {
        return this.apply {
            filters.add(Filter.GreaterThan(column, value))
        }
    }

    fun <T : Any> greaterThanOrEqualTo(column: String, value: T): Query {
        return this.apply {
            filters.add(Filter.GreaterThanOrEqualTo(column, value))
        }
    }

    fun <T : Any> lessThan(column: String, value: T): Query {
        return this.apply {
            filters.add(Filter.LessThan(column, value))
        }
    }

    fun <T : Any> lessThanOrEqualTo(column: String, value: T): Query {
        return this.apply {
            filters.add(Filter.LessThanOrEqualTo(column, value))
        }
    }

    fun matchesPattern(column: String, pattern: String, caseSensitive: Boolean): Query {
        val matchesPattern: Filter = if (caseSensitive) {
            Filter.MatchesPattern.CaseSensitive(column, pattern)
        } else {
            Filter.MatchesPattern.CaseInsensitive(column, pattern)
        }
        return this.apply {
            filters.add(matchesPattern)
        }
    }

    fun `is`(column: String, value: Boolean?): Query {
        return this.apply {
            filters.add(Filter.Is(column, value))
        }
    }

    fun <T : Any> `in`(column: String, value: Array<T>): Query {
        return this.apply {
            filters.add(Filter.In(column, value))
        }
    }

    fun <T : Any> contains(column: String, value: Array<T>): Query {
        return this.apply {
            filters.add(Filter.Contains(column, value))
        }
    }

    fun <T : Any> containedBy(column: String, value: Array<T>): Query {
        return this.apply {
            filters.add(Filter.ContainedBy(column, value))
        }
    }

    fun rangeLessThan(column: String, value: Range): Query {
        return this.apply {
            filters.add(Filter.RangeLessThan(column, value))
        }
    }

    fun rangeLessThanOrEqualTo(column: String, value: Range): Query {
        return this.apply {
            filters.add(Filter.RangeLessThanOrEqualTo(column, value))
        }
    }

    fun rangeGreaterThan(column: String, value: Range): Query {
        return this.apply {
            filters.add(Filter.RangeGreaterThan(column, value))
        }
    }

    fun rangeGreaterThanOrEqualTo(column: String, value: Range): Query {
        return this.apply {
            filters.add(Filter.RangeGreaterThanOrEqualTo(column, value))
        }
    }

    fun rangeAdjacentTo(column: String, value: Range): Query {
        return this.apply {
            filters.add(Filter.RangeAdjacentTo(column, value))
        }
    }

    fun rangeOverlaps(column: String, value: Range): Query {
        return this.apply {
            filters.add(Filter.RangeOverlaps(column, value))
        }
    }

    fun textSearch(
        column: String,
        value: String,
        config: TextConfig = TextConfig.None()
    ): Query {
        return this.apply {
            filters.add(Filter.TextSearch(column, value, config))
        }
    }


    fun limit(count: Int): Query {
        return this.apply {
            modifiers.add(Modifier.Limit(count))
        }
    }

    fun order(
        column: String,
        orderBy: OrderBy = OrderBy.Ascending,
        nullsFirst: Boolean = false
    ): Query {
        return this.apply {
            modifiers.add(Modifier.Order(column, orderBy, nullsFirst))
        }
    }


}