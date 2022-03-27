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
Supabase should be initialized before using this library. Instructions are [here](https://github.com/ferhatwi/supabase-kt).
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
    .call(
        DATA,
        onFailure = { message, code, statusCode ->

        }, onSuccess = {

        }
    )
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
    .get(
        onFailure = { message, code, statusCode ->

        }, onSuccess = {

        }
    )
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
    .delete(
        onFailure = { message, code, statusCode ->

        }, onSuccess = {

        }
    )
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
    .update(
        DATA,
        onFailure = { message, code, statusCode ->

        }, onSuccess = {

        }
    )
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
    .insert(
        DATA,
        onFailure = { message, code, statusCode ->

        }, onSuccess = {

        }
    )
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
    .upsert(
        DATA,
        onFailure = { message, code, statusCode ->

        }, onSuccess = {

        }
    )
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
- Nothing
```kotlin
db
    .schema("SCHEMA_NAME")
    .table("TABLE_NAME")
    .equalTo("COLUMN", VALUE)
    .on(EVENT_TYPE)
    .listen {

    }
```
## Behaviors
#### Request
- Schema : Default value is ```"public"```.
- Selection : Specifying select with our without any columns returns data, otherwise doesn't. Can be specified before range.
- Range : Can be specified before count type.
- Count type : Can be specified before filters.
- Filters : Can be specified before orders.
- Orders : Can be specified before limit.
## Improvements and Bugs
Feel free to improve, upgrade, fix or report bugs!
