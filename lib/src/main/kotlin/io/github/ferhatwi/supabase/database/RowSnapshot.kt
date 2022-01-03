package io.github.ferhatwi.supabase.database

import com.google.gson.Gson

class RowSnapshot internal constructor(private val map: Map<String, Any?>) {

    override fun toString(): String {
        return Gson().toJson(map)
    }

    fun get(key: String): Any? {
        return map[key]
    }

    fun <T> getAsCustomType(key: String, transform: (Any?) -> T): T {
        return transform(map[key])
    }

}