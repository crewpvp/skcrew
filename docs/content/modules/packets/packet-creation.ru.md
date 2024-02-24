+++
archetype = "default"
title = "Создание пакета"
weight = 1
+++
# Создание пакета
#### Изучение wiki.vg
[Данный сайт <i class="fas fa-link"></i>](https://wiki.vg/Protocol_version_numbers) содержит в себе описание пакетов, использующихся в серверной части Minecraft.\
Выбираем на сайте версию ядра, на которой стоит сервер и переходим по ссылке этой версии.\
Для начала требуется найти пакет, который нам необходим. Все пакеты, находящиеся в разделе `SERVERBOUND` - отправляются игроком - серверу, пакеты в разделе `CLIENTBOUND` - от сервера к игроку.
Нас, очевидно интересует раздел пакетов `CLIENTBOUND`.\
Выберем пакет [**SET CAMERA** <i class="fas fa-link"></i>](https://wiki.vg/Protocol#Set_Camera), так как он не изменялся с версии 1.8.\
Данный пакет позволяет задать игроку камеру, аналогичную камере, которая происходит при клике на сущность в режиме наблюдателя.\
Перед нами предстает следующая таблица на сайте:
| Packet ID          |   State   |   Bound To   |   Field Name   |     Field Type    | Notes      |
|:------------------:|:---------:|:------------:|:--------------:|:-----------------:|:-----------|
|       0x50	     |   Play    |    Client    |	Camera ID    |	    VarInt       |ID of the entity to set the client's camera to. |

Данная таблица содержит в себе номер пакета, момент игрового процесса, когда он может быть получен, к чему привязан пакет, и описание его содержимого.\
Первым делом нам необходимо выяснить, как этот пакет называется на сервере. Для этого нам понадобится три первых поля:
- id
- state
- bound

#### Получение названия пакета
```vb
[wrapped] packet name (by|of) id %number% [(and|,)] state %string% [(and|,)] bound %string%
```
ID на сайте обозначено числом в шестнадцатиричной системе счисления, при помощи модуля [Bitwise <i class="fas fa-link"></i>](../../bitwise/#число-в-шестнадцатиричной-системе) 
мы можем указать значение как описано на сайте, так и перевести число с сайта в десятичную систему счисления (выбирайте как вам удобно).
```vb
on load:
  broadcast packet name of id 0x50 and state "play" and bound "client"
```

После загрузки скрипта, в чате будет написано название данного пакета - `PacketPlayOutCamera`.\
Больше код выше нам не понадобится, он был необходим чтобы узнать название пакета.\
Дальше нам необходимо заполнить буфер данными, которые описаны в столбцах `Field Name`, `Field Type`, `Notes`.\
Заполнять мы будем новую структуру - `ByteBuf`. Она представляет из себя набор байт, в который можно записывать байты и считывать их.
#### Создание буфера
Для создания пустого буфера используется выражение ниже:
```vb
empty buffer
```
{{% expand title="Пример" %}}
```vb
command packet_example:
  trigger:
    set {_buffer} to empty buffer
```
{{% /expand %}}

#### Заполнение буфера данными
Для записи в буфер используется следующие выражения:
```vb
write bytes %bytebuf% to %bytebuf%
write bool[ean] %boolean% to %bytebuf%
write uuid %string% to %bytebuf%
write string %string% to %bytebuf%
write utf[-| ]8 %string% to %bytebuf%
write position %vector% to %bytebuf%
write position %location% to %bytebuf%
write [unsigned] byte %number% to %bytebuf%
write [unsigned] short %number% to %bytebuf%
write float %number% to %bytebuf%
write double %number% to %bytebuf%
write int[eger] %number% to %bytebuf%
write long %number% to %bytebuf%
write angle %number% to %bytebuf%
write var[iable][ ]int[eger] %number% to %bytebuf%
write var[iable][ ]long %number% to %bytebuf%
```
Каждое выражение предоставляет определенный тип, описанный в разделе [Data type на wiki.vg <i class="fas fa-link"></i>](https://wiki.vg/Protocol#Data_types).\
Некоторые сложные типы, описанные на сайте, могут быть составлены из простых, поэтому они отсутствуют в выражениях.\
Пакет `PacketPlayOutCamera` принимает в себя поле с типом `VarInt` и значением равным идентификатору сущности.\
Для получения идентификатора [воспользуемся следующим выражением <i class="fas fa-link"></i>](./additional/#получить-id-сущности).\
Из таблицы, мы знаем что записать данный идентификатор нужно с типом `VarInt`. Воспользуемся необходимым выражением:
```vb
command packet_example:
  trigger:
    set {_buffer} to empty buffer
    set {_entity} to target entity of player
    set {_id} to entity id of {_entity}
    write varint {_id} to {_buffer}
```
Так как таблица больше не содержит данных, мы можем создать пакет из буфера по его названию.

#### Write index 
Каждая запись в буфер сдвигает его `Writer index`, это количество записанных байт внутри буфера.\
`Writer index` можно узнать, или изменить при помощи следующего выражеения:
```vb
writer index of %bytebuf%
```
```vb
%bytebuf%'s writer index
```

#### Создание пакета из буфера
Выражение ниже позволяет по названию пакета и заполненному буферу создать пакет для последующей отправки.
```vb
[create] packet %string% (from|of|with) %bytebuf%
```
```vb
command packet_example:
  trigger:
    set {_buffer} to empty buffer
    set {_entity} to target entity of player
    set {_id} to entity id of {_entity}
    write varint {_id} to {_buffer}
    set {_packet} to create packet "PacketPlayOutCamera" with {_buffer}
```
Мы создали наш первый пакет, осталось только отправить его игроку.
#### Отправка пакетов
Для отправки пакетов используется следующее выражение:
```vb
send packet %packets% to %players%
```

Выражения ниже позволяют отправить пакет, без вызова события [on packet <i class="fas fa-link"></i>](./packet-handling/#событие-получения-или-отправки-пакета)
```vb
send packet %packets%  without [(trigger|call)[ing]] [the] event to %players%
```
```vb
send packet %packets% to %players%  without [(trigger|call)[ing]] [the] event
```
{{% expand title="Пример" %}}
```vb
command packet_example:
  trigger:
    set {_buffer} to empty buffer
    set {_entity} to target entity of player
    set {_id} to entity id of {_entity}
    write varint {_id} to {_buffer}
    set {_packet} to create packet "PacketPlayOutCamera" with {_buffer}
    send packet {_packet} to player
```
{{% /expand %}}
Для проверки работоспособности кода выше, направьте прицел на любую сущность, а затем пропишите команду `/packet_example`.\
После выполнения команды, вы будете смотреть от лица сущности, даже если вы не в режиме наблюдателя.
\
\
Таким образом вы можете создать любой пакет, следуя описанным выше действиям.
