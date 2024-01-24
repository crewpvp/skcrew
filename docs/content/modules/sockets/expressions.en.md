+++
archetype = "default"
title = "Expressions"
weight = 1
+++
# EXPRESSIONS

#### Get the current server
Returns the current game server
```vb
(current|this) server
```

#### Get the server by name
Returns the server by the name under which it is written in the proxy server configuration.\
If this server is not listed in the proxy server configuration, `<none>` will be returned 
```vb
server %string%
```

#### Get the name of the server
```vb
server[ ]name of %server%
```
```vb
%server%'s server[ ]name
```

#### Get a list of servers
The expression below returns a list of all servers described in the proxy server configuration.
```vb
[all] servers
```
Get only those servers that are enabled. In order for the server to be defined as enabled, it must be connected to a socket server.
```vb
[all] online servers
```

#### Get players from the server
Returns a list of players in the form of online players.
```vb
players (from|of|on) %servers%
```
```vb
%servers%'s players
```

#### Get a player on a proxy server
Accepts the player's nickname or UUID, returns the online player, or `<none>` if the player is not online
```vb
network[ ]player %string%
```

{{% notice style="note" %}}
The server player is automatically converted to `OfflinePlayer` if used in any expressions if necessary.
{{% /notice %}}

#### Get the player's server
```vb
server of %networkplayer%
```
```vb
%networkplayer%'s server
```




