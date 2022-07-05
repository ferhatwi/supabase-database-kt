package io.github.ferhatwi.supabase.database

sealed class TextSearchType {
    override fun toString() = when (this) {
        is Plain -> "pl"
        is Phrase -> "ph"
        is Website -> "w"
        is None -> ""
    }


    object Plain : TextSearchType()
    object Phrase : TextSearchType()
    object Website : TextSearchType()
    object None : TextSearchType()
}