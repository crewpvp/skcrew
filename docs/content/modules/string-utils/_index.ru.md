+++
archetype = "default"
title = "String utils"
weight = 13
+++

# STRING UTILS

#### Проверить, что текст соответствует регулярному выражению
```vb
%string% (does|is)[n't| not] regex match(es|ed) %string%
```
{{% expand title="Пример" %}}
```vb
if "1361" is regex matches "1..1":
  broadcast "Matched"
```
{{% /expand %}}

#### Найти группы по регулярному выражению
```vb
regex group[s] %integer% of %string% matched to %string%
```
{{% expand title="Пример" %}}
```vb
set {_group::*} to regex group 1 of "123123123123" matched to "123"
```
{{% /expand %}}

#### Замена по регулярному выражению
```vb
regex replace %string% with %string% in %string%
```
{{% expand title="Пример" %}}
```vb
set {_text} to regex replace "3.*" with "" in "123123123123"
```
{{% /expand %}}

#### Разбить текст по регулярному выражению
```vb
regex split %string% at %string%
```
{{% expand title="Пример" %}}
```vb
set {_text::*} to regex split "123123123123" at ".3"
```
{{% /expand %}}

#### Отзеркалить текст
```vb
(reverse[d]|backward(s|ed)) [(string|text)] %string%
```
{{% expand title="Пример" %}}
```vb
set {_text} to reversed "321"
```
{{% /expand %}}

#### Разбить строку каждые N символов
```vb
%string% split [(by|at)] every %integer% (symbol|char[acter])[s]
```
```vb
split %string% [(by|at)] every %integer% (symbol|char[acter])[s]
```
{{% expand title="Пример" %}}
```vb
set {_text::*} to "123123123123" split eveny 3 symbols
```
{{% /expand %}}