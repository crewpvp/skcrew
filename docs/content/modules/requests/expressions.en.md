+++
archetype = "default"
title = "Expressions"
weight = 1
+++
# EXPRESSIONS
#### Request header
Creates a new request header in the **key:meaning**
```vb
request (header|property) %string% %string%
```

#### Request Header Key
```vb
%request property%'s key
```
```vb
key of %request property%
```

#### The value of the request header
```vb
%request property%'s value
```
```vb
value of %request property%
```