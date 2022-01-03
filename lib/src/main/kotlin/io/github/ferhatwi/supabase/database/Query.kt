package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.API
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.HttpHeaders

open class Query{
    internal lateinit var table : TableReference

    private val filter = mutableListOf<Filter>()
    private lateinit var range : Pair<Int, Int>

    private fun isRangeInitialized() : Boolean {
        return ::range.isInitialized
    }

    private fun finalizeFilterRequest() : String {
        return "${table.tableRequest}?" +
                if (filter.isEmpty()) {
                    ""
                } else {
                    filter.joinToString("&") {
                        it.asQueryString()
                    }
                }
    }

    internal fun finalizeSelectionRequest() : String {
        return "&select=" + if (table.columnName.isEmpty()) {
            "*"
        } else {
            table.columnName.joinToString(",") {
                it.name
            }
        }
    }

    private fun finalizeRequest() : String {
        return finalizeFilterRequest() + finalizeSelectionRequest()
    }

    internal fun getClient() : HttpClient {
        return HttpClient(CIO) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
    }

    suspend fun get(): TableSnapshot {
        val request = finalizeRequest()
        val client = getClient()

        val result: List<HashMap<String, Any?>> = client.get(request) {
            headers {
                append("apikey", API)
                append(HttpHeaders.Authorization, API)
                if (isRangeInitialized()) {
                    append(HttpHeaders.Range, "${range.first}-${range.second}")
                }
            }
        }
        return TableSnapshot(result)
    }

    suspend fun upsert(data : HashMap<String, Any?>): TableSnapshot {
        val request = finalizeRequest()
        val client = getClient()

        val result: List<HashMap<String, Any?>> = client.post(request) {
            headers {
                append("apikey", API)
                append(HttpHeaders.Authorization, API)
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Prefer, "return=representation")
                append(HttpHeaders.Prefer, "resolution=merge-duplicates")
            }
            body = data
        }
        return TableSnapshot(result)
    }

    suspend fun update(data : HashMap<String, Any?>): TableSnapshot {
        val request = finalizeRequest()
        val client = getClient()

        val result: List<HashMap<String, Any?>> = client.patch(request) {
            headers {
                append("apikey", API)
                append(HttpHeaders.Authorization, API)
                append(HttpHeaders.ContentType, "application/json")
            }
            body = data
        }
        return TableSnapshot(result)
    }



    suspend fun delete(): TableSnapshot {
        val request = finalizeRequest()
        val client = getClient()

        val result: List<HashMap<String, Any?>> = client.delete(request) {
            headers {
                append("apikey", API)
                append(HttpHeaders.Authorization, API)
            }
        }
        return TableSnapshot(result)
    }



    fun or(vararg filters : Filter): Query {
        return this.apply {
            filter.add(Filter.Or(*filters))
        }
    }

    fun and(vararg filters : Filter): Query {
        return this.apply {
            filter.add(Filter.And(*filters))
        }
    }

    fun not(filter : Filter): Query {
        return this.apply {
            this@Query.filter.add(Filter.Not(filter))
        }
    }

    fun <T : Any>equalTo(columnName: String, value: T): Query {
        return this.apply {
            filter.add(Filter.EqualTo(columnName, value))
        }
    }

    fun <T : Any>notEqualTo(columnName: String, value: T): Query {
        return this.apply {
            filter.add(Filter.NotEqualTo(columnName, value))
        }
    }
    fun <T : Any>greaterThan(columnName: String, value: T): Query {
        return this.apply {
            filter.add(Filter.GreaterThan(columnName, value))
        }
    }
    fun <T : Any>greaterThanOrEqualTo(columnName: String, value: T): Query {
        return this.apply {
            filter.add(Filter.GreaterThanOrEqualTo(columnName, value))
        }
    }
    fun <T : Any>lessThan(columnName: String, value: T): Query {
        return this.apply {
            filter.add(Filter.LessThan(columnName, value))
        }
    }
    fun <T : Any>lessThanOrEqualTo(columnName: String, value: T): Query {
        return this.apply {
            filter.add(Filter.LessThanOrEqualTo(columnName, value))
        }
    }

    fun matchesPattern(columnName: String, pattern: String, caseSensitive : Boolean): Query {
        val matchesPattern : Filter = if (caseSensitive) {
            Filter.MatchesPattern.CaseSensitive(columnName, pattern)
        } else {
            Filter.MatchesPattern.CaseInsensitive(columnName, pattern)
        }
        return this.apply {
            filter.add(matchesPattern)
        }
    }
    fun `is`(columnName: String, value: Boolean?): Query {
        return this.apply {
            filter.add(Filter.Is(columnName, value))
        }
    }
    fun <T : Any>`in`(columnName: String, value: Array<T>): Query {
        return this.apply {
            filter.add(Filter.In(columnName, value))
        }
    }
    fun <T : Any>contains(columnName: String, value: Array<T>): Query {
        return this.apply {
            filter.add(Filter.Contains(columnName, value))
        }
    }
    fun <T : Any>containedBy(columnName: String, value: Array<T>): Query {
        return this.apply {
            filter.add(Filter.ContainedBy(columnName, value))
        }
    }

    fun rangeLessThan(columnName: String, value: Range): Query {
        return this.apply {
            filter.add(Filter.RangeLessThan(columnName, value))
        }
    }
    fun rangeLessThanOrEqualTo(columnName: String, value: Range): Query {
        return this.apply {
            filter.add(Filter.RangeLessThanOrEqualTo(columnName, value))
        }
    }
    fun rangeGreaterThan(columnName: String, value: Range): Query {
        return this.apply {
            filter.add(Filter.RangeGreaterThan(columnName, value))
        }
    }
    fun rangeGreaterThanOrEqualTo(columnName: String, value: Range): Query {
        return this.apply {
            filter.add(Filter.RangeGreaterThanOrEqualTo(columnName, value))
        }
    }
    fun rangeAdjacentTo(columnName: String, value: Range): Query {
        return this.apply {
            filter.add(Filter.RangeAdjacentTo(columnName, value))
        }
    }

    fun rangeOverlaps(columnName: String, value: Range): Query {
        return this.apply {
            filter.add(Filter.RangeOverlaps(columnName, value))
        }
    }

    fun textSearch(columnName: String, value: String, config: TextConfig = TextConfig.None()): Query {
        return this.apply {
            filter.add(Filter.TextSearch(columnName, value, config))
        }
    }



    fun withRange(from: Int, to: Int): Query {
        return this.apply {
            range = Pair(from, to)
        }
    }

}