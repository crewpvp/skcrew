+++
archetype = "default"
title = "Виды форм"
weight = 1
+++
# FORM TYPES

#### Создание новой формы
##### Modal form
Представляет собой самую простую форму, которая имеет лишь две кнопки и блок описания. Кнопки - это условный выбор 'да' или 'нет'. \
Созданная форма будет помещена в выражение [last created form <i class="fas fa-link"></i>](#последняя-созданная-форма)
```vb
create [a] [new] modal form (with (name|title)|named) %string%
```
{{% expand title="Пример" %}}
```vb
create modal form named "Modal form":
  set form content to "Please select one option"
  run on form close:
    broadcast "closed"
  run on form open:
    broadcast "opened"
  form button named "I like skript!":
    broadcast "Thank you!!!"
  form button named "I didnt like skript!":
    broadcast "USE DENIZEN INSTEAD!!!"
open last created form to player
```
![modal form](/images/modal-form.jpg)
{{% /expand %}}

##### Simple form
Представляет собой форму c кнопками. Кнопок может быть неограниченное количество, помимо этого, кнопки могут иметь изображения из интернета. \
Созданная форма будет помещена в выражение [last created form <i class="fas fa-link"></i>](#последняя-созданная-форма)
```vb
create [a] [new] simple form (with (name|title)|named) %string%
```
{{% expand title="Пример" %}}
```vb
create simple form named "Simple form":
  form button named "button 1" with image "https://pics.clipartpng.com/Carrots_PNG_Clipart-465.png":
    broadcast "button 1 pressed"
  form button named "button 2":
    broadcast "button 2 pressed"
  form button named "button 3":
    broadcast "button 3 pressed"
open last created form to player
```
![simple form](/images/simple-form.jpg)
{{% /expand %}}

##### Custom form
Представляет собой форму c любыми элементами, кроме кнопок. Данный вид форм позволяет использовать слайдеры, переключатели, поля ввода, поля выбора и т.д \
Обработка элементов формы осуществляется в секции on form result \
Созданная форма будет помещена в выражение [last created form <i class="fas fa-link"></i>](#последняя-созданная-форма)
```vb
create [a] [new] custom form (with (name|title)|named) %string%
```
{{% expand title="Пример" %}}
```vb
create custom form named "Custom form":
  form dropdown named "Select one value from list" with elements "one", "two", "three"
  form input named "Your password?" with placeholder "write your password"
  form label named "sample text"
  form slider named "music volume????" with minimum value 0 and maximum value 10
  form textslider named "select shit" with elements "value 1","value 2","value 3"
  form toggle named "yes or no?"
  run on form result:
    broadcast "%form toggle 1 value%"
open last created form to player
```
![custom form](/images/custom-form.jpg)
{{% /expand %}}

#### Последняя созданная форма
```vb
[the] (last[ly] [(created|edited)]|(created|edited)) form
```

#### Открыть форму игроку
```vb
open %form% (for|to) %players%
```