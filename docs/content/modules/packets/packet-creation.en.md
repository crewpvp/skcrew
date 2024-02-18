+++
archetype = "default"
title = "Creating packets"
weight = 1
+++
# Creating packets
#### Learning wiki.vg
[This site <i class="fas fa-link"></i>](https://wiki.vg/Protocol_version_numbers#Release) it contains a description of the packets used in the Minecraft backend.\
Select the core version on the website on which the server is located and click on the link of this version.\
First, we need to find the packet that we need. All packets located in the `SERVERBOUND` section - packets are sent to the server players in the `CLIENTBOUND` section - from the server to the player.
We are obviously interested in the packets section `CLIENTBOUND`.\
Let's choose a packet [**SET CAMERA** <i class="fas fa-link"></i>](https://wiki.vg/Protocol#Set_Camera), since it has not changed since version 1.8.\
This packet allows you to set a camera for the player, similar to the camera that occurs when you click on an entity in observer mode.\
We see the following table on the website:
| Packet ID          |   State   |   Bound To   |   Field Name   |     Field Type    | Notes      |
|:------------------:|:---------:|:------------:|:--------------:|:-----------------:|:-----------|
|       0x50       |   Play    |    Client    | Camera ID    |      VarInt       |ID of the entity to set the client's camera to. |

This table contains the packet number, the moment of gameplay when it can be received, what the packet is linked to, and a description of its contents.\
First of all, we need to find out what this packet is called on the server. To do this, we will need the first three fields:
- id
- state
- bound

#### Getting the packet name
```vb
[wrapped] packet name (by|of) id %number% [(and|,)] state %string% [(and|,)] bound %string%
```
ID on the site is indicated by a number in the hexadecimal number system, using the module [Bitwise <i class="fas fa-link"></i>](../../bitwise/#число-в-шестнадцатиричной-системе) 
we can specify the value as described on the site, or convert the number from the site to the decimal system (choose how convenient for you).
```vb
on load:
  broadcast packet name of id 0x50 and state "play" and bound "client"
```

After downloading the script, the name of this packet will be written in the chat - `PacketPlayOutCamera`.\
We won't need any more code above, it was necessary to find out the name of the packet.\
Next, we need to fill the buffer with the data described in the columns `Field Name`, `Field Type`, `Notes`.\
We will fill in a new structure - `ByteBuf'. It is a set of bytes into which bytes can be written and read.
#### Creating a buffer
The expression below is used to create an empty buffer:
```vb
empty buffer
```
{{% expand title="Example" %}}
```vb
command packet_example:
  trigger:
    set {_buffer} to empty buffer
```
{{% /expand %}}

#### Filling the buffer with data
The following expressions are used to write to the buffer:
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
Each expression provides a specific type, described in the section [Data type on wiki.vg <i class="fas fa-link"></i>](https://wiki.vg/Protocol#Data_types).\
Some complex types described on the site can be made up of simple ones, so they are missing in expressions.\
Packet `PacketPlayOutCamera` accepts a field with the type `VarInt` and a value equal to the entity ID.\
To get the identifier [use the following expression <i class="fas fa-link"></i>](./additional/#получить-id-сущности).\
From the table, we know that you need to write this identifier with the type `VarInt`. Let's use the necessary expression:
```vb
command packet_example:
  trigger:
    set {_buffer} to empty buffer
    set {_entity} to target entity of player
    set {_id} to entity id of {_entity}
    write varint {_id} to {_buffer}
```
Since the table no longer contains data, we can create a packet from the buffer by its name.

#### Write index 
Each write to the buffer shifts its `Writer index`, which is the number of bytes written inside the buffer.\
`Writer index` you can find out or change it using the following expression:
```vb
writer index of %bytebuf%
```
```vb
%bytebuf%'s writer index
```

#### Creating a packet from a buffer
The expression below allows you to create a packet based on the packet name and the filled buffer for subsequent sending.
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
We have created our first packet, it remains only to send it to the player.
#### Sending packets
The following expression is used to send packets:
```vb
send packet %packets% to %players%
```
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

To check the operability of the code above, aim the scope at any entity, and then write the command `/packet_example`.\
After executing the command, you will be looking from the entity's face, even if you are not in observer mode.
\
\
This way you can create any packet by following the steps described above.
