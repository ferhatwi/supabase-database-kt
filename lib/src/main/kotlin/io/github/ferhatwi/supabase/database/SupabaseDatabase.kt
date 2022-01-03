package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase

class SupabaseDatabase {

    companion object {
        fun getInstance() : SupabaseDatabase {
            return SupabaseDatabase()
        }
    }

    fun table(tableName : String, vararg columnName : ColumnReference = emptyArray()) : TableReference {
        val request = Query().apply {
            table = TableReference(tableName, *columnName)
        }
        return request.table
    }

}

fun Supabase.database() : SupabaseDatabase {
    return SupabaseDatabase.getInstance()
}
