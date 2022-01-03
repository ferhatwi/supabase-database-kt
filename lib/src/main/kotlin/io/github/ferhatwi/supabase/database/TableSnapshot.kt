package io.github.ferhatwi.supabase.database

import com.google.gson.Gson

class TableSnapshot internal constructor(private val list: List<Map<String, Any?>>) {

    override fun toString(): String {
        return Gson().toJson(list)
    }

    fun getRows(): List<RowSnapshot> {
        return list.map {
            RowSnapshot(it)
        }
    }

}