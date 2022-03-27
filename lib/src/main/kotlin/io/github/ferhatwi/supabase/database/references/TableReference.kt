package io.github.ferhatwi.supabase.database.references

import io.github.ferhatwi.supabase.database.request.SelectableQuery

class TableReference internal constructor(
    schema : String,
    name: String,
) : SelectableQuery(schema, name)