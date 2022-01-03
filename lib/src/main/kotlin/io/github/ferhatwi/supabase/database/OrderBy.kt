package io.github.ferhatwi.supabase.database

sealed class OrderBy {
    object Ascending : OrderBy()
    object Descending : OrderBy()
}
