package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase

class SupabaseDatabase {

    companion object {
        fun getInstance(): SupabaseDatabase {
            return SupabaseDatabase()
        }
    }

    fun table(name: String): TableReference {
        return TableReference(name)
    }

}

fun Supabase.database(): SupabaseDatabase {
    return SupabaseDatabase.getInstance()
}
