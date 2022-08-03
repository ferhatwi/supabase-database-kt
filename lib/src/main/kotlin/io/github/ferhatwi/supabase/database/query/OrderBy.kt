package io.github.ferhatwi.supabase.database.query

sealed class OrderBy {
    override fun toString() = when (this) {
        Ascending -> "asc"
        Descending -> "desc"
    }

    object Ascending : OrderBy()
    object Descending : OrderBy()
}
