+++
archetype = "default"
title = "Обработка результатов"
weight = 3
+++
# Обработка результатов
#### Получить игрока внутри формы
{{% notice style="note" %}}
Используйте данное выражение для получения игрока внутри формы, вместо привычного player или каких-либо переменных.
{{% /notice %}}
```vb
form(-| )player
```

#### Получение типа созданной формы
```vb
form[(-| )]type of %form%
```
```vb
%form%'s form[(-| )]type
```
Доступные типы форм для сравнения:
- custom form
- modal form
- simple form

#### Выполнить код при открытии/закрытии формы
```vb
run (when|while) (open[ing]|clos(e|ing)) [[the] form]
```
```vb
run (when|while) [the] form (opens|closes)
```
```vb
run on form (open[ing]|clos(e|ing))
```
{{% expand title="Пример" %}}
```vb
create modal form named "Modal form":
  run on form close:
  	broadcast "%formplayer%" #will show name of player what close form
open last created form to player
```
{{% /expand %}}
#### Отменить или разрешить закрытие формы
По умолчанию закрытие формы разрешено. Если запретить, то форма будет переоткрыта после выбора
```vb
cancel [the] form clos(e|ing)
```
```vb
uncancel [the] form clos(e|ing)
```
#### Получить причину закрытия формы
{{% notice style="note" %}}
Данное выражение может быть использовано только в [секции при закрытии формы <i class="fas fa-link"></i>](./getting-results#выполнить-код-при-открытиизакрытии-формы)
{{% /notice %}}
```vb
[form(-| )]close reason
```
Доступные причины закрытия для сравнения:
- close
- (submit|success)
- invalid[ response]

#### Выполнить код при успешном закрытии формы
```vb
run on form (result|submit)
```
{{% expand title="Пример" %}}
```vb
create custom form named "Custom form":
  form toggle named "toggle value"
  run on form result:
    broadcast "%form toggle 1 value%"
open last created form to player
```
{{% /expand %}}

#### Элементы Custom form
{{% notice style="note" %}}
Данное выражение может быть использовано только в [секции при успешном закрытии формы <i class="fas fa-link"></i>](./getting-results#выполнить-код-при-успешном-закрытии-формы)
{{% /notice %}}
```vb
[form[(-| )]](drop[(-| )]down|input|slider|step[(-| )]slider|toggle) %number% [value]
```
```vb
value of [form[(-| )]](drop[(-| )]down|input|slider|step[(-| )]slider|toggle) %number%
```

{{% notice style="note" %}}
Для обработки кнопок [Modal form <i class="fas fa-link"></i>](../form-types/#modal-form) и [Simple form <i class="fas fa-link"></i>](../form-types/#simple-form) используйте [секцию создания кнопки <i class="fas fa-link"></i>](../elements/#кнопки) 
{{% /notice %}}
