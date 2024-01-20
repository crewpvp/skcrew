+++
archetype = "default"
title = "Bitwise"
weight = 2
+++
# BITWISE
This module allows you to perform logical and bitwise operations with numbers.
{{% notice style="note" %}}
Special thanks to the [Pesekjak <i class="fas fa-link"></i>](https://github.com/Pesekjak) for creating an addon [Bitshift <i class="fas fa-link"></i>](https://github.com/Pesekjak/Bitshift). Part of the code and the idea was borrowed from him.
{{% /notice %}}

#### Binary operation **OR**
Copies a bit to the result if it exists in both operands.
```vb
%number% | %number%
```

#### Logical operation **OR**
If at least one is **True**, then the truth will be returned.
```vb
%boolean% || %boolean%
```

#### Binary operation **AND**
Copies a bit if it exists in any of the operands.
```vb
%number% & %number%
```

#### Logical operation **AND**
If both operands are **True**, then true will be returned.
```vb
%boolean% && %boolean%
```

#### Binary operation **XOR**
Copies a bit if it is set in one operand, but not in both.
```vb
%number% ^^ %number%
```

#### Bitwise shift to the left
The value of the left operands moves to the left by the number of bits specified by the right operand.
```vb
%number% << %number%
```

#### Bitwise shift to the right
The value of the right operands moves to the right by the number of bits specified by the left operand.
```vb
%number% >> %number%
```

#### Bitwise zero shift to the right
The value of the left operands is moved to the right by the number of bits specified by the right operand, and the shifted values are filled with zeros.
```vb
%number% >>> %number%
```

#### Binary complement operation (reflection)
Each bit of the number will be inverted.
```vb
~~%number% 
```

#### A number in binary notation
```vb
0(b|B)<[0-1]+>
```
{{% expand title="Example" %}}
```vb
set {_binary} to 0b22
```
{{% /expand %}}
#### A number in the hexadecimal system
```vb
0(x|X)<[A-Fa-f0-9]+>
```
{{% expand title="Example" %}}
```vb
set {_hexadecimal} to 0xFF
```
{{% /expand %}}


