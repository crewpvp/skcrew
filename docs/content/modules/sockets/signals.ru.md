+++
archetype = "default"
title = "Signals"
weight = 4
+++
# SIGNALS
Сигналы позволяют передавать необходимую вам информацию между серверами.
#### Создать сигнал
Сигнал имеет вид `ключ : значения`, позволяет передать любые данные, которые могут быть сериализованны.
```vb
signal [(with key|keyed)|(with name|named)] %string% (and|with) [data] %objects%
```
{{% expand title="Пример" %}}
```vb
set {_signal} to signal named "broadcast" with data "Hello world!","My name is Bjork."
```
{{% /expand %}}

#### Получить ключ сигнала.
```vb
(key|signal name) of %signal%
```
```vb
%signal%'s (signal name|key)
```

#### Получить данные сигнала
Возвращает список объектов
```vb
data of %signal%
```
```vb
%signal%'s data
```

#### Отправить сигнал
Позволяет отправить сигнал на какой-либо подключенный сервер
```vb
send signal %signals% to %servers%
```
{{% expand title="Пример" %}}
```vb
set {_signal} to signal named "broadcast" with data "Hello world!","My name is Bjork."
send signal {_signal} to all servers
```
{{% /expand %}}

#### Событие получения сигнала
Позволяет так же отслеживать сигналы по ключу.
```vb
signal [(with key|keyed) %string%]
```
Событие имеет встроенные данные, при помощи `event-signal` можно получить пришедший сигнал.
{{% expand title="Пример" %}}
```vb
on load:
  set {_signal} to signal named "broadcast" with data "Hello world!","My name is Bjork."
  send signal {_signal} to all servers
  
on signal with key "broadcast":
  send data of event-signal to all players, console
```
{{% /expand %}}
