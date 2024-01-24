+++
archetype = "default"
title = "Effects"
weight = 1
+++
# EFFECTS
#### Load the world
Loads the world by its name. The world must be located in the root directory of the server.
```vb
load world %string%
```

#### Unload the world
Unload the world. It also allows you not to save changes when uploading.
```vb
unload [world] %world% [without save]
```

#### Load a chunk
It also allows you not to generate a world in a chunk if this chunk has not been generated.
```vb
load %chunk% [without gen[erate]]
```

#### Upload a chunk
Unload the chunk. It also allows you not to save changes when uploading.
```vb
unload [chunk] %chunk% [without save]
```

#### Create a world
It also allows you to create a flat world.
```vb
create world %string% [[with type] [super]flat]
```

#### Delete the world
```vb
delete world %world/string%
```

#### Create a copy of the world
```vb
(copy|duplicate) world %string% [(with|using)] name[d] %string%
```
