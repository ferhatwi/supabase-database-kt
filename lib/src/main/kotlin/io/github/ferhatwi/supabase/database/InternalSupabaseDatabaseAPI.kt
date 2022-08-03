package io.github.ferhatwi.supabase.database

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is internal in SupabaseDatabase and should not be used. It could be removed or changed without notice."
)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY,
)
annotation class InternalSupabaseDatabaseAPI