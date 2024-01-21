+++
archetype = "default"
title = "FORM TYPES"
weight = 1
+++
# FORM TYPES

#### Creating a new form
##### Modal form
It is the simplest form, which has only two buttons and a description block. The buttons are a conditional choice of 'yes' or 'no'. \
The created form will be placed in the expression [last created form <i class="fas fa-link"></i>](./#последняя-созданная-форма)
```vb
create [a] [new] modal form (with (name|title)|named) %string%
```
{{% expand title="Example" %}}
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
It is a form with buttons. There can be an unlimited number of buttons, in addition, the buttons can have images from the Internet. \
The created form will be placed in the expression [last created form <i class="fas fa-link"></i>](./#последняя-созданная-форма)
```vb
create [a] [new] simple form (with (name|title)|named) %string%
```
{{% expand title="Example" %}}
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
It is a form with any elements except buttons. This type of forms allows you to use sliders, switches, input fields, selection fields, etc. \
The processing of the form elements is carried out in the section **on form result** \
The created form will be placed in the expression [last created form <i class="fas fa-link"></i>](./#последняя-созданная-форма)
```vb
create [a] [new] custom form (with (name|title)|named) %string%
```
{{% expand title="Example" %}}
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

#### The last form created
```vb
[the] (last[ly] [(created|edited)]|(created|edited)) form
```

#### Open the form to the player
```vb
open %form% (for|to) %players%
```