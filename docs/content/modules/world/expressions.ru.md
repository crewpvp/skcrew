+++
archetype = "default"
title = "Expressions"
weight = 2
+++
# EXPRESSIONS
#### Получить главный мир сервера
Возвращает главный мир сервера, без которого работа сервера невозможна.
```vb
main[ ]world
```

#### Получить чанк по его координатам
В данном выражении используются не координаты блоков, а координаты чанка.\
Чтобы получить координаты чанка, координата X и Z делятся на 16.
```vb
chunk [at] %number%[ ],[ ]%number% (in|at) %world%
```

#### Получить всех сущностей внутри чанка
```vb
entit(y|ies) (of|in) %chunk%
```
```vb
%chunk%'s entit(y|ies)
```

#### Получить все загруженные чанки в мире
```vb
[all [[of] the]] loaded chunks (in|at|of) %world%
```
```vb
%world%'s [all [[of] the]] loaded chunks
```

#### Постоянная прогрузка чанка
{{% notice style="warning" %}}
Не поддерживается на версиях ниже чем 1.13.1
{{% /notice %}}
Данное выражение поддерживает изменение состояния
```vb
force load[ed] of %chunk%
```
```vb
%chunk%'s force load[ed]
```

#### Центр барьера мира
Данное выражение поддерживает изменение состояния
```vb
[world] border center of %world%
```
```vb
%world%'s [world] border center
```

#### Размер барьера мира
Данное выражение поддерживает изменение состояния.
При изменении возможно указать время в тиках, за сколько барьер должен изменить свой размер
```vb
[world[ ]]border size of %world% [for %-timespan%]
```
```vb
%world%'s [world[ ]]border size [for %-timespan%]
```
```vb
[the] size of %world%'s [world[ ]]border [for %-timespan%]
```