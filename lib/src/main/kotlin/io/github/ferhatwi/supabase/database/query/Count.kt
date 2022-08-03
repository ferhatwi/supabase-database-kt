package io.github.ferhatwi.supabase.database.query

sealed class Count {
    override fun toString() = when (this) {
        Exact -> "exact"
        Planned -> "planned"
        Estimated -> "estimated"
    }

    object Exact : Count()
    object Planned : Count()
    object Estimated : Count()
}


