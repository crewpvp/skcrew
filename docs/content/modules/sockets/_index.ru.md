+++
archetype = "default"
title = "Sockets"
weight = 11
+++
# SOCKETS
Данный модуль позволяет серверам под контролем прокси сервера обмениваться информацией через сокеты.

Для работы модуля необходимо установить Skcrew на прокси сервер ( [**Velocity** <i class="fas fa-link"></i>](https://github.com/PaperMC/Velocity) или [**Bungeecord** <i class="fas fa-link"></i>](https://github.com/SpigotMC/BungeeCord) ). В файле конфигурации на прокси сервере указать порт для открытия сокет сервера, на игровом сервере указать адрес прокси сервера и порт открытого сокет сервера.
{{% expand title="Пример" %}}
Конфигурация на прокси сервере
```yaml
socket-server-port: 1337
```
Конфигурация на игровом сервере
```yaml
sockets:
  enabled: false
  server-address: "127.0.0.1"
  server-port: 1337
  client-autoreconnect-time: 5
```
`client-autoreconnect-time` - описывает время, через которое игровой сервер попробует переподключиться в случае отключения прокси сервера.
{{% /expand %}}

{{% big-link url="./expressions" icon="fa-solid fa-link" %}}**EXPRESSIONS**{{% /big-link %}}

{{% big-link url="./conditions" icon="fa-solid fa-link" %}}**CONDITIONS**{{% /big-link %}}

{{% big-link url="./effects" icon="fa-solid fa-link" %}}**EFFECTS**{{% /big-link %}}

{{% big-link url="./events" icon="fa-solid fa-link" %}}**EVENTS**{{% /big-link %}}

{{% big-link url="./signals" icon="fa-solid fa-link" %}}**SIGNALS**{{% /big-link %}}

{{% big-link url="./web-api" icon="fa-solid fa-link" %}}**WEB API**{{% /big-link %}}