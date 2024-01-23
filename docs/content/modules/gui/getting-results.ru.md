+++
archetype = "default"
title = "Обработка результатов"
weight = 3
+++
# ОБРАБОТКА РЕЗУЛЬТАТОВ
#### Открытое GUI у игрока
Возвращает открытое в текущий момент GUI у игрока
```vb
%players%'s gui
```
```vb
gui of %player%'s
```

#### Проверить открыто или нет какое-либо GUI у игрока
```vb
%players% (has|have) a gui [open]
```
```vb
%players% do[es](n't| not) have a gui [open]
```

#### Следующий слот GUI
Возвращает номер или букву шаблона следующего слота GUI
```vb
%guis%'[s] next gui slot[s]
```
```vb
[the] next gui slot[s] of %guis%
```
{{% notice style="warning" %}}
Последующие выражения могут быть использованы только в секциях [создания GUI <i class="fas fa-link"></i>](../gui-creation/#создание-нового-gui) и [создания интерактивного элемента <i class="fas fa-link"></i>](../gui-creation/#изменить-уже-созданное-gui)
{{% /notice %}}
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

#### Инвентарь, редактируемого GUI
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

{{% notice style="warning" %}}
Последующие выражения могут быть использованы только в секции [закрытия GUI <i class="fas fa-link"></i>](../gui-creation/#при-закрытии-gui)
{{% /notice %}}

#### Отменить закрытие
Запрещает игроку закрыть GUI, переоткрывая его с сохранением параметров GUI
```vb
cancel [the] gui clos(e|ing)
```

#### Разрешить закрытие
Разрешает игроку закрыть GUI
```vb
uncancel [the] gui close(e|ing)
```