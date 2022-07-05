package io.github.ferhatwi.supabase.database.snapshots

data class TableSnapshot internal constructor(val rows: List<RowSnapshot>) {
    fun single() = rows[0]
    fun maybeSingle() = if (rows.isEmpty()) null else single()
}