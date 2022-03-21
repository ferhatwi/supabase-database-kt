package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.Supabase
import io.github.ferhatwi.supabase.database.references.RPCReference
import io.github.ferhatwi.supabase.database.references.TableReference
import io.github.ferhatwi.supabase.database.request.Listenable
import io.github.ferhatwi.supabase.database.request.listen
import io.github.ferhatwi.supabase.database.request.topic
import io.github.ferhatwi.supabase.database.snapshots.ListenSnapshot

class SupabaseDatabase : Listenable(null, null, mutableListOf(), null) {

    companion object {
        fun getInstance() = SupabaseDatabase()
    }

    fun schema(name: String) = Schema(name, events)

    fun table(name: String) = TableReference(name)

    fun rpc(name: String) = RPCReference(name)

    suspend fun listen(vararg listenable: Listenable, onSuccess: (ListenSnapshot) -> Unit) =
        listen(
            topic = listenable.map { it.topic() }.toTypedArray(),
            events = events,
            onSuccess = onSuccess
        )

}

fun Supabase.database() = SupabaseDatabase.getInstance()

