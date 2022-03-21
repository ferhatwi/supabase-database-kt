package io.github.ferhatwi.supabase.database.snapshots

import io.github.ferhatwi.supabase.database.Event

class ListenSnapshot internal constructor(val row: RowSnapshot, val event: Event) {
    override fun toString() = listOf(row, event.toString()).toString()
}