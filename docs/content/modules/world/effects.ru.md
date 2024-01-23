+++
archetype = "default"
title = "Effects"
weight = 1
+++
# EFFECTS
#### Загрузить мир
Загружает мир по его названию. Мир должен находиться в корневой директории сервера.
```vb
load world %string%
```

#### Выгрузить мир
Выгрузить мир. Так же позволяет не сохранять изменения при выгрузке.
```vb
unload [world] %world% [without save]
```

#### Загрузить чанк
Позволяет так же не генерировать мир в чанке, если данный чанк не был сгенерирован.
```vb
load %chunk% [without gen[erate]]
```

#### Выгрузить чанк
Выгрузить чанк. Так же позволяет не сохранять изменения при выгрузке.
```vb
unload [chunk] %chunk% [without save]
```

#### Создать мир
Позволяет так же создать плосский мир.
```vb
create world %string% [[with type] [super]flat]
```

#### Удалить мир
```vb
delete world %world/string%
```

#### Создать копию мира
```vb
(copy|duplicate) world %string% [(with|using)] name[d] %string%
```
