package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase
import io.ktor.client.request.*
import io.ktor.http.*

class XQuery internal constructor(query: Query) : Query(
    query.table,
    query.selections,
    query.range,
    query.filters,
    query.modifiers
) {

    suspend fun insert(vararg data: Map<String, Any?>): TableSnapshot {
        return add(data = data, merge = false)
    }

    suspend fun upsert(vararg data: Map<String, Any?>): TableSnapshot {
        return add(data = data, merge = true)
    }

    private suspend fun add(vararg data: Map<String, Any?>, merge: Boolean): TableSnapshot {
        val request =
            "https://${Supabase.PROJECT_ID}.supabase.co/rest/v1/$table?${selections.asQueryString()}"

        val result: List<HashMap<String, Any?>> = getClient().post(request) {
            headers {
                append("apikey", Supabase.API_KEY)
                append(HttpHeaders.Authorization, Supabase.API_KEY)
                append(HttpHeaders.ContentType, "application/json")
                append(
                    HttpHeaders.Prefer,
                    "return=representation${if (merge) "&resolution=merge-duplicates" else ""}"
                )

                range?.let {
                    append(HttpHeaders.Range, "${it.first}-${it.second}")
                }
            }

            body = data
        }
        return TableSnapshot(result)
    }




}