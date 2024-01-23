+++
archetype = "default"
title = "Effects"
weight = 3
+++
# EFFECTS

#### Кикнуть игрока с прокси сервера
Позволяет указать причину. Причина может быть простым текстом, или компонентом AdventureAPI ( [Velocity <i class="fas fa-link"></i>](https://docs.advntr.dev/serializer/index.html) ) или ChatComponentAPI ( [Bungeecord <i class="fas fa-link"></i>](https://www.spigotmc.org/wiki/the-chat-component-api/) ) в формате JSON. 
```vb
kick %offlineplayers% from proxy [(by reason of|because [of]|on account of|due to) %string%]
```

#### Переместить игрока на сервер
Позволяет сменить сервер игрока, если он находится на сервере, который подключен к прокси серверу
```vb
switch %offlineplayers% to %server%
```


