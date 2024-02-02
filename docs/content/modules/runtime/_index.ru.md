+++
archetype = "default"
title = "Runtime"
weight = 11
+++
# RUNTIME 
Данный модуль позволяет получать информацию о нагрузке и некоторые технические сведения об оборудовании.

#### Выполнить команду в операционной системе
Выполняет команду в консоли/терминале операционной системы.
```vb
[execute] [the] system command %strings%
```
```vb
(let|make) system execute [[the] command] %strings%
```

#### Количество выделенной оперативной памяти на процесс в байтах
```vb
[the] [server['s]] max memory
```
```vb
max memory of server
```

##### Количество используемой оперативной памяти процессом в байтах
```vb
[the] [server['s]] used memory
```
```vb
used memory of server
```

#### Ап-тайм сервера (время жизни процесса)
```vb
[the] server['s] uptime
```
```vb
[the] uptime of server
```

#### Количество ядер процессора, установленного на ЭВМ
```vb
processor['s] cores amount
```
```vb
[amount of] processor cores
```

#### Коэффициент нагрузки ЭВМ этим процессом
```vb
[the] process['s] [average] load
```
```vb
[average] load of process
```

#### Коэффициент нагрузки ЭВМ
```vb
[the] (system|cpu)['s] [average] load
```
```vb
[average] load of (system|cpu)
```

