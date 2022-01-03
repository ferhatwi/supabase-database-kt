package io.github.ferhatwi.supabase.database

sealed class TextConfig(val config: String?) {
    class Plain(config: String? = null) : TextConfig(config)
    class Phrase(config: String? = null) : TextConfig(config)
    class Website(config: String? = null) : TextConfig(config)
    class None(config: String? = null) : TextConfig(config)
}