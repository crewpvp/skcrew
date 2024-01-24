+++
archetype = "default"
title = "Sockets"
weight = 11
+++
# SOCKETS
This module allows servers under the control of a proxy server to exchange information via sockets.

For the module to work, you need to install Skcrew on a proxy server ( [**Velocity** <i class="fas fa-link"></i>](https://github.com/PaperMC/Velocity) or [**Bungeecord** <i class="fas fa-link"></i>](https://github.com/SpigotMC/BungeeCord) ). In the configuration file on the proxy server, specify the port for opening the server socket, on the game server specify the address of the proxy server and the port of the open server socket.
{{% expand title="Example" %}}
Configuration on the proxy server
```yaml
socket-server-port: 1337
```
Configuration on the game server
```yaml
sockets:
  enabled: false
  server-address: "127.0.0.1"
  server-port: 1337
  client-autoreconnect-time: 5
```
`client-autoreconnect-time` - describes the time after which the game server will try to reconnect if the proxy server is disabled.
{{% /expand %}}

{{% big-link url="./expressions" icon="fa-solid fa-link" %}}**EXPRESSIONS**{{% /big-link %}}

{{% big-link url="./conditions" icon="fa-solid fa-link" %}}**CONDITIONS**{{% /big-link %}}

{{% big-link url="./effects" icon="fa-solid fa-link" %}}**EFFECTS**{{% /big-link %}}

{{% big-link url="./events" icon="fa-solid fa-link" %}}**EVENTS**{{% /big-link %}}

{{% big-link url="./signals" icon="fa-solid fa-link" %}}**SIGNALS**{{% /big-link %}}

{{% big-link url="./web-api" icon="fa-solid fa-link" %}}**WEB API**{{% /big-link %}}