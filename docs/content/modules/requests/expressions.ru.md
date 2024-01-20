+++
archetype = "default"
title = "Expressions"
weight = 1
+++
# EXPRESSIONS
#### Заголовок запроса
Создает новый заголовок запроса в стиле **ключ : значение**
```vb
request (header|property) %string% %string%
```

#### Ключ заголовка запроса
```vb
%request property%'s key
```
```vb
key of %request property%
```

#### Значение заголовка запроса
```vb
%request property%'s value
```
```vb
value of %request property%
```