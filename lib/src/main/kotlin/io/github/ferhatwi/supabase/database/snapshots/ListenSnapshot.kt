package io.github.ferhatwi.supabase.database.snapshots

import io.github.ferhatwi.supabase.database.Event

data class ListenSnapshot internal constructor(
    val record: RowSnapshot,
    val oldRecord: RowSnapshot?,
    val event: Event
) {

    fun isUpdated(key: String): Boolean? = oldRecord?.let {
        record.get(key)?.let { recordValue ->
            it.get(key)?.let { oldRecordValue ->
                recordValue == oldRecordValue
            }
        }
    }

    inline fun ifUpdated(key: String, block: (recordValue: Any) -> Unit) {
        isUpdated(key)?.also { if (it) block(record.get(key)!!) }
    }

    inline fun <reified T> ifUpdatedMaybe(
        key: String,
        block: (recordValue: T?) -> Unit
    ) {
        ifUpdated(key) {
            if (it is T) block(it) else block(null)
        }
    }

    @JvmName("ifUpdatedT")
    inline fun <reified T> ifUpdated(key: String, block: (recordValue: T) -> Unit) =
        ifUpdatedMaybe<T>(key) {
            block(it!!)
        }

}