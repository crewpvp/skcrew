+++
archetype = "default"
title = "Conditions"
weight = 2
+++
# CONDITIONS

#### Check that the server is online
```vb
%servers% is (online|connected)
```
```vb
%servers% is(n't| not) (online|connected)
```

#### Check that the player is online
Allows you to change the player's server if it is located on a server that is connected to a proxy server
```vb
%offlineplayers% is ([online ]on|connected to) proxy
```
```vb
%offlineplayers% (does|is)(n't| not) ([online ]on|connected to) proxy
```


