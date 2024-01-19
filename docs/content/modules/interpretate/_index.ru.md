+++
archetype = "default"
title = "Interpretate"
weight = 5
+++

# Interpretate

#### Выполнить блок кода из текста
Позволяет передать список строк кода чтобы их выполнить
```vb
evalnode %strings%
```
{{% expand title="Пример" %}}
```vb
evalnode "if 1 is 2:","  broadcast ""yes"""
```
{{% /expand %}}

#### Выполнить блок кода из текста
Позволяет выполнить независимые строки кода (работает быстрее, но не столь функционально)
```vb
eval[uate] %strings%
```
{{% expand title="Пример" %}}
```vb
evaluate "broadcast ""yes"""
```
{{% /expand %}}