package io.github.ferhatwi.supabase.database.query

data class Order(val column: String, val orderBy: OrderBy = OrderBy.Ascending, val nullsFirst: Boolean = false) {
    override fun toString() =
        "order=$column.$orderBy.${if (nullsFirst) "nullfirst" else "nullslast"}"
}
