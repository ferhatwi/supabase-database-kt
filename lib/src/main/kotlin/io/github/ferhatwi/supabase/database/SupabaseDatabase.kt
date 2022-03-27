package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase
import io.github.ferhatwi.supabase.database.request.Listenable
import io.github.ferhatwi.supabase.database.request.listen
import io.github.ferhatwi.supabase.database.request.topic
import io.github.ferhatwi.supabase.database.snapshots.ListenSnapshot

class SupabaseDatabase : Schema("public") {

    companion object {
        fun getInstance() = SupabaseDatabase()
    }

    fun schema(name: String) = Schema(name)

    suspend fun listen(vararg listenable: Listenable, onSuccess: (ListenSnapshot) -> Unit) =
        listen(
            topic = listenable.map { it.topic() }.toTypedArray(),
            events = events,
            onSuccess = onSuccess
        )

}

fun Supabase.database() = SupabaseDatabase.getInstance()

