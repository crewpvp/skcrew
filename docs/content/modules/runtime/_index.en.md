+++
archetype = "default"
title = "RUNTIME"
weight = 3
+++

This module allows you to get information about the load and some technical information about the equipment.

#### Run the command in the operating system
Executes a command in the console/terminal of the operating system.
```vb
[execute] [the] system command %strings%
```
```vb
(let|make) system execute [[the] command] %strings%
```

#### The amount of allocated RAM per process in bytes
```vb
[the] [server['s]] max memory
```
```vb
max memory of server
```

##### The amount of RAM used by the process in bytes
```vb
[the] [server['s]] used memory
```
```vb
used memory of server
```

#### Server uptime (process lifetime)
```vb
[the] server['s] uptime
```
```vb
[the] uptime of server
```

#### The number of processor cores installed on the server
```vb
processor['s] cores amount
```
```vb
[amount of] processor cores
```

#### The load factor of the server by this process
```vb
[the] process['s] [average] load
```
```vb
[average] load of process
```

#### Server load factor
```vb
[the] (system|cpu)['s] [average] load
```
```vb
[average] load of (system|cpu)
```

