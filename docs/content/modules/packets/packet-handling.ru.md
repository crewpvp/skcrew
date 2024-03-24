+++
archetype = "default"
title = "Отслеживание пакетов"
weight = 2
+++
# ОТСЛЕЖИВАНИЕ ПАКЕТОВ
#### Событие получения или отправки пакета.
Данное событие позволяет получить пакет, отправляемый игроком или отпрвляемый сервером игроку.
```vb
on packet:
```
```
on packet <packet name>:
```
`<packet name>` - позволяет указать название пакета для отслеживания.\
\
Событие может быть отменено. В этом случае пакет не будет получен сервером, или отправлен игроку\
\
Данное событие имеет встроенные выражения.
- `event-packet` - для получения пакета
- `event-player` - для получения игрока

{{% notice style="note" %}}
`event-packet` может быть заменен при помощи `set event-packet to %packet%`, при условии, что не использовалась задержка выполнения 
{{% /notice %}}


{{% expand title="Пример" %}}
```vb
on packet PacketPlayOutOpenSignEditor:
  broadcast "%event-player%"
  broadcast "%event-packet%"
  cancel event
```
В данном примере происходит вывод ника игрока и названия пакета в чат. А после отмена отправки этого события игроку.\
Сам пакет - заставляет игрока открыть окно редактирования текста таблички.
{{% /expand %}}

#### Получение буфера из пакета
После получения буфера из пакета мы можем читать поля указанные на [wiki.vg <i class="fas fa-link"></i>](https://wiki.vg/Protocol_version_numbers#Release)
```vb
buffer (of|from) %packet%
```
```vb
%packet%'s buffer
```

#### Чтение буфера
Для получения значений из буфера существуют следующие выражения, аналогичные [записи в буфер <i class="fas fa-link"></i>](./packet-creation/#заполнение-буфера-данными)
```vb
read bool[ean] from %bytebuf%
read uuid from %bytebuf%
read string from %bytebuf%
read position from %bytebuf%
read [unsigned] byte from %bytebuf%
read [unsigned] short from %bytebuf%
read float from %bytebuf%
read double from %bytebuf%
read int[eger] from %bytebuf%
read long from %bytebuf%
read angle from %bytebuf%
read var[iable][ ]int[eger] from %bytebuf%
read var[iable][ ]long from %bytebuf%
read utf[(-| )]8 [with [len[gth]]] %number% from %bytebuf%
```
{{% notice style="note" %}}
Для чтения utf-8 необходимо так же указать длину текста в байтах
{{% /notice %}}
Чтение происходит в том же порядке, что и запись пакета, при условии, что этот пакет не был создан вами. Если же вы создали пакет, и хотите его прочитать (не известно по какой причине), то сместите его reader index в позицию нуля.\
Рассмотрим пакет [PacketPlayOutOpenSignEditor <i class="fas fa-link"></i>](https://wiki.vg/Protocol#Open_Sign_Editor)
| Packet ID          |   State   |   Bound To   |   Field Name   |     Field Type    | Notes      |
|:------------------:|:---------:|:------------:|:--------------:|:-----------------:|:-----------|
|       0x32         |   Play    |    Client    |    Location    |     Position      |            |
|                    |           |              | Is Front Text  |     Boolean       | Whether the opened editor is for the front or on the back of the sign |

```vb
on packet PacketPlayOutOpenSignEditor:
  set {_buffer} to buffer from event-packet
  set {_position} to read position from {_buffer}
  set {_isFrontText} to read boolean from {_buffer}
  broadcast "%{_position}%"
  cancel event
```

Код выше выведет в чат Vector с координатами открытой таблички.

#### Reader index 
Каждое чтение буфера сдвигает его `Reader index`, это количество прочитанных байт внутри буфера.\
`Reader index` можно узнать, или изменить при помощи следующего выражеения:
```vb
reader index of %bytebuf%
```
```vb
%bytebuf%'s reader index
```

#### Отключение или включение отслеживания пакетов
Для любого игрока можно включить или отключить отслеживание входящих, или исходящих пакетов.
```vb
[(handl(e|ing))|(listen[ing] [of])] (in|out)coming packets of %player%
```
```vb
%player%'s [(handl(e|ing))|(listen[ing] [of])] (in|out)coming packets
```

{{% expand title="Пример" %}}
```vb
on join:
  set listening incoming packet of player to false
```
{{% /expand %}}
