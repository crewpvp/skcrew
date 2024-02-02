+++
archetype = "default"
title = "SQL"
weight = 13
+++
# SQL
This module allows you to connect to the DBMS and make synchronous and asynchronous requests.
{{% notice style="note" %}}
Special thanks to the btk5h (Bryan Terce), FranKusmiruk, Govindas, TPGamesNL for the support of the addon [Skript-db <i class="fas fa-link"></i>](https://forums.skunity.com/resources/skript-db-updated.1261/). Part of the code and the idea was borrowed from here.
{{% /notice %}}

#### Connecting to a DBMS
Initializes the connection to the DBMS and returns this connection.
Returns an object of the type **datasource** (com.zaxxer.hikari.HikariDataSource class).
```vb
[the] data(base|[ ]source) [(of|at)] %string% [with [a] [max[imum]] [connection] life[ ]time of %timespan%]
```

{{% expand title="Пример" %}}
```vb
set {_database} to "mysql://IP:3306/DATABASENAME?user=USERNAME&password=PASSWORD&useSSL=false"
```
{{% /expand %}}

#### Asynchronous request to the DBMS
```vb
[async[hronously]] execute %string% [with (data|(param[eter][s])) %objects%] (in|on) %datasource% [and store [[the] (output|result)[s]] (to|in) [the] [var[iable]] %objects%]
```
**The first parameter** - is the query being executed.\
**The second parameter** - is the insertion of values for escaping. The first character is **?** will be replaced by the first value from the passed list in the second argument, the second **?** to the second from the list, etc. For example:\
```vb
execute "select * from ? where name=?" with "books","worldatwar" in {_database}
```

The first **?** will be replaced with books before executing the request, and the second one with 'worldatwar'.
{{% notice style="warning" %}}
Use value escaping for any queries where user input is used to avoid SQL-injection
{{% /notice %}}
\
**The third parameter** - connecting to a DBMS, an object of type **datasource** from a previous expression.
\
**The last parameter** - where the query result will be placed, you must specify the list variable. After the query, the column names will become indexes of this list, the column values will be stored in a new sub-list with the column name.
\
\
For example, we have the following table:

| id (INT) | name (VARCHAR) | age (INT) |
|:--------:|:--------------:|:---------:|
|    1     |     Lotzy      |     22    |
|    2     |     w0w0       |     19    |

\
When executing the code:
```vb
execute "select * from table" in {_database} and store the result in {_output::*}
```

The list **{_output::\*}** will look like this:
```vb
{_output::id::1} = 1  
{_output::name::1} = "Lotzy"  
{_output::age::1} = 22 
{_output::id::2} = 2  
{_output::name::2} = "w0w0"  
{_output::age::2} = 19
{_output::*} = "id", "name" and "age" 
```
{{% notice style="warning" %}}
An asynchronous request cannot be used in functions where a value is returned. The result of an asynchronous request will be `<none>` 
{{% /notice %}}
#### Synchronous request to the DBMS
```vb
sync[hronously] execute %string% [with (data|(param[eter][s])) %objects%] (in|on) %datasource% [and store [[the] (output|result)[s]] (to|in) [the] [var[iable]] %objects%]
```
{{% notice style="warning" %}}
Block the main thread for the duration of the request, use wisely.
{{% /notice %}}