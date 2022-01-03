package io.github.ferhatwi.supabase.database

sealed class Interval {
    object Closed : Interval()
    object Open : Interval()
}

data class Range(
    val leftInterval: Interval,
    val from: Int,
    val to: Int,
    val rightInterval: Interval
)