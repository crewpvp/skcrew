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

#### Get text bytes
Returns a list of bytes.\
It is also possible to specify the required encoding. Default is `UTF-8`
```vb
bytes of %string% [with charset %string%]
```
```vb
%string%'s bytes [with charset %string%]
```

