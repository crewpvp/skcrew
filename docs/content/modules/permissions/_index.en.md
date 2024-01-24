+++
archetype = "default"
title = "Permissions"
weight = 8
+++

# PERMISSIONS

#### Execute a command with operator rights
```vb
[execute] [the] command %strings% [by %-commandsenders%] as op
```
```vb
[execute] [the] %commandsenders% command %strings% as op
```
```vb
(let|make) %commandsenders% execute [[the] command] %strings% as op
```

#### User Rights Management
Allows you to delete, view, add rights to the user without additional plugins
```vb
%player%'s perm[ission][s]
```
```vb
perm[ission][s] of %player%
```
{{% expand title="Example" %}}
```vb
add "my.cool.permission" to player's perms
remove "not.cool.permission" from player's perms
broadcast "%player's perms%"
```
{{% /expand %}}
