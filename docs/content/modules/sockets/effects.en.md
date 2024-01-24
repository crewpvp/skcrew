+++
archetype = "default"
title = "Effects"
weight = 3
+++
# EFFECTS

#### Kick a player from a proxy server
Allows you to specify the reason. The reason may be plain text, or an AdventureAPI component ( [Velocity <i class="fas fa-link"></i>](https://docs.advntr.dev/serializer/index.html) ) or ChatComponentAPI ( [Bungeecord <i class="fas fa-link"></i>](https://www.spigotmc.org/wiki/the-chat-component-api/) ) in JSON format. 
```vb
kick %offlineplayers% from proxy [(by reason of|because [of]|on account of|due to) %string%]
```

#### Move the player to the server
Allows you to change the player's server if it is located on a server that is connected to a proxy server
```vb
switch %offlineplayers% to %server%
```


