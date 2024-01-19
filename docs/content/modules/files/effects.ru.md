+++
archetype = "default"
title = "Effects"
weight = 3
+++
# EFFECTS
#### Скопировать файл или директорию
Позволяет так же указать параметр для перезаписи, если файл уже существует
```vb
copy %path% to %path% [with (replac|overwrit)(e|ing)]
```
{{% expand title="Пример" %}}
```vb
copy file "plugins/Skcrew.jar" to file "./"
```
{{% /expand %}}
#### Создать простой файл
Позволяет так же указать текстовое содержимое создаваемого файла
```vb
create %paths% [with [(text|string|content)] %strings%]
```
{{% expand title="Пример" %}}
```vb
create file "eula.txt" with content "eula=true"
```
{{% /expand %}}
#### Удалить файл или директорию
```vb
delete %paths%
```
{{% expand title="Пример" %}}
```vb
delete file "plugins/Skcrew.jar"
```
{{% /expand %}}
#### Переместить файл или директорию
Позволяет так же указать параметр для перезаписи, если файл уже существует
```vb
move %paths% to %path% [with (overwrit|replac)(e|ing)]
```
{{% expand title="Пример" %}}
```vb
move file "plugins" to file "disabled_plugins"
```
{{% /expand %}}
#### Переименовать файл или директорию
Позволяет так же указать параметр для перезаписи, если файл уже существует
```vb
rename %paths% to %string% [with (overwrit|replac)(e|ing)]
```
{{% expand title="Пример" %}}
```vb
rename file "plugins/Skript/scripts/mycoolscript.sk" to "-mycoolscript.sk"
```
{{% /expand %}}
#### Распаковать zip архив
Позволяет так же указать параметр для перезаписи, если файл уже существует
```vb
unzip %path% to %path% [with (overwrit|replac)(e|ing)]
```
{{% expand title="Пример" %}}
```vb
unzip file "myarchive.zip" to file "./"
```
{{% /expand %}}
#### Создать zip архив
Позволяет так же указать параметр для перезаписи, если файл уже существует
```vb
zip %paths% to %path% [with (overwrit|replac)(e|ing)]
```
{{% expand title="Пример" %}}
```vb
zip file "world/" to file "backupworld.zip"
```
{{% /expand %}}

