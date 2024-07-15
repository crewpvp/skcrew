+++
archetype = "default"
title = "SQL"
weight = 13
+++
# SQL
Данный модуль позволяет подключаться к СУБД и совершать синхронные и асинхронные запросы.
{{% notice style="note" %}}
Специальная благодарность btk5h (Bryan Terce), FranKusmiruk, Govindas, TPGamesNL за поддержку аддона [Skript-db <i class="fas fa-link"></i>](https://forums.skunity.com/resources/skript-db-updated.1261/). Часть кода и идея была позаимствованны отсюда.
{{% /notice %}}

#### Подключение к СУБД
Инициализирует подключение к СУБД и возвращает это подключение. \
Возвращает объект типа **datasource** (com.zaxxer.hikari.HikariDataSource class). \
Вы так же можете опционально указать драйвер для взаимодействия с СУБД. \
Если драйвер не указан, аддон постарается выбрать драйвер из списка имеющихся (mysql, mssql, oraclesql, sqllite, postgres)
```vb
[the] data(base|[ ]source) [(of|at)] %string% [with [a] [max[imum]] [connection] life[ ]time of %timespan%] [(with|and) driver [class] [name] %-string%]
```

{{% expand title="Пример" %}}
```vb
set {_database} to database of "mysql://IP:3306/DATABASENAME?user=USERNAME&password=PASSWORD&useSSL=false"
```
{{% /expand %}}

#### Асинхронный запрос к СУБД
```vb
[async[hronously]] execute %string% [with (data|(param[eter][s])) %objects%] (in|on) %datasource% [and store [[the] (output|result)[s]] (to|in) [the] [var[iable]] %objects%]
```
**Первый параметр** - выполняемый запрос.\
**Второй параметр** - вставка значений для экранирования. Первый символ **?** будет заменен на первое значение из переданного списка во втором аргументе, второй **?** на второй из списка и т.д.
```vb
execute "select * from ? where name=?" with "books","worldatwar" in {_database}
```

Первый **?** будет заменен на books перед выполнением запроса, а второй на 'worldatwar'.
{{% notice style="warning" %}}
Используйте экранирование значений для любых запросов, где используется пользовательский ввод чтобы избежать SQL-инъекций
{{% /notice %}}
\
**Третий параметр** - подключение к СУБД, объект типа **datasource** из прошлого выражения.
\
**Последний параметр** - куда будет помещен результат запроса, необходимо указать переменную - список. После запроса названия столбцов станут индексами этого списка, значения столбов будут хранится в новом под-списке с названием столбца.
\
\
Например у нас есть следующая таблица:

| id (INT) | name (VARCHAR) | age (INT) |
|:--------:|:--------------:|:---------:|
|    1     |     Lotzy      |     22    |
|    2     |     w0w0       |     19    |

\
При выполнении кода:
```vb
execute "select * from table" in {_database} and store the result in {_output::*}
```

Список **{_output::\*}** будет иметь следующий вид:
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
Асинхронный запрос нельзя использовать в функциях, где возвращается значение. Результатом асинхронного запроса будет `<none>` 
{{% /notice %}}
#### Синхронный запрос к СУБД
```vb
sync[hronously] execute %string% [with (data|(param[eter][s])) %objects%] (in|on) %datasource% [and store [[the] (output|result)[s]] (to|in) [the] [var[iable]] %objects%]
```
{{% notice style="warning" %}}
Приостанавливает основной поток на время выполнения запроса, используйте с умом.
{{% /notice %}}