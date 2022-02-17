package io.github.ferhatwi.supabase.database

sealed class Event {
    override fun toString(): String {
        return when (this) {
            Update -> "UPDATE"
            Insert -> "INSERT"
            Delete -> "DELETE"
        }
    }

    object Insert : Event()
    object Update : Event()
    object Delete : Event()
}
