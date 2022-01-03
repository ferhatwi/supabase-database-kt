package io.github.ferhatwi.supabase.database

class RowSnapshot internal constructor(private val hashMap : HashMap<String, Any?>) {

    fun get(key : String) : Any? {
        return hashMap[key]
    }

}