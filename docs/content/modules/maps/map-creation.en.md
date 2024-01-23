+++
archetype = "default"
title = "Creating a map"
weight = 1
+++
# Creating a map

#### Creating a canvas
Returns an object from which, later, you can get a map in the form of an object
```vb
[new|empty] drawing map
```
{{% notice style="warning" %}}
When creating a map in a folder `/skcrew/maps/` A map image file will be created. The file name will match the ID of the game card.
{{% /notice %}}
#### Getting a canvas by card ID
```vb
[drawing] map (by|with) id %number%
```

#### Getting an item from the canvas
Accepts canvas as an argument, returns a game item - a map
```vb
[the] map item (from|of) %map%
```

#### Getting a canvas from an object
Accepts a map item as an argument, returns a canvas for drawing
```vb
[drawing] map (of|from) item %itemstack%
```

#### Forced saving of the canvas
```vb
save map %map%
```
{{% notice style="note" %}}
Maps are not loaded on their own, so that they do not disappear - put the canvases of these maps in global variables.

The addon supports automatic serialization of data associated with canvases.
{{% /notice %}}

