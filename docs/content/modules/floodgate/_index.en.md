+++
archetype = "default"
title = "Floodgate"
weight = 3
+++
# FLOODGATE

This module allows you to interact with the plugin [**Floodgate** <i class="fas fa-link"></i>](https://github.com/GeyserMC/Floodgate), and also create interactive forms
{{% notice style="warning" %}}
A plugin is required for the module to work [**Floodgate** <i class="fas fa-link"></i>](https://github.com/GeyserMC/Floodgate)
{{% /notice %}}

#### Check that the player has logged in with Bedrock Edition
```vb
%player% [(is|does)](n't| not) from floodgate
```
#### Get the localization of the player
```vb
[the] be[[drock] [edition]] (locale|language) of [the] [floodgate] %player%           
```
```vb
%player%'s be[[drock] [edition]] (locale|language) [of [the] floodgate]
```
#### Get the name of the player's device
```vb
[the] [be[[drock] [edition]]] (platform|device) of [the] [floodgate] %player%
```
```vb
%player%'s [be[[drock] [edition]]] (platform|device) [of [the] floodgate]
```
#### Get the Bedrock version of the client
```vb
[the] be[[drock] [edition]] version of [the] [floodgate] %player%
```
```vb
%player%'s be[[drock] [edition]] version [of [the] floodgate]
```