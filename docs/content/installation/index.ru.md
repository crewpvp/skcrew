+++
archetype = "default"
title = "Установка"
weight = 2
+++

Скачайте последнюю версию по данной [**ссылке**](https://github.com/crewpvp/skcrew/releases/latest/download/Skcrew.jar) и переместите скачанный файл в директорию **plugins** внутри игрового или прокси сервера\
\
После запуска игрового или прокси сервера в папке **plugins** будет создана папка **Skcrew** где находится файл конфигурации аддона. В нем вы можете включить или отключить определенные модули, а также настроить некоторые параметры, если это необходимо.
\
\
Стандартная конфигурация для игрового сервера (Spigot и ему подобные форки):
```yaml
sql:
  enabled: true
  driver-class-name: "default"
bitwise:
  enabled: true
gui:
  enabled: true
runtime:
  enabled: true
requests:
  enabled: true
files:
  enabled: true
world:
  enabled: true
permissions:
  enabled: true
interpretate:
  enabled: true
string-utils:
  enabled: true
floodgate:
  enabled: true
viaversion:
  enabled: true
map:
  enabled: true
other:
  enabled: true
maps:
  enabled: true
sockets:
  enabled: false
  server-address: "127.0.0.1"
  server-port: 1337
  client-autoreconnect-time: 5
```

Стандартная конфигурация для прокси сервера (Bungeecord или Velocity):
```yaml
socket-server-port: 1337

web-server-enabled: true
web-server-port: 1338
web-server-user: admin
web-server-password: admin
``` 


Поддерживаемые версии **Skript**:
- [x] [**Skript 2.6.4 for 1.8**](https://github.com/Matocolotoe/Skript-1.8/releases/tag/2.6.4-for-1.8)
- [x] [**Skript 2.6.4**](https://github.com/SkriptLang/Skript/releases/tag/2.6.4)
- [x] [**Skript 2.7.0**](https://github.com/SkriptLang/Skript/releases/tag/2.7.0)
- [x] [**Skript 2.7.1**](https://github.com/SkriptLang/Skript/releases/tag/2.7.1)


Модули аддона и их поддержка на разных версиях сервера:
| Модуль                                      | 1.8.8 | 1.12.2 | 1.14+   |
|--------------------------------------------:|:-----:|:------:|:-------:|
| [**bitwise**](../modules/bitwise)           |  ✔  |   ✔   |   ✔   |
| [**sql**](../modules/sql)                   |  ✔  |   ✔   |   ✔   |
| [**gui**](../modules/gui)                   |  ✔  |   ✔   |   ✔   |
| [**runtime**](../modules/gui)               |  ✔  |   ✔   |   ✔   |
| [**requests**](../modules/requests)         |  ✔  |   ✔   |   ✔   |
| [**files**](../modules/files)               |  ✔  |   ✔   |   ✔   |
| [**world**](../modules/world)               |  ✔  |   ✔   |   ✔   |
| [**permissions**](../modules/permissions)   |  ✔  |   ✔   |   ✔   |
| [**interpretate**](../modules/interpretate) |  ✔  |   ✔   |   ✔   |
| [**string-utils**](../modules/string-utils) |  ✔  |   ✔   |   ✔   |
| [**floodgate**](../modules/floodgate)       |  ✔  |   ✔   |   ✔   |
| [**viaversion**](../modules/viaversion)     |  ✔  |   ✔   |   ✔   |
| [**maps**](../modules/maps)                 | **Х** | **Х**  |   ✔   |
| [**other**](../modules/other)               |  ✔  |   ✔   |   ✔   | 
| [**sockets**](../modules/sockets)           |  ✔  |   ✔   |   ✔   |


{{% notice style="note" %}}
Модуль будет автоматически отключен, если не поддерживается версией сервера, поэтому не переживайте, если что-то сделали не так <font color="gold"><i class="fas fa-smile"></i>
{{% /notice %}}