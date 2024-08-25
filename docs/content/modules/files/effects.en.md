+++
archetype = "default"
title = "Effects"
weight = 3
+++
# EFFECTS
#### Copy a file or directory
It also allows you to specify a parameter for overwriting if the file already exists
```vb
copy %path% to %path% [with (replac|overwrit)(e|ing)]
```
{{% expand title="Example" %}}
```vb
copy file "plugins/Skcrew.jar" to file "./"
```
{{% /expand %}}
#### Create a simple file
It also allows you to specify the text content of the file being created
```vb
create %paths% [with [(text|string|content)] %strings%]
```
{{% expand title="Example" %}}
```vb
create file "eula.txt" with content "eula=true"
```
{{% /expand %}}
#### Delete a file or directory
```vb
delete %paths%
```
{{% expand title="Example" %}}
```vb
delete file "plugins/Skcrew.jar"
```
{{% /expand %}}
#### Move a file or directory
It also allows you to specify a parameter for overwriting if the file already exists
```vb
move %paths% to %path% [with (overwrit|replac)(e|ing)]
```
{{% expand title="Example" %}}
```vb
move file "plugins" to file "disabled_plugins"
```
{{% /expand %}}
#### Rename a file or directory
It also allows you to specify a parameter for overwriting if the file already exists
```vb
rename %paths% to %string% [with (overwrit|replac)(e|ing)]
```
{{% expand title="Example" %}}
```vb
rename file "plugins/Skript/scripts/mycoolscript.sk" to "-mycoolscript.sk"
```
{{% /expand %}}
#### Unpack the zip archive
It also allows you to specify a parameter for overwriting if the file already exists
```vb
unzip %path% to %path% [with (overwrit|replac)(e|ing)]
```
{{% expand title="Example" %}}
```vb
unzip file "myarchive.zip" to file "./"
```
{{% /expand %}}
#### Create a zip archive
It also allows you to specify a parameter for overwriting if the file already exists
```vb
zip %paths% to %path% [with (overwrit|replac)(e|ing)]
```
{{% expand title="Example" %}}
```vb
zip file "world/" to file "backupworld.zip"
```
{{% /expand %}}

