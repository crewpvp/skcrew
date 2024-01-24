+++
archetype = "default"
title = "Expressions"
weight = 2
+++
# EXPRESSIONS
#### Get the main server world
Returns the main world of the server, without which the server operation is impossible.
```vb
main[ ]world
```

#### Get a chunk by its coordinates
In this expression, not the coordinates of the blocks are used, but the coordinates of the chunk.\
To get the coordinates of the chunk, the X and Z coordinates are divided by 16.
```vb
chunk [at] %number%[ ],[ ]%number% (in|at) %world%
```

#### Get all entities inside the chunk
```vb
entit(y|ies) (of|in) %chunk%
```
```vb
%chunk%'s entit(y|ies)
```

#### Get all the uploaded chunks in the world
```vb
[all [[of] the]] loaded chunks (in|at|of) %world%
```
```vb
%world%'s [all [[of] the]] loaded chunks
```

#### Constant loading of the chunk
{{% notice style="warning" %}}
Not supported on versions lower than 1.13.1
{{% /notice %}}
This expression supports changing the state
```vb
force load[ed] of %chunk%
```
```vb
%chunk%'s force load[ed]
```

#### The center of the barrier of the world
This expression supports changing the state
```vb
[world] border center of %world%
```
```vb
%world%'s [world] border center
```

#### The size of the world barrier
This expression supports changing the state.
When changing, it is possible to specify the time in ticks for how long the barrier should change its size
```vb
[world[ ]]border size of %world% [for %-timespan%]
```
```vb
%world%'s [world[ ]]border size [for %-timespan%]
```
```vb
[the] size of %world%'s [world[ ]]border [for %-timespan%]
```