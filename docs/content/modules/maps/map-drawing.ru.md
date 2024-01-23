+++
archetype = "default"
title = "Рисование на холсте"
weight = 2
+++
# Рисование на холсте

#### Получить цвет пикселя на холсте
```vb
pixel %number%(,[ ]| )%number% of %map%
```
{{% notice style="warning" %}}
Не позволяет изменить цвет пикселя, для этого используйте выражение [закрасить пиксель <i class="fas fa-link"></i>](#закрасить-пиксель)
{{% /notice %}}
#### Закрасить пиксель
Позволяет задать пикселю определенный цвет по его координатам.
Первые два аргумента указывают на координаты на холсте, последующие цвет в формате RGB или RGBA, и последний - это холст.
```vb
draw pixel [at] %number%(,[ ]| )%number% [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```
{{% expand title="Пример" %}}
```vb
set {_map} to new drawing map
draw pixel at 1,1 with color 255,0,0 on map {_map}
# will draw pixel at 1,1 with pure red color
```
{{% /expand %}}
#### Закрасить пиксели
Позволяет установить пикселям в диапазоне определенный цвет.\
Первые четыре аргумента указывают на координаты на холсте, последующие цвет в формате RGB или RGBA, и последний - это холст.
```vb
draw pixels (from|between) %number%(,[ ]| )%number% [to] %number%(,[ ]| )%number% [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```
{{% expand title="Пример" %}}
```vb
set {_map} to new drawing map
draw pixels from 10,10 to 50,50 with color 255,0,0 on map {_map}
# will draw pure red square on map
```
{{% /expand %}}

#### Нарисовать линию
Принимает аналогичные аргументы с закрашиванием пикселей.
```vb
draw line (from|between) %number%(,[ ]| )%number% [to] %number%(,[ ]| )%number% [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```

#### Нарисовать кривую безье
Первые четыре аргумента - точки, между которыми будет основная часть кривой. Последующие четыре аргумента позволяют опционально указать контрольные точки для смещения кривой. Последующие аргументы аналогичны рисованию линии или закраски пискелей.
```vb
draw bezier curve (from|between) %number%(,[ ]| )%number% [to] %number%(,[ ]| )%number% [[with] control points] %number%(,[ ]| )%number% [and] %number%(,[ ]| )%number% [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```

#### Нарисовать круг
Первые два аргумента - это центр круга, последующий - радиус круга. Остальные аргументы являются цветом и холстом.
```vb
draw circle at %number%(,[ ]| )%number% with radius %number% [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```

#### Нарисовать изображение
Изображение должно находиться в папке `images/` аддона. Принимает первым аргументом название файла с изображением.
```vb
draw image %string% on [[the] map] %map%
```

#### Написать текст на карте
Первый аргумент - текст, который должен быть написан, последующие два аргумента описывают название шрифта и размер шрифта.
```vb
draw text %string% [at] %number%(,[ ]| )%number% [with font %-string%] [with size %-number%] [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```