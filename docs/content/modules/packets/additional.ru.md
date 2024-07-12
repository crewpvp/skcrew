+++
archetype = "default"
title = "Дополнительно"
weight = 3
+++
# ДОПОЛНИТЕЛЬНО

#### Получить ID сущности
Возвращает уникальный идентификатор сущности
```vb
entity id of %entity%
```
```vb
%entity%'s entity id
```

#### Получить или изменить Skin value игрока
```vb
skin value of %player%   
```
```vb
%player%'s skin value
```

#### Получить или изменить Skin signature игрока
```vb
skin signature of %player%   
```
```vb
%player%'s skin signature
```

#### Очистить signature и value скина игрока
```vb
delete skin properties of %player%
```
```vb
delete %player%'s skin properties
```

#### Получить байты текста
Возвращает список байтов.\
Возможно также указать необходимую кодировку. По умолчанию `UTF-8`
```vb
bytes of %string% [with charset %string%]
```
```vb
%string%'s bytes [with charset %string%]
```

#### Создать Bundle
Это пакет, который позволяет создать последовательность из пакетов и разом отправить их на клиент, что обеспечивает большую скорость и стабильность.\
Если вы отправляете за раз множество пакетов, помещайте их в Bundle.
```vb
bundle packet (from|of|with) %packets%
```