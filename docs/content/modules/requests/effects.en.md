+++
archetype = "default"
title = "Effects"
weight = 2
+++
# EFFECTS
#### Asynchronous HTTP/S request
Executes a request with the specified method to the specified site using the specified headers and the specified request body. Returns the code and the body of the response.
```vb
[async[hronously]] request [%string%] to [url] %string% [with header[s] %request properties%] [(and|with) body %string%] [and store [[the] (body|result) in %object%] [and] [code in %object%]]
```

{{% expand title="Example" %}}
```vb
async request "GET" to url "https://crewpvp.xyz" and store the result in {_data} and code in {_code}
```
{{% /expand %}}
{{% notice style="warning" %}}
An asynchronous request cannot be used in functions where a value is returned. The result of an asynchronous request will be `<none>` 
{{% /notice %}}

#### Synchronous HTTP/S request
Executes a request with the specified method to the specified site using the specified headers and the specified request body. Returns the code and the body of the response.
```vb
sync[hronously] request [%string%] to [url] %string% [with header[s] %request properties%] [(and|with) body %string%] [and store [[the] (body|result) in %object%] [and] [code in %object%]]
```

{{% notice style="warning" %}}
Block the main thread for the duration of the request, use wisely.
{{% /notice %}}
