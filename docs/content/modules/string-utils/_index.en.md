+++
archetype = "default"
title = "String utils"
weight = 13
+++

# STRING UTILS

#### Check that the text matches the regular expression
```vb
%string% (does|is)[n't| not] regex match(es|ed) %string%
```
{{% expand title="Example" %}}
```vb
if "1361" is regex matches "1..1":
  broadcast "Matched"
```
{{% /expand %}}

#### Find groups by regular expression
```vb
regex group[s] %integer% of %string% matched to %string%
```
{{% expand title="Example" %}}
```vb
set {_group::*} to regex group 1 of "123123123123" matched to "123"
```
{{% /expand %}}

#### Regular expression substitution
```vb
regex replace %string% with %string% in %string%
```
{{% expand title="Example" %}}
```vb
set {_text} to regex replace "3.*" with "" in "123123123123"
```
{{% /expand %}}

#### Split text by regular expression
```vb
regex split %string% at %string%
```
{{% expand title="Example" %}}
```vb
set {_text::*} to regex split "123123123123" at ".3"
```
{{% /expand %}}

#### Mirror the text
```vb
(reverse[d]|backward(s|ed)) [(string|text)] %string%
```
{{% expand title="Example" %}}
```vb
set {_text} to reversed "321"
```
{{% /expand %}}

#### Split the string every N characters
```vb
%string% split [(by|at)] every %integer% (symbol|char[acter])[s]
```
```vb
split %string% [(by|at)] every %integer% (symbol|char[acter])[s]
```
{{% expand title="Example" %}}
```vb
set {_text::*} to "123123123123" split eveny 3 symbols
```
{{% /expand %}}