+++
archetype = "default"
title = "installation"
weight = 1
+++
# installation
Download the latest version from this [**link** <i class="fas fa-link"></i>](https://github.com/crewpvp/skcrew/releases/latest/download/Skcrew.jar) and move the downloaded file to the **plugins** directory inside the game server or proxy server
\
After launching the game or proxy server, the **Skcrew** folder will be created in the **plugins** folder, where the addon configuration file is located. In it, you can enable or disable certain modules, as well as adjust some settings, if necessary.
\
\
The standard configuration for the game server (Spigot and similar forks):
```yaml
bitwise:
  enabled: true
files:
  enabled: true
floodgate:
  enabled: true
gui:
  enabled: true
interpretate:
  enabled: true
maps:
  enabled: true
other:
  enabled: true
packets:
  enabled: true
permissions:
  enabled: true
requests:
  enabled: true
runtime:
  enabled: true
sockets:
  enabled: false
  server-address: "127.0.0.1"
  server-port: 1337
  client-autoreconnect-time: 5
sql:
  enabled: true
  driver-class-name: "default"
string-utils:
  enabled: true
viaversion:
  enabled: true
world:
  enabled: true

```

Standard configuration for a proxy server ( [**Velocity** <i class="fas fa-link"></i>](https://github.com/PaperMC/Velocity) or [**Bungeecord** <i class="fas fa-link"></i>](https://github.com/SpigotMC/BungeeCord) ):
```yaml
socket-server-port: 1337

web-server-enabled: true
web-server-port: 1338
web-server-user: admin
web-server-password: admin
``` 


Supported versions of the **Skript**:
- [**Skript 2.6.4 for 1.8** <i class="fas fa-link"></i>](https://github.com/Matocolotoe/Skript-1.8/releases/tag/2.6.4-for-1.8)
- [**Skript 2.6.5 for 1.8** <i class="fas fa-link"></i>](https://github.com/Matocolotoe/Skript-1.8/releases/tag/2.6.5-for-1.8)
- [**Skript 2.6.4** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.6.4)
- [**Skript 2.7.0** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.7.0)
- [**Skript 2.7.1** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.7.1)
- [**Skript 2.7.2** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.7.2)
- [**Skript 2.7.3** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.7.3)
- [**Skript 2.8.0** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.8.0)
- [**Skript 2.8.1** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.8.1)

Addon modules and their support on different server versions:
| Module                                      | 1.8.8 | 1.12.2 | 1.14+   |
|--------------------------------------------:|:-----:|:------:|:-------:|
| [**bitwise**](../modules/bitwise)           |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**files**](../modules/files)               |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**floodgate**](../modules/floodgate)       |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**gui**](../modules/gui)                   |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**interpretate**](../modules/interpretate) |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**maps**](../modules/maps)                 |  <red><i class="fas fa-xmark fa-lg"></i></red> |   <red><i class="fas fa-xmark fa-lg"></i></red> |   <green><i class="fas fa-check"></i></green>   |
| [**other**](../modules/other)               |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   | 
| [**packets**](../modules/packets)           |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**permissions**](../modules/permissions)   |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**requests**](../modules/requests)         |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   | 
| [**runtime**](../modules/gui)               |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**sockets**](../modules/sockets)           |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**sql**](../modules/sql)                   |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**string-utils**](../modules/string-utils) |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**viaversion**](../modules/viaversion)     |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |
| [**world**](../modules/world)               |  <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |   <green><i class="fas fa-check"></i></green>   |


{{% notice style="note" %}}
The module will be automatically disabled if it is not supported by the server version, so do not worry if you did something wrong <yellow><i class="fas fa-smile"></i></yellow>
{{% /notice %}}