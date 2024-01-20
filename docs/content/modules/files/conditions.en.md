+++
archetype = "default"
title = "Conditions"
weight = 2
+++
# CONDITIONS
#### Checking the file for its actual existence
```vb
%path% (is|does)[(n't| not)] exist[s]
```
{{% expand title="Example" %}}
```vb
if file "eula.txt" is exists:
  broadcast "Yea, file eula.txt is exists"
```
{{% /expand %}}
#### Checking that the file is a directory
```vb
%path% is[(n't| not)] dir[ectory]
```
{{% expand title="Example" %}}
```vb
if file "eula.txt" is not directory:
  broadcast "Yea, file eula.txt is not directory, it is regular file"
```
{{% /expand %}}
#### Checking that the file is a file
```vb
%path% is[(n't| not)] file
```
{{% expand title="Example" %}}
```vb
if file "eula.txt" is file:
  broadcast "Yea, file eula.txt is regular file"
```
{{% /expand %}}