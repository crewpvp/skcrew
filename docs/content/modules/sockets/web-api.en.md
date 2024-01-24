+++
archetype = "default"
title = "Web API"
weight = 6
+++
# WEB API
API allows you to get information from servers, as well as send signals from outside.\
Make sure that the web server is enabled in the Screw settings of your proxy server.
```
web-server-enabled: true
web-server-port: 1338
web-server-user: admin
web-server-password: admin
```
Here you can also configure the web server port and authorization data to access the API.\
Authorization is performed by passing the username and password to base64 in the request header:
```
Authorization: Basic base64_encode("login:password")
```
{{% expand title="Request Example (curl)" %}}
```bash
curl localhost:1338/players -H "Authorization: Basic YWRtaW46YWRtaW4="
```
{{% /expand %}}

#### Available routes for requests.
#### {{% lighting color="red" %}}GET{{% /lighting %}} &nbsp;&nbsp; `/players`
Allows you to get a list of all players with the name of the server they are on.\
Possible parameters:
- `server`:`string` - allows you to get players from certain servers

{{% expand title="Request Example (curl)" %}}
```bash
curl localhost:1338/players -H "Authorization: Basic YWRtaW46YWRtaW4="
```
{{% /expand %}}
{{% expand title="Example answer" %}}
```json
{
  "data": [
    {
      "name": "Lotzy",
      "uuid": "a0970e26-b9f4-3f73-bd06-ede16c390d34",
      "join_date": 1706008310,
      "time_played": 1089,
      "server_name": "lobby"
    }
  ]
}
```
{{% /expand %}}

#### {{% lighting color="red" %}}GET{{% /lighting %}} &nbsp;&nbsp; `/players/{UUID/NICKNAME}`
Allows you to get a player, if he is online, by his nickname or UUID. As well as the entire server on which it is located.
{{% expand title="Request Example (curl)" %}}
```bash
curl localhost:1338/players/Lotzy -H "Authorization: Basic YWRtaW46YWRtaW4="
```
{{% /expand %}}
{{% expand title="Example answer" %}}
```json
{
  "data": {
    "name": "Lotzy",
    "uuid": "a0970e26-b9f4-3f73-bd06-ede16c390d34",
    "join_date": 1706008310,
    "time_played": 2335,
    "server_name": "lobby",
    "server": {
      "name": "lobby",
      "address": "127.0.0.1",
      "port": 25565,
      "hostname": "127.0.0.1",
      "online": true,
      "connection_date": 1706008017,
      "uptime": 2628,
      "players_count": 1,
      "players": [
        {
          "name": "Lotzy",
          "uuid": "a0970e26-b9f4-3f73-bd06-ede16c390d34",
          "join_date": 1706008310,
          "time_played": 2335,
          "server_name": "lobby"
        }
      ]
    }
  }
}
```
{{% /expand %}}

#### {{% lighting color="red" %}}GET{{% /lighting %}}&nbsp;{{% lighting color="orange" %}}POST{{% /lighting %}} &nbsp;&nbsp; `/players/{UUID/NICKNAME}/kick`
Allows you to kick a player from a proxy server, by his nickname or UUID.\
When using a POST request, you can specify the reason for the kick. The reason can be specified either in plain text or by the AdventureAPI component ( [Velocity <i class="fas fa-link"></i>](https://docs.advntr.dev/serializer/index.html) ) or ChatComponentAPI ( [Bungeecord <i class="fas fa-link"></i>](https://www.spigotmc.org/wiki/the-chat-component-api/) ) in JSON format. 
{{% expand title="Request Example (curl)" %}}
```bash
curl localhost:1338/players/Lotzy/kick -X POST -H "Authorization: Basic YWRtaW46YWRtaW4=" -H "Content-Type: application/json" -d '"GO OUT FROM SERVER"'
```
{{% /expand %}}
{{% expand title="Example answer" %}}
```json
{ 
  "data":
    "Player Lotzy kicked"
}
```
{{% /expand %}}

#### {{% lighting color="red" %}}GET{{% /lighting %}} &nbsp;&nbsp; `/players/{UUID/NICKNAME}/connect/{SERVER}`
Allows you to move a player to another server, by his nickname or UUID.\
{{% expand title="Request Example (curl)" %}}
```bash
curl localhost:1338/players/Lotzy/connect/lobby -H "Authorization: Basic YWRtaW46YWRtaW4="
```
{{% /expand %}}
{{% expand title="Example answer" %}}
```json
{ 
  "data":
    "Player Lotzy connected to server lobby"
}
```
{{% /expand %}}


#### {{% lighting color="red" %}}GET{{% /lighting %}} &nbsp;&nbsp; `/servers`
Get a list of all servers and players on them.\
Possible parameters:
- `online`:`boolean` - allows you to get only those servers that are enabled

{{% expand title="Request Example (curl)" %}}
```bash
curl localhost:1338/servers?online=true -H "Authorization: Basic YWRtaW46YWRtaW4="
```
{{% /expand %}}
{{% expand title="Example answer" %}}
```json
{
  "data": [
    {
      "name": "lobby",
      "address": "127.0.0.1",
      "port": 25565,
      "hostname": "127.0.0.1",
      "online": true,
      "connection_date": 1706008017,
      "uptime": 2132,
      "players_count": 1,
      "players": [
        {
          "name": "Lotzy",
          "uuid": "a0970e26-b9f4-3f73-bd06-ede16c390d34",
          "join_date": 1706008310,
          "time_played": 1839,
          "server_name": "lobby"
        }
      ]
    }
  ]
}
```
{{% /expand %}}


#### {{% lighting color="orange" %}}POST{{% /lighting %}} &nbsp;&nbsp; `/servers/{SERVER}/signal`
Allows you to send a signal to the connected server.\
The signal itself is transmitted in the request body.
{{% expand title="Request Example (curl)" %}}
```bash
curl localhost:1338/servers/lobby/signal -X POST -H "Authorization: Basic YWRtaW46YWRtaW4=" -H "Content-Type: application/json" -d "{'signals':[{'key':'broadcast','data':['Hello world!']}]}"
```
{{% /expand %}}
{{% expand title="Example answer" %}}
```json
{ 
  "data": {
    "response": "Signals successfully sended to servers" 
  } 
}
```
{{% /expand %}}

#### {{% lighting color="orange" %}}POST{{% /lighting %}} &nbsp;&nbsp; `/signal`
Allows you to send a signal to the connected servers.\
The signal itself and the list of servers are transmitted in the request body.
{{% expand title="Request Example (curl)" %}}
```bash
curl localhost:1338/servers/lobby/signal -X POST -H "Authorization: Basic YWRtaW46YWRtaW4=" -H "Content-Type: application/json" -d "{'servers':['lobby'],'signals':[{'key':'broadcast','data':['Hello world!']}]}"
```
{{% /expand %}}
{{% expand title="Example answer" %}}
```json
{ 
  "data": {
    "response": "Signals successfully sended to servers" 
  } 
}
```
{{% /expand %}}

{{% notice style="note" %}}
You can see an example of implementing API access in Python at [this link <i class="fas fa-link"></i>](https://github.com/crewpvp/skcrew-python)
{{% /notice %}}


