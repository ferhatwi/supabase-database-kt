package io.github.ferhatwi.supabase.database.query

sealed class Interval {

    object Closed : Interval()
    object Open : Interval()
}

data class Range(
    val leftInterval: Interval,
    val from: Int,
    val to: Int,
    val rightInterval: Interval
) {
    override fun toString() = "${
        when (leftInterval) {
            Interval.Closed -> "["
            Interval.Open -> "("
        }
    }$from,$to${
        when (rightInterval) {
            Interval.Closed -> "]"
            Interval.Open -> ")"
        }
    }"
}