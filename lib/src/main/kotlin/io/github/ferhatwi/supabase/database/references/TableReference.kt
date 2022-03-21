package io.github.ferhatwi.supabase.database.references

import io.github.ferhatwi.supabase.database.request.SelectableQuery

class TableReference internal constructor(
    name: String,
) : SelectableQuery(name)