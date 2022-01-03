package io.github.ferhatwi.supabase.database

class TableSnapshot internal constructor(private val list : List<HashMap<String, Any?>>) {

    fun get() : List<RowSnapshot> {
        return list.map {
            RowSnapshot(it)
        }
    }

}