package io.github.ferhatwi.supabase.database

sealed class Modifier {
    class Limit(val count: Int) : Modifier()
    class Order(val column : String, val orderBy: OrderBy, val nullsFirst : Boolean) : Modifier()
}
