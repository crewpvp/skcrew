+++
archetype = "default"
title = "Other"
weight = 7
+++

# OTHER

#### Give or drop
Gives items to the player, if there is no place in his inventory, he throws them next to him
```vb
give or drop %itemtypes/experiences% to %players%
```

#### Inventory drag event
An event triggered when a player presses the mouse button to distribute items in the inventory
```vb
on inventory drag
```
#### Getting added items in this event
Returns a list of things that have been added
```vb
new items
```
```vb
added items
```
#### Get affected slots
Returns the slot numbers that have been changed in the same order as it returns the expression for things
```vb
changed slot[s]
```
```vb
event[-]slots
```
