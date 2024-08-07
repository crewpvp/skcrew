+++
archetype = "default"
title = "Установка"
weight = 1
+++
# Установка
Скачайте последнюю версию по данной [**ссылке** <i class="fas fa-link"></i>](https://github.com/crewpvp/skcrew/releases/latest/download/Skcrew.jar) и переместите скачанный файл в директорию **plugins** внутри игрового или прокси сервера
\
После запуска игрового или прокси сервера в папке **plugins** будет создана папка **Skcrew** где находится файл конфигурации аддона. В нем вы можете включить или отключить определенные модули, а также настроить некоторые параметры, если это необходимо.
\
\
Стандартная конфигурация для игрового сервера (Spigot и ему подобные форки):
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
string-utils:
  enabled: true
viaversion:
  enabled: true
world:
  enabled: true
```

Стандартная конфигурация для прокси сервера ( [**Velocity** <i class="fas fa-link"></i>](https://github.com/PaperMC/Velocity) или [**Bungeecord** <i class="fas fa-link"></i>](https://github.com/SpigotMC/BungeeCord) ):
```yaml
socket-server-port: 1337

web-server-enabled: true
web-server-port: 1338
web-server-user: admin
web-server-password: admin
``` 


Поддерживаемые версии **Skript**:
- [**Skript 2.6.4 for 1.8** <i class="fas fa-link"></i>](https://github.com/Matocolotoe/Skript-1.8/releases/tag/2.6.4-for-1.8)
- [**Skript 2.6.5 for 1.8** <i class="fas fa-link"></i>](https://github.com/Matocolotoe/Skript-1.8/releases/tag/2.6.5-for-1.8)
- [**Skript 2.6.4** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.6.4)
- [**Skript 2.7.0** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.7.0)
- [**Skript 2.7.1** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.7.1)
- [**Skript 2.7.2** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.7.2)
- [**Skript 2.7.3** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.7.3)
- [**Skript 2.8.0** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.8.0)
- [**Skript 2.8.1** <i class="fas fa-link"></i>](https://github.com/SkriptLang/Skript/releases/tag/2.8.1)

Модули аддона и их поддержка на разных версиях сервера:
| Модуль                                      | 1.8.8 | 1.12.2 | 1.14+   |
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
Модуль будет автоматически отключен, если не поддерживается версией сервера, поэтому не переживайте, если что-то сделали не так <yellow><i class="fas fa-smile"></i></yellow>
{{% /notice %}}