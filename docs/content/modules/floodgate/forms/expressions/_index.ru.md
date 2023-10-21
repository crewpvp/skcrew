+++
archetype = "default"
title = "EXPRESSIONS"
weight = 2
+++

#### Последнее созданное GUI
Возвращает последнее созданное/измененное GUI (com.lotzy.skcrew.spigot.gui.GUI class)
```vb
[the] last[ly] [(created|edited)] gui
```

#### Открытое GUI у игрока
Возвращает открытое в текущий момент GUI у игрока
```vb
%players%'s gui
```
```vb
gui of %player%'s
```

#### Следующий слот GUI
Возвращает номер или букву шаблона следующего слота GUI
```vb
%guis%'[s] next gui slot[s]
```
```vb
[the] next gui slot[s] of %guis%
```
\
{{% notice style="warning" %}}
Последующие выражения могут быть использованы только в секциях [Создания GUI](../sections/#создание-нового-gui) и [Создания интерактивного элемента](../sections/#изменить-уже-созданное-gui)
{{% /notice %}}
</br>
```vb
[the] next gui slot
```

#### Название инвентаря GUI
```vb
%gui%'s gui(-| )name
```
```vb
gui(-| )name of %gui%
```

#### Размер инвентаря GUI
```vb
%gui%'s gui(-| )size
```
```vb
gui(-| )size of %gui%
```

#### Шаблон GUI
```vb
%gui%'s gui(-| )shape
```
```vb
gui(-| )shape of %gui%
```

#### Возможность брать предметы в GUI
```vb
%gui%'s gui(-| )lock(-| )status
```
```vb
gui(-| )lock(-| )status of %gui%
```

#### Текущее, редактируемое GUI
```vb
[the] gui
```

#### Кликнутый слот
```vb
[the] gui(-| )raw(-| )slot
```

#### Горячая клавиша кликнутого слота
```vb
[the] gui(-| )hotbar(-| )slot
```

#### Инветарь, редактируемого GUI
```vb
[the] gui(-| )inventory
```

#### Действие внутри GUI
Например, игрок сделал двойной клик, из-за чего вещи собрались в слоте курсора.
```vb
[the] gui(-| )inventory(-| )action
```

#### Тип клика
Например, игрок сделал клик с зажатой клавишей Shift.
```vb
[the] gui(-| )click(-| )(type|action)
```

#### Слот курсора
```vb
[the] gui(-| )cursor[(-| )item]
```

#### Тип кликнутого слота
```vb
[the] gui(-| )slot(-| )type
```

#### Кликнутый предмет
```vb
[the] gui[(-| )(clicked|current)](-| )item
```

#### Выражение используемое вместо игрока внутри секции GUI
```vb
[the] gui(-| )player
```
{{% notice style="warning" %}}
Обязательно используйте это выражение внутри секции создания GUI или интерактивного элемента вместо **player**, иначе ваш код будет работать не так, как вы это предполагаете.
{{% /notice %}}

#### Игроки у которых открыто GUI
```vb
[the] gui(-| )(viewer|player)s
```

#### ID кликнутого слота
```vb
[the] gui(-| )slot(-| )id
```