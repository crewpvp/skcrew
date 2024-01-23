+++
archetype = "default"
title = "Effects"
weight = 2
+++
# EFFECTS
#### Асинхронный HTTP/S запрос
Выполняет запрос с заданным методом к заданному сайту, используя заданные заголовки и заданное тело запроса. Возвращает код и тело ответа.
```vb
[async[hronously]] request [%string%] to [url] %string% [with header[s] %request properties%] [(and|with) body %string%] [and store [[the] (body|result) in %object%] [and] [code in %object%]]
```

{{% expand title="Пример" %}}
```vb
async request "GET" to url "https://crewpvp.xyz" and store the result in {_data} and code in {_code}
```
{{% /expand %}}
{{% notice style="warning" %}}
Асинхронный запрос нельзя использовать в функциях, где возвращается значение. Результатом асинхронного запроса будет `<none>` 
{{% /notice %}}

#### Синхронный HTTP/S запрос
Выполняет запрос с заданным методом к заданному сайту, используя заданные заголовки и заданное тело запроса. Возвращает код и тело ответа.
```vb
sync[hronously] request [%string%] to [url] %string% [with header[s] %request properties%] [(and|with) body %string%] [and store [[the] (body|result) in %object%] [and] [code in %object%]]
```

{{% notice style="warning" %}}
Приостанавливает основной поток на время выполнения запроса, используйте с умом.
{{% /notice %}}
