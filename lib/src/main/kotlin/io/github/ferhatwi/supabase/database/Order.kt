package io.github.ferhatwi.supabase.database

data class Order(val column: String, val orderBy: OrderBy, val nullsFirst: Boolean) {
    override fun toString() =
        "order=$column.$orderBy.${if (nullsFirst) "nullfirst" else "nullslast"}"
}
