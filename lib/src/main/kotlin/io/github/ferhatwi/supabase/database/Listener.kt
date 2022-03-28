package io.github.ferhatwi.supabase.database

import kotlinx.coroutines.Job

class Listener internal constructor(private val job: Job) {
    fun cancel() = job.cancel()
}