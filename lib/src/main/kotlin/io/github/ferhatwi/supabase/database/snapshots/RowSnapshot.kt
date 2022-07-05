package io.github.ferhatwi.supabase.database.snapshots

data class RowSnapshot internal constructor(private val map: Map<String, Any?>) {

    fun get(key: String) = map[key]

    inline fun <reified T : Any> maybeGet(key: String): T? = get(key).let {
        if (it is T) it else null
    }

    @JvmName("getT")
    inline fun <reified T : Any> get(key: String): T = maybeGet(key)!!

    fun maybeGetAsInnerRow(key: String) = get(key).let {
        @Suppress("UNCHECKED_CAST")
        if (it is Map<*, *>) RowSnapshot(map[key] as Map<String, Any?>) else null
    }

    fun getAsInnerRow(key: String) = maybeGetAsInnerRow(key)!!

    fun <T> getTransforming(key: String, transform: (Any?) -> T) = transform(map[key])

    fun contains(key: String) = map.contains(key)
    fun containsValue(key: String) = map.containsValue(key)
}