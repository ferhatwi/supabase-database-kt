package io.github.ferhatwi.supabase.database

import io.github.ferhatwi.supabase.database.references.ListenableTableReference
import io.github.ferhatwi.supabase.database.request.Listenable


class Schema internal constructor(name: String, events: MutableList<Event>) :
    Listenable(name, null, events, null) {

    fun table(name: String): ListenableTableReference =
        ListenableTableReference(schema, name, events)
}