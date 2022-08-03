package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase

class SupabaseDatabase {

    companion object {
        fun getInstance() = SupabaseDatabase()
    }

}

val Supabase.database get() = SupabaseDatabase.getInstance()
