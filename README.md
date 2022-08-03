# Library for Supabase Database with Realtime

## Install

### With BOM

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ferhatwi/supabase-kt-bom.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.ferhatwi%22%20AND%20a:%22supabase-kt-bom%22)

```groovy  
dependencies {  
 implementation platform("io.github.ferhatwi:supabase-kt-bom:{BOM_VERSION}")
 implementation "io.github.ferhatwi:supabase-database-kt"
}  
```  

### Without BOM

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ferhatwi/supabase-database-kt.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.ferhatwi%22%20AND%20a:%22supabase-database-kt%22)

#### NOTICE: BOM is strongly recommended to prevent conflicts.

```groovy 
dependencies {  
 implementation "io.github.ferhatwi:supabase-database-kt:{DATABASE_VERSION}"
}  
```  

## How to use?

### REMINDER

Supabase should be initialized before using this library. Instructions
are [here](https://github.com/ferhatwi/supabase-kt).

#

```kotlin  
val db = Supabase.database()  
```  

#### RPC

```kotlin
db.rpc.request<List<SomeDataClass>>(
    schema = SCHEMA,
    function = FUNCTION,
    selections = listOf(SELECTION, SELECTION2),
    count = COUNT,
    filters = listOf(FILTER, FILTER2),
    orders = listOf(ORDER, ORDER2),
    range = FROM to TO,
    data = DATA
).catch {

}.collect {

}
```

#### Get

```kotlin
db.get.request<List<SomeDataClass>>(
    schema = SCHEMA,
    table = FUNCTION,
    selections = listOf(SELECTION, SELECTION2),
    count = COUNT,
    filters = listOf(FILTER, FILTER2),
    orders = listOf(ORDER, ORDER2),
    range = FROM to TO
).catch {

}.collect {

}
```

#### Delete

```kotlin
db.delete.request<List<SomeDataClass>>(
    schema = SCHEMA,
    table = FUNCTION,
    selections = listOf(SELECTION, SELECTION2),
    count = COUNT,
    filters = listOf(FILTER, FILTER2),
    orders = listOf(ORDER, ORDER2),
    range = FROM to TO
).catch {

}.collect {

}
```

#### Update

```kotlin
db.update.request<List<SomeDataClass>>(
    schema = SCHEMA,
    table = FUNCTION,
    selections = listOf(SELECTION, SELECTION2),
    count = COUNT,
    filters = listOf(FILTER, FILTER2),
    orders = listOf(ORDER, ORDER2),
    range = FROM to TO,
    data = DATA,
).catch {

}.collect {

}
```

#### Insert

```kotlin
db.insert.request<List<SomeDataClass>>(
    schema = SCHEMA,
    table = FUNCTION,
    selections = listOf(SELECTION, SELECTION2),
    count = COUNT,
    orders = listOf(ORDER, ORDER2),
    range = FROM to TO,
    data = DATA
).catch {

}.collect {

}
```

#### Upsert

```kotlin
db.upsert.request<List<SomeDataClass>>(
    schema = SCHEMA,
    table = FUNCTION,
    selections = listOf(SELECTION, SELECTION2),
    count = COUNT,
    orders = listOf(ORDER, ORDER2),
    range = FROM to TO,
    data = DATA
).catch {

}.collect {

}
```

#### Listen

```kotlin
db.listen.request<List<SomeDataClass>>(
    schema = SCHEMA,
    table = FUNCTION,
    events = listOf(Event.INSERT, Event.UPDATE, Event.DELETE),
    filter = Filter.EqualTo(COLUMN, VALUE)
).cancellable()
    .catch {

    }
    .collect {

    }
```

## Improvements and Bugs

Feel free to improve, upgrade, fix or report bugs!