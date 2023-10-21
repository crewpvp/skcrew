+++
archetype = "default"
title = "SECTIONS"
weight = 1
+++

#### Создание нового GUI
Создает новое GUI и помещает его в результат выражения (last created gui)[./expressions#последнее-созданное-gui]
```vb
create [a] [new] gui with %inventory% [(and|with) ([re]move[e]able|stealable) items] [(and|with) shape %strings%]
```
Пример:
```vb
create a gui with chest inventory with 3 rows named "My GUI":
  #do some stuff
```

Последний аргумент позволяет использовать заготовленные шаблоны расположения интерактивных элементов. 
Например, если мы открываем инвентарь воронки, то можем указать шаблон "xxixx", и тогда, при создании элемента "x" первый, второй, четвертый и пятый слоты будут с этим элементом.
#### Изменить уже созданное GUI
Позволяет переопределить интерактивные элементы внутри уже созданного GUI
```vb
(change|edit) [gui] %gui%
```

#### Создать интерактивный элемент
При клике на этот элемент будет выполнен код внутри секции.
```vb
(make|format) [the] next gui [slot] (with|to) [([re]mov[e]able|stealable)] %itemtype%
```
Создает интерактивный элемент на следующем пустом слоте инвентаря.
```vb
(make|format) gui [slot[s]] %strings/numbers% (with|to) [([re]mov[e]able|stealable)] %itemtype%
```
Второе выражение позволяет указать значение из шаблона или номер слота для создания элемента.
\
Пример:
```vb
create a gui with chest inventory with 3 rows named "My GUI":
  make gui slot 1 with stone named "Click for hello world!":
    broadcast "Hello world!"
```

#### Удалить интерактивный элемент
```vb
(un(make|format)|remove) [the] next gui [slot]
```
```vb
(un(make|format)|remove) gui [slot[s]] %strings/numbers%
```
```vb
(un(make|format)|remove) all [[of] the] gui [slots]
```

#### При открытии GUI
Код внутри данной секции будет выполнен после открытия GUI игроку.
```vb
run (when|while) open[ing] [[the] gui]
```
```vb
run (when|while) [the] gui opens
```
```vb
run on gui open[ing]
```

#### При закрытии GUI
Код внутри данной секции будет выполнен после закрытия GUI игроком.
```vb
run (when|while) clos(e|ing) [[the] gui]
```
```vb
run (when|while) [the] gui closes
```
```vb
run on gui clos(e|ing)
```