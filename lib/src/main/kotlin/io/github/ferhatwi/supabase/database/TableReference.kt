package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.API
import io.ktor.client.request.*
import io.ktor.http.*


class TableReference internal constructor(
    tableName: String,
    vararg val columnName: ColumnReference
) : Query() {
    internal val tableRequest = "$API/rest/v1/$tableName"

    suspend fun add(data : HashMap<String, Any?>): TableSnapshot {
        val request = finalizeSelectionRequest()
        val client = getClient()

        val result: List<HashMap<String, Any?>> = client.post(request) {
            headers {
                append("apikey", API)
                append(HttpHeaders.Authorization, API)
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Prefer, "return=representation")
            }
            body = data
        }
        return TableSnapshot(result)
    }

}

