+++
archetype = "default"
title = "Additionally"
weight = 3
+++
# ADDITIONALLY

#### Get the Entity ID
Returns the unique identifier of the entity
```vb
entity id of %entity%
```
```vb
%entity%'s entity id
```

#### Get or change the player's Skin value
```vb
skin value of %player%   
```
```vb
%player%'s skin value
```

#### Get or change a player's Skin signature
```vb
skin signature of %player%   
```
```vb
%player%'s skin signature
```

#### Clear player's skin signature and value
```vb
delete skin properties of %player%
```
```vb
delete %player%'s skin properties
```

#### Get text bytes
Returns a list of bytes.\
It is also possible to specify the required encoding. Default is `UTF-8`
```vb
bytes of %string% [with charset %string%]
```
```vb
%string%'s bytes [with charset %string%]
```

#### Create Bundle packet
This is a packet that allows you to create a sequence of packets and send them to the client at once, which provides greater speed and stability. \
If you are sending many packets at once, put them in a Bundle.
```vb
bundle packet (from|of|with) %packets%
```
