package io.github.ferhatwi.supabase.database

import com.google.gson.Gson

class ListenSnapshot internal constructor(val row: RowSnapshot, val event: Event) {

    override fun toString(): String {
        return Gson().toJson(listOf(row, event))
    }

}