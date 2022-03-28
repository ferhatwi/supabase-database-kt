# Library for Supabase Database with Realtime

## Install

### With BOM

```groovy  
dependencies {  
 implementation platform("io.github.ferhatwi:supabase-kt-bom:0.1.2")
 implementation "io.github.ferhatwi:supabase-database-kt"
}  
```  

### Without BOM

#### NOTICE: BOM is strongly recommended to prevent conflicts.

```groovy 
dependencies {  
 implementation "io.github.ferhatwi:supabase-database-kt:0.3.2"
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

#### Call

###### Return

- String

#

###### Optional

- Schema
- Selection
- Range
- Count type
- Filters
- Orders
- Limit

###### Required

- Nothing

###### Disallowed

- Nothing

```kotlin
db
    .schema("SCHEMA_NAME")
    .rpc("FUNCTION_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .equalTo("COLUMN", VALUE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .call(DATA)
    .catch {

    }.collect {

    }
```

#### Get

###### Return

- Rows

#

###### Optional

- Schema
- Selection
- Range
- Count type
- Filters
- Orders
- Limit

###### Required

- Nothing

###### Disallowed

- Nothing

```kotlin
db
    .schema("SCHEMA_NAME")
    .table("TABLE_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .equalTo("COLUMN", VALUE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .get()
    .catch {

    }.collect {

    }
```

#### Delete

###### Return

- Rows

#

###### Optional

- Schema
- Selection
- Range
- Count type
- Filters
- Orders
- Limit

###### Required

- Nothing

###### Disallowed

- Nothing

```kotlin
db
    .schema("SCHEMA_NAME")
    .table("TABLE_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .equalTo("COLUMN", VALUE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .delete()
    .catch {

    }.collect {

    }
```

#### Update

###### Return

- Rows

#

###### Optional

- Schema
- Selection
- Range
- Count type
- Orders
- Limit

###### Required

- At least one filter

###### Disallowed

- Nothing

```kotlin
db
    .schema("SCHEMA_NAME")
    .table("TABLE_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .equalTo("COLUMN", VALUE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .update(DATA)
    .catch {

    }.collect {

    }
```

#### Insert

###### Return

- Rows

#

###### Optional

- Schema
- Selection
- Range
- Count type
- Orders
- Limit

###### Required

- Nothing

###### Disallowed

- Filters

```kotlin
db
    .schema("SCHEMA_NAME")
    .table("TABLE_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .insert(DATA)
    .catch {

    }.collect {

    }
```

#### Upsert

###### Return

- Rows

#

###### Optional

- Schema
- Selection
- Range
- Count type
- Orders
- Limit

###### Required

- Nothing

###### Disallowed

- Filters

```kotlin
db
    .schema("SCHEMA_NAME")
    .table("TABLE_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .upsert(DATA)
    .catch {

    }.collect {

    }
```

#### Listen

###### Return

- Row with event type

#

###### Optional

- Schema
- Equal To Filter
- On

###### Required

- Nothing

###### Disallowed

- Selection
- Range
- Count type
- Filters other than Equal To
- Orders
- Limit

```kotlin
val listener = db
    .schema("SCHEMA_NAME")
    .table("TABLE_NAME")
    .equalTo("COLUMN", VALUE)
    .on(EVENT_TYPE)
    .listen {

    }
```
##### To Cancel Listener
```kotlin
listener.cancel()
```

## Behaviors

#### Database

- Schema : Default value is ```"public"```.
- Selection : Specifying select with or without any columns returns data, otherwise doesn't. Can be
  specified before range.
- Range : Can be specified before count type.
- Count type : Can be specified before filters.
- Filters : Can be specified before orders.
- Orders : Can be specified before limit.

#### Realtime

- On : Not specifying on listens all type of events.

## Improvements and Bugs

Feel free to improve, upgrade, fix or report bugs!