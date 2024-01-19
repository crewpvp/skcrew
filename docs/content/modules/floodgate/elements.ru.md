+++
archetype = "default"
title = "Элементы форм"
weight = 2
+++
# ЭЛЕМЕНТЫ ФОРМ

#### Кнопки
Кнопка представляет собой секцию, код внутри которой будет выполнен при клике на нее. После клика на кнопку - форма будет автоматически закрыта (особенность Bedrock форм).
{{% notice style="warning" %}}
Могут быть использованы только в [Modal form <i class="fas fa-link"></i>](../form-types/#modal-form) и [Simple form <i class="fas fa-link"></i>](../form-types/#simple-form). \
Кнопка с изображением может быть использована только в [Simple form <i class="fas fa-link"></i>](../form-types/#simple-form)
{{% /notice %}}
```vb
form(-| )button ((with (name|title))|named) %string% [with image %string%]
```
{{% expand title="Пример" %}}
```vb
create modal form named "Modal form":
  form button named "My button":
    broadcast "Pressed button"
open last created form to player
```
{{% /expand %}}

#### Текст Modal form.
Позволяет указать или получить текстовое описание в Modal form. Позволяет указать как в уже созданной форме, так и в [секции создания Modal form <i class="fas fa-link"></i>](../form-types/#modal-form)
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
{{% expand title="Пример" %}}
```vb
create modal form named "Modal form":
  set content of form to "Sample text"
open last created form to player
```
{{% /expand %}}
#### Элементы Custom form
Получить значение элементов Custom form после закрытия можно только в секции run on form result.
##### Элемент пользовательского ввода
Создает элемент в который пользователь может ввести любой текст. Позволяет указать начальный текст внутри поля, и текст заполнитель
```vb
form(-| )input (with name|named) [%string% (with|and) [placeholder] %string%[(, | (with|and) ) [def[ault] [value]] %string%]]
```
##### Выбор из списка
Создает элемент в котором пользователь может выбрать одно из предложенных значений. Позволяет указать изначальное значение по индексу.
```vb
form(-| )drop[(-| )]down (with name|named) %string% (with|and) [elements] %strings%[(, | (with|and) ) [def[ault] [(element [index]|index)]] %number%]
```
##### Метка, заметка, текст
Создает элемент с текстом.
```vb
form(-| )label [(with (name|title)|named)] %string%
```
##### Ползунок с числовым выбором значения
Создает элемент в виде полоски со регулятором, в котором пользователь может указать значение. Позволяет указать минимальный и максимальный порог чисел, начальное значение и шаг ползунка.
```vb
form(-| )slider (with name|named) %string% [[(with|and) [min[imum] [value]] %number%[(, | (with|and) ) [max[imum] [value]] %number%[(, | (with|and) ) [def[ault] [value]] %number%[(, | (with|and) ) [[step] [value]] %number%]]]]
```
##### Ползунок с выбором значений
Создает аналогичный числовому ползунку элемент, но уже с текстовыми значениями. Позвлояет указать начальное значение по индексу.
```vb
form(-| )(text|step)[(-| )]slider (with name|named) %string% (with|and) [elements] %strings%[(, | (with|and) ) [def[ault] [(element [index]|index)]] %number%]
```
##### Элемент переключатель.
Имеет лишь два состояния , включено или выключено. Можно указать начальное состояние.
```vb
form(-| )toggle (with name|named) %string% [(with|and) [def[ault]] [value] %boolean%]
```
