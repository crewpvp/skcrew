+++
archetype = "default"
title = "Expressions"
weight = 1
+++
# EXPRESSIONS
#### Получить ссылку на абстрактный файл
По сути, вы получаете либо реальный файл, если он есть, либо абстрактный файл, который потом можно создать
```vb
[the] (file[s]|dir[ector(y|ies)]) %strings%
```
{{% expand title="Пример" %}}
```vb
set {_file} to file "eula.txt"
```
{{% /expand %}}
#### Получить абсолютный файл
Абсолютный - то есть тот, у которого путь начинается с корня файловой системы
```vb
absolute [(file|dir[ectory])] of %path%
```
```vb
%path%'s absolute [(file|dir[ectory])] 
```
{{% expand title="Пример" %}}
```vb
set {_file} to absolute file of file "eula.txt"
```
{{% /expand %}}
#### Получить все файлы внутри директории
Позволяет так же делать это рекурсивно, получая файлы в директории и во всех внутренних директориях
{{% expand title="Паттерны" %}}
```vb
all [the] files and [all] [the] dir[ectorie]s (in|of|from) %path%
```
```vb
all [the] files (in|of|from) %path%
```
```vb
all [the] dir[ectorie]s (in|of|from) %path%
```
```vb
all [the] sub[(-| )]files and [all] [the] sub[(-| )]dir[ectorie]s (in|of|from) %path%
```
```vb
all [the] sub[(-| )]dir[ectorie]s (in|of|from) %path%
```
```vb
all [the] sub[(-| )]files (in|of|from) %path%
```
```vb
glob (files|dir[ectorie]s) %string% (in|of|from) %path%
```
{{% /expand %}}
{{% expand title="Пример" %}}
```vb
loop all files from file "plugins/":
  broadcast "%loop-value%"
```
{{% /expand %}}
#### Получить текстовое содержимое файла
```vb
[the] content of %path%
```
```vb
[the] %path%'s content
```
{{% expand title="Пример" %}}
```vb
set {_file} to content of file "eula.txt"
replace all "false" with "true" in {_file}
set content of file "eula.txt" to {_file}
```
{{% /expand %}}
#### Получить название файла или директории
```vb
[the] (file|dir[ectory])name of %path%
```
```vb
[the] %path%'s (file|dir[ectory])name
```
{{% expand title="Пример" %}}
```vb
loop all files from file "plugins/":
  broadcast filename of loop-value
```
{{% /expand %}}
#### Прочитать строку/строки файла или изменить их
```vb
[the] line %number% (from|of|in) %path%
```
```vb
[all] [the] lines (from|of|in) %path%
```
{{% expand title="Пример" %}}
```vb
set line 1 of file "eula.txt" to "eula=true"
```
{{% /expand %}}
#### Получить директорию в которой находится файл/папка
```vb
parent [(file|dir[ectory])] of %path%
```
```vb
%path%'s parent [(file|dir[ectory])]
```
{{% expand title="Пример" %}}
```vb
set {_file} to parent of file "eula.txt"
```
{{% /expand %}}

#### Получить размер файла в байтах
```vb
file size of %path%
```
```vb
%path%'s file size
```

#### Получить дату последнего доступа к файлу
```vb
[last] access (date|time) of %path%
```
```vb
%path%'s [last] access (date|time)
```

#### Получить дату последнего изменения файла
```vb
[last] modified (date|time) of %path%
```
```vb
%path%'s [last] modified (date|time)
```

#### Получить дату создания файла
```vb
create[d] (date|time) of %path%
```
```vb
%path%'s create[d] (date|time)
```
