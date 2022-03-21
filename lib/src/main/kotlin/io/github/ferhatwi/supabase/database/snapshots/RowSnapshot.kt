package io.github.ferhatwi.supabase.database.snapshots

class RowSnapshot internal constructor(private val map: Map<String, Any?>) {

    override fun toString() = map.toString()
    fun get(key: String) = map[key]
    fun <T> getAsCustomType(key: String, transform: (Any?) -> T) = transform(map[key])
}