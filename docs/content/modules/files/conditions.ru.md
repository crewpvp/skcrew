+++
archetype = "default"
title = "Conditions"
weight = 2
+++
# CONDITIONS
#### Проверка файла на его фактическое наличие
```vb
%path% (is|does)[(n't| not)] exist[s]
```
{{% expand title="Пример" %}}
```vb
if file "eula.txt" is exists:
  broadcast "Yea, file eula.txt is exists"
```
{{% /expand %}}
#### Проверка что файл является директорией
```vb
%path% is[(n't| not)] dir[ectory]
```
{{% expand title="Пример" %}}
```vb
if file "eula.txt" is not directory:
  broadcast "Yea, file eula.txt is not directory, it is regular file"
```
{{% /expand %}}
#### Проверка что файл является файлом
```vb
%path% is[(n't| not)] file
```
{{% expand title="Пример" %}}
```vb
if file "eula.txt" is file:
  broadcast "Yea, file eula.txt is regular file"
```
{{% /expand %}}