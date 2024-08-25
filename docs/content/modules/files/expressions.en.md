+++
archetype = "default"
title = "Expressions"
weight = 1
+++
# EXPRESSIONS
#### Get a link to an abstract file
In fact, you get either a real file, if there is one, or an abstract file that can then be created.
```vb
[the] (file[s]|dir[ector(y|ies)]) %strings%
```
{{% expand title="Example" %}}
```vb
set {_file} to file "eula.txt"
```
{{% /expand %}}
#### Get an absolute file
Absolute - that is, the one with the path starting from the root of the file system
```vb
absolute [(file|dir[ectory])] of %path%
```
```vb
%path%'s absolute [(file|dir[ectory])] 
```
{{% expand title="Example" %}}
```vb
set {_file} to absolute file of file "eula.txt"
```
{{% /expand %}}
#### Get all files inside a directory
It also allows you to do this recursively, getting files in the directory and in all internal directories
{{% expand title="Patterns" %}}
```vb
all [the] files and [all] [the] dir[ectorie]s (in|of|from) %path%
```
```vb
all [the] files (in|of|from) %path%
```
```vb
all [the] dir[ectorie]s (in|of|from) %path%
```
```vb
all [the] sub[(-| )]files and [all] [the] sub[(-| )]dir[ectorie]s (in|of|from) %path%
```
```vb
all [the] sub[(-| )]dir[ectorie]s (in|of|from) %path%
```
```vb
all [the] sub[(-| )]files (in|of|from) %path%
```
```vb
glob (files|dir[ectorie]s) %string% (in|of|from) %path%
```
{{% /expand %}}
{{% expand title="Example" %}}
```vb
loop all files from file "plugins/":
  broadcast "%loop-value%"
```
{{% /expand %}}
#### Get the text content of the file
```vb
[the] content of %path%
```
```vb
[the] %path%'s content
```
{{% expand title="Example" %}}
```vb
set {_file} to content of file "eula.txt"
replace all "false" with "true" in {_file}
set content of file "eula.txt" to {_file}
```
{{% /expand %}}
#### Get the name of a file or directory
```vb
[the] (file|dir[ectory])name of %path%
```
```vb
[the] %path%'s (file|dir[ectory])name
```
{{% expand title="Example" %}}
```vb
loop all files from file "plugins/":
  broadcast filename of loop-value
```
{{% /expand %}}
#### Read a line/lines of a file or change them
```vb
[the] line %number% (from|of|in) %path%
```
```vb
[all] [the] lines (from|of|in) %path%
```
{{% expand title="Example" %}}
```vb
set line 1 of file "eula.txt" to "eula=true"
```
{{% /expand %}}
#### Get the directory where the file/folder is located
```vb
parent [(file|dir[ectory])] of %path%
```
```vb
%path%'s parent [(file|dir[ectory])]
```
{{% expand title="Example" %}}
```vb
set {_file} to parent of file "eula.txt"
```
{{% /expand %}}

#### Get file size in bytes
```vb
file size of %path%
```
```vb
%path%'s file size
```

#### Get the last access date of a file
```vb
[last] access (date|time) of %path%
```
```vb
%path%'s [last] access (date|time)
```

#### Get the last modified date of a file
```vb
[last] modified (date|time) of %path%
```
```vb
%path%'s [last] modified (date|time)
```

#### Get file creation date
```vb
create[d] (date|time) of %path%
```
```vb
%path%'s create[d] (date|time)
```