+++
archetype = "default"
title = "Conditions"
weight = 2
+++
# CONDITIONS

#### Проверить, что сервер онлайн
```vb
%servers% is (online|connected)
```
```vb
%servers% is(n't| not) (online|connected)
```

#### Проверить, что игрок онлайн
Позволяет сменить сервер игрока, если он находится на сервере, который подключен к прокси серверу
```vb
%offlineplayers% is ([online ]on|connected to) proxy
```
```vb
%offlineplayers% (does|is)(n't| not) ([online ]on|connected to) proxy
```


