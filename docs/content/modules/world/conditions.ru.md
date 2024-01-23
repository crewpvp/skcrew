+++
archetype = "default"
title = "Conditions"
weight = 3
+++
# Conditions
#### Проверить, что чанк загружен
```vb
%chunk% [(does|is)][(n't| not)] load[ed]
```

#### Проверить, наличие мира
```vb
world %string% [(does|is)][(n't| not)] (exist[s]|available)
```