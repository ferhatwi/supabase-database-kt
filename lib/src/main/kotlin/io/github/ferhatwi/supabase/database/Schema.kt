package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.database.references.RPCReference
import io.github.ferhatwi.supabase.database.references.TableReference
import io.github.ferhatwi.supabase.database.request.Listenable


open class Schema internal constructor(private val name: String) :
    Listenable(name, null, mutableListOf(), null) {

    fun table(name: String) = TableReference(this.name, name)

    fun rpc(name: String) = RPCReference(this.name, name)
}