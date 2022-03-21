package io.github.ferhatwi.supabase.database.snapshots

class TableSnapshot internal constructor(val rows: List<RowSnapshot>) {
    override fun toString() = rows.toString()
}