package io.github.ferhatwi.supabase.database

sealed class TextConfig(val text: String?) {
    override fun toString() = when (this) {
        is Plain -> "p1"
        is Phrase -> "ph"
        is Website -> "w1"
        is None -> ""
    }


    class Plain(text: String? = null) : TextConfig(text)
    class Phrase(text: String? = null) : TextConfig(text)
    class Website(text: String? = null) : TextConfig(text)
    class None(text: String? = null) : TextConfig(text)
}