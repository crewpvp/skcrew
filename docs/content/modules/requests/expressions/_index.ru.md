+++
archetype = "default"
title = "EXPRESSIONS"
weight = 1
+++

#### Заголовок запроса
Создает новый заголовок запроса в стиле **ключ:значение** (com.lotzy.skcrew.spigot.requests.RequestProperty class)
```vb
request (header|property) %string% %string%
```

#### Ключ заголовка запроса
```vb
%request property%'s key
key of %request property%
```

#### Значение заголовка запроса
```vb
%request property%'s value
value of %request property%
```