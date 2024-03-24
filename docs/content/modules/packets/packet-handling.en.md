+++
archetype = "default"
title = "Packet handling"
weight = 2
+++
# Packet handling
#### The event of receiving or sending a packet.
This event allows you to receive a packet sent by the player or sent by the server to the player.
```vb
on packet:
```
```
on packet <packet name>:
```
`<packet name>` - allows you to specify the name of the tracking packet.\
\
The event can be canceled. In this case, the packet will not be received by the server, or sent to the player\
\
This event has built-in expressions.
- `event-packet` - to receive the packet
- `event-player` - to get a player

{{% notice style="note" %}}
`event-packet` it can be replaced with `set event-packet to %packet%`, provided that no execution delay was used 
{{% /notice %}}


{{% expand title="Example" %}}
```vb
on packet PacketPlayOutOpenSignEditor:
  broadcast "%event-player%"
  broadcast "%event-packet%"
  cancel event
```
In this example, the player's nickname and the packet name are displayed in the chat. And then cancel sending this event to the player.\
The packet itself forces the player to open the text editing window of the plate.
{{% /expand %}}

#### Getting a buffer from a packet
After receiving the buffer from the packet, we can read the fields indicated on [wiki.vg <i class="fas fa-link"></i>](https://wiki.vg/Protocol_version_numbers#Release)
```vb
buffer (of|from) %packet%
```
```vb
%packet%'s buffer
```

#### Reading the buffer
To get values from the buffer, the following expressions exist, similar to [writing to the buffer <i class="fas fa-link"></i>](./packet-creation/#filling-the-buffer-with-data)
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
To read utf-8 you must also specify the length of the text in bytes
{{% /notice %}}
Reading occurs in the same order as writing a packet, provided that the packet was not created by you. If you created a package and want to read it (it is not known for what reason), then move its reader index to position zero.\
Consider the packet [PacketPlayOutOpenSignEditor <i class="fas fa-link"></i>](https://wiki.vg/Protocol#Open_Sign_Editor)
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

The code above will display a Vector with the coordinates of the open plate in the chat.

#### Reader index 
Each reading of the buffer shifts its `Reader index`, this is the number of bytes read inside the buffer.\
`Reader index` you can find out or change it using the following expression:
```vb
reader index of %bytebuf%
```
```vb
%bytebuf%'s reader index
```

#### Disabling or enabling packet handling
For any player, you can enable or disable tracking of incoming or outgoing packets.
```vb
[(handl(e|ing))|(listen[ing] [of])] (in|out)coming packets of %player%
```
```vb
%player%'s [(handl(e|ing))|(listen[ing] [of])] (in|out)coming packets
```

{{% expand title="Example" %}}
```vb
on join:
  set listening incoming packets of player to false
```
{{% /expand %}}
