+++
archetype = "default"
title = "Floodgate"
weight = 3
+++
# FLOODGATE

Данный модуль позволяет взаимодействовать с плагином [**Floodgate** <i class="fas fa-link"></i>](https://github.com/GeyserMC/Floodgate), а так же создавать интерактивные формы
{{% notice style="warning" %}}
Для работы модуля необходим плагин [**Floodgate** <i class="fas fa-link"></i>](https://github.com/GeyserMC/Floodgate)
{{% /notice %}}

#### Проверить, что игрок зашел с Bedrock Edition
```vb
%player% [(is|does)](n't| not) from floodgate
```
#### Получить локализацию игрока
```vb
[the] be[[drock] [edition]] (locale|language) of [the] [floodgate] %player% 
```
```vb
%player%'s be[[drock] [edition]] (locale|language) [of [the] floodgate]
```
#### Получить название устройства игрока
```vb
[the] [be[[drock] [edition]]] (platform|device) of [the] [floodgate] %player%
```
```vb
%player%'s [be[[drock] [edition]]] (platform|device) [of [the] floodgate]
```
#### Получить версию Bedrock клиента
```vb
[the] be[[drock] [edition]] version of [the] [floodgate] %player%
```
```vb
%player%'s be[[drock] [edition]] version [of [the] floodgate]
```