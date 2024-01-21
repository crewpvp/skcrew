+++
archetype = "default"
title = "Form Elements"
weight = 2
+++
# FORM ELEMENTS

#### Buttons
The button is a section, the code inside of which will be executed when you click on it. After clicking on the button, the form will be automatically closed (a feature of Bedrock forms).
{{% notice style="warning" %}}
Can only be used in [Modal form <i class="fas fa-link"></i>](../form-types/#modal-form) and [Simple form <i class="fas fa-link"></i>](../form-types/#simple-form). \
The button with the image can only be used in [Simple form <i class="fas fa-link"></i>](../form-types/#simple-form)
{{% /notice %}}
```vb
form(-| )button ((with (name|title))|named) %string% [with image %string%]
```
{{% expand title="Example" %}}
```vb
create modal form named "Modal form":
  form button named "My button":
    broadcast "Pressed button"
open last created form to player
```
{{% /expand %}}

#### Text Modal form.
Allows you to specify or get a text description in Modelform. Allows you to specify both in an already created form and in the [creation section Modal form <i class="fas fa-link"></i>](../form-types/#modal-form)
```vb
form['s] content
```
```vb
content of form 
```
```vb
%form%['s] content
```
```vb
content of %form%
```
{{% expand title="Example" %}}
```vb
create modal form named "Modal form":
  set content of form to "Sample text"
open last created form to player
```
{{% /expand %}}
#### Custom form Elements
Get the value of the **Custom form Elements** after closing, it is possible only in the section **run on form result.**
##### User Input element
Creates an element in which the user can enter any text. Allows you to specify the initial text inside the field, and the placeholder text
```vb
form(-| )input (with name|named) [%string% (with|and) [placeholder] %string%[(, | (with|and) ) [def[ault] [value]] %string%]]
```
##### Choosing from the list
Creates an element in which the user can select one of the suggested values. Allows you to specify the initial value by index.
```vb
form(-| )drop[(-| )]down (with name|named) %string% (with|and) [elements] %strings%[(, | (with|and) ) [def[ault] [(element [index]|index)]] %number%]
```
##### Label, note, text
Creates an element with text.
```vb
form(-| )label [(with (name|title)|named)] %string%
```
##### A slider with a numeric value selection
Creates an element in the form of a strip with a control in which the user can specify a value. Allows you to specify the minimum and maximum threshold of numbers, the initial value and the slider step.
```vb
form(-| )slider (with name|named) %string% [[(with|and) [min[imum] [value]] %number%[(, | (with|and) ) [max[imum] [value]] %number%[(, | (with|and) ) [def[ault] [value]] %number%[(, | (with|and) ) [[step] [value]] %number%]]]]
```
##### A slider with a selection of values
Creates an element similar to the numeric slider, but with text values. Allows you to specify the initial value by index.
```vb
form(-| )(text|step)[(-| )]slider (with name|named) %string% (with|and) [elements] %strings%[(, | (with|and) ) [def[ault] [(element [index]|index)]] %number%]
```
##### The switch element.
It has only two states, on or off. You can specify the initial state.
```vb
form(-| )toggle (with name|named) %string% [(with|and) [def[ault]] [value] %boolean%]
```
