package io.github.ferhatwi.supabase.database.references

import io.github.ferhatwi.supabase.database.request.SelectableQueryR

class RPCReference internal constructor(
    schema : String,
    function : String,
) : SelectableQueryR(schema, function)