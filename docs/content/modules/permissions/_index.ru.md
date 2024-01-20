+++
archetype = "default"
title = "Permissions"
weight = 8
+++

# PERMISSIONS

#### Выполнить команду с правами оператора
```vb
[execute] [the] command %strings% [by %-commandsenders%] as op
```
```vb
[execute] [the] %commandsenders% command %strings% as op
```
```vb
(let|make) %commandsenders% execute [[the] command] %strings% as op
```

#### Управление правами пользователя
Позволяет удалять, просматривать, добавлять пользователю права без наличия дополнительных плагинов
```vb
%player%'s perm[ission][s]
```
```vb
perm[ission][s] of %player%
```
{{% expand title="Пример" %}}
```vb
add "my.cool.permission" to player's perms
remove "not.cool.permission" from player's perms
broadcast "%player's perms%"
```
{{% /expand %}}
