package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase
import io.github.ferhatwi.supabase.database.request.Listenable
import io.github.ferhatwi.supabase.database.request.listen
import io.github.ferhatwi.supabase.database.request.topic
import io.github.ferhatwi.supabase.database.snapshots.ListenSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect

class SupabaseDatabase : Schema("public") {

    companion object {
        fun getInstance() = SupabaseDatabase()
    }

    fun schema(name: String) = Schema(name)

    suspend fun listen(
        vararg listenable: Listenable,
        action: suspend (value: ListenSnapshot) -> Unit
    ) =
        Listener(CoroutineScope(Dispatchers.IO).async {
            listen(
                topic = listenable.map { it.topic() }.toTypedArray(),
                events = events
            ).cancellable().collect(action)
        }.apply { await() })
}

fun Supabase.database() = SupabaseDatabase.getInstance()

