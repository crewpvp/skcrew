+++
archetype = "default"
title = "Interpretate"
weight = 5
+++

# Interpretate

#### Execute a block of code from the text
Allows you to pass a list of lines of code to execute them
```vb
evalnode %strings%
```
{{% expand title="Example" %}}
```vb
evalnode "if 1 is 2:","  broadcast ""yes"""
```
{{% /expand %}}

#### Execute a block of code from the text
Allows you to execute independent lines of code (works faster, but not as functional)
```vb
eval[uate] %strings%
```
{{% expand title="Example" %}}
```vb
evaluate "broadcast ""yes"""
```
{{% /expand %}}