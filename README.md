# Library for Supabase Database with Realtime
## Install
### With BOM
```groovy  
dependencies {  
 implementation platform("io.github.ferhatwi:supabase-kt-bom:0.1.1")
 implementation "io.github.ferhatwi:supabase-database-kt"
}  
```  
### Without BOM
#### NOTICE: BOM is strongly recommended to prevent conflicts.
```groovy 
dependencies {  
 implementation "io.github.ferhatwi:supabase-database-kt:0.3.1"
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
    .rpc("FUNCTION_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .equalTo("COLUMN", VALUE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .call(
        DATA,
        onFailure = {

        }, onSuccess = {

        }
    )
```
#### Get
###### Return
- Rows
#
###### Optional
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
    .table("TABLE_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .equalTo("COLUMN", VALUE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .get(
        onFailure = {

        }, onSuccess = {

        }
    )
```
#### Delete
###### Return
- Rows
#
###### Optional
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
    .table("TABLE_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .equalTo("COLUMN", VALUE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .delete(
        onFailure = {

        }, onSuccess = {

        }
    )
```
#### Update
###### Return
- Rows
#
###### Optional
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
    .table("TABLE_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .equalTo("COLUMN", VALUE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .update(
        DATA,
        onFailure = {

        }, onSuccess = {

        }
    )
```
#### Insert
###### Return
- Rows
#
###### Optional
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
    .table("TABLE_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .insert(
        DATA,
        onFailure = {

        }, onSuccess = {

        }
    )
```
#### Upsert
###### Return
- Rows
#
###### Optional
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
    .table("TABLE_NAME")
    .select("COLUMN1", "COLUMN2")
    .range(FROM, TO)
    .countType(COUNT_TYPE)
    .order("COLUMN", OrderBy, NULLS_WHEN)
    .limit(VALUE)
    .upsert(
        DATA,
        onFailure = {

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
- Schema : If specified, only listening is allowed.
- Selection : Not specifying any column results in selecting all columns. Can be specified before range.
- Range : Can be specified before count type.
- Count type : Can be specified before filters.
- Filters : Can be specified before orders.
- Orders : Can be specified before limit.
#### Return
- All requests return data.
## Improvements and Bugs
Feel free to improve, upgrade, fix or report bugs!
