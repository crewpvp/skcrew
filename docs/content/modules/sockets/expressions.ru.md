+++
archetype = "default"
title = "Expressions"
weight = 1
+++
# EXPRESSIONS

#### Получить текущий сервер
Вовзращает текущий игровой сервер
```vb
(current|this) server
```

#### Получить сервер по названию
Возвращает сервер по названию, под которым он записан в конфигурации прокси сервера.\
Если данный сервер не числится в конфигурации прокси сервера - будет возвращено `<none>`
```vb
server %string%
```

#### Получить название сервера
```vb
server[ ]name of %server%
```
```vb
%server%'s server[ ]name
```

#### Получить список серверов
Выражение ниже возвращает список всех серверов описанных в конфигурации прокси сервера.
```vb
[all] servers
```
Получить только те сервера, которые включены. Чтобы сервер был определен как включенный - он должен быть подключен к сокет серверу.
```vb
[all] online servers
```

#### Получить игроков с сервера
Возвращает список игроков в виде сетевых игроков.
```vb
players (from|of|on) %servers%
```
```vb
%servers%'s players
```

#### Получить игрока на прокси сервере
Принимает ник игрока или его UUID , возвращает сетевого игрока, или `<none>`, если игрока нет онлайн
```vb
network[ ]player %string%
```

{{% notice style="note" %}}
Серверный игрок автоматически конвертируется в `OfflinePlayer`, если используется в каких-либо выражениях при необходимости
{{% /notice %}}

#### Получить сервер игрока
```vb
server of %networkplayer%
```
```vb
%networkplayer%'s server
```




