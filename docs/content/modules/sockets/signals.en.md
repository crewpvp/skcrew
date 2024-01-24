+++
archetype = "default"
title = "Signals"
weight = 5
+++
# SIGNALS
Signals allow you to transfer the information you need between servers.
#### Create a signal
The signal has the form `key : values', which allows you to transfer any data that can be serialized.
```vb
signal [(with key|keyed)|(with name|named)] %string% (and|with) [data] %objects%
```
{{% expand title="Example" %}}
```vb
set {_signal} to signal named "broadcast" with data "Hello world!","My name is Bjork."
```
{{% /expand %}}

#### Get the signal key.
```vb
(key|signal name) of %signal%
```
```vb
%signal%'s (signal name|key)
```

#### Get signal data
Returns a list of objects
```vb
data of %signal%
```
```vb
%signal%'s data
```

#### Send a signal
Allows you to send a signal to any connected server
```vb
send signal %signals% to %servers%
```
{{% expand title="Example" %}}
```vb
set {_signal} to signal named "broadcast" with data "Hello world!","My name is Bjork."
send signal {_signal} to all servers
```
{{% /expand %}}

#### Signal receiving event
It also allows you to track signals by key.
```vb
signal [(with key|keyed) %string%]
```
The event has built-in data, using the `event-signal` you can receive the incoming signal.
{{% expand title="Example" %}}
```vb
on load:
  set {_signal} to signal named "broadcast" with data "Hello world!","My name is Bjork."
  send signal {_signal} to all servers
  
on signal with key "broadcast":
  send data of event-signal to all players, console
```
{{% /expand %}}
