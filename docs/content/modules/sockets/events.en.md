+++
archetype = "default"
title = "Events"
weight = 4
+++
# EVENTS

#### The player has connected to the proxy server
Called when the player has not yet connected to any server
```vb
on player (join|connect)[(ed|s)] [to] proxy [server]
```
The event has built-in data, using the `event-player` you can get the connected player in the form of an `OfflinePlayer`

#### The player disconnected from the proxy server
```vb
on player (leave|disconnect)[(ed|s)] [from] proxy [server]
```
The event has built-in data, using the `event-player` you can get the connected player in the form of an `OfflinePlayer`

#### The current server has connected to the proxy
Called after the server has been connected to the proxy.
```vb
on connect(ed|s) [to] proxy [server]
```
```vb
on proxy connect
```

#### The current server has disconnected from the proxy
Called after the server has been disconnected from the proxy.
```vb
on disconnect(ed|s) [from] proxy [server]
```
```vb
on proxy disconnect
```

#### Attempt to reconnect to the proxy
Called when the server tries to reconnect to the proxy.
```vb
on reconnect(ing|s) [to] proxy [server]
```
```vb
on proxy reconnect
```

#### The server has connected to the proxy
Called after any server has connected to the proxy
```vb
on server connect(ed|s) [to] proxy [server]
```
The event has built-in data, using the `event-server` you can get a connected server

#### The server was disconnected from the proxy
Called after any server has disconnected from the proxy
```vb
on server disconnect(ed|s) [from] proxy [server]
```
The event has embedded data, using the `event-server` you can get a disconnected server

#### The player has connected to the game server
It is called after the player is connected to the game server, which is connected to the proxy.
```vb
on player (join|connect)[(ed|s)] [to] (proxied|network) server
```
The expression below allows you to track connections only to a specific server by its name.
```vb
on player (join|connect)[(ed|s)] [to] (proxied|network) server %string%
```
The event has built-in data, using the `event-server` you can get the player's server, and using the `event-player` you can get the player in the form of an `OfflinePlayer`

#### The player disconnected from the game server
It is called after the player disconnects from the game server that is connected to the proxy.
```vb
on player (leave|disconnect[e])[(d|s)] [from] (proxied|network) server
```
The expression below allows you to track disconnections only from a specific server by its name.
```vb
on player (leave|disconnect[e])[(d|s)] [from] (proxied|network) server %string%
```
The event has built-in data, using the `event-server` you can get the server of the player from which he left, and using the `event-player` you can get the player in the form of an `OfflinePlayer`