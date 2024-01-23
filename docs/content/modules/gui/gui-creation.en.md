+++
archetype = "default"
title = "Creating a GUI"
weight = 1
+++
# Creating a GUI
#### Creating a new GUI
Creates a new GUI and puts it in the result of the expression [last created GUI <i class="fas fa-link"></i>](./gui-creation#последнее-созданное-gui)
```vb
create [a] [new] gui with %inventory% [(and|with) ([re]move[e]able|stealable) items] [(and|with) shape %strings%]
```
{{% expand title="Example" %}}
```vb
create a gui with chest inventory with 3 rows named "My GUI":
  #do some stuff
open last created gui to player
```
![empty gui](/images/empty-gui.jpg)
{{% /expand %}}

The last argument allows you to use prepared templates for the arrangement of interactive elements.
For example, if we open the funnel inventory, we can specify the template "xxixx", and then, when creating the element "x", the first, second, fourth and fifth slots will be with this element.
{{% expand title="Example" %}}
```vb
create a gui with chest inventory with 3 rows named "My GUI" with shape "xxxxxxxxx","x-------x","xxxxxxxxx":
  make gui slot "x" with dirt
open last created gui to player
```
![gui shape](/images/gui-shape.jpg)
{{% /expand %}}
#### Edit an already created GUI
Allows you to redefine interactive elements inside an already created GUI
```vb
(change|edit) [gui] %gui%
```

#### Create an interactive element
When you click on this element, the code inside the section will be executed.
The expression below creates an interactive element on the next empty inventory slot.
```vb
(make|format) [the] next gui [slot] (with|to) [([re]mov[e]able|stealable)] %itemtype%
```
The following expression allows you to specify the value from the template or the slot number to create the element.
```vb
(make|format) gui [slot[s]] %strings/numbers% (with|to) [([re]mov[e]able|stealable)] %itemtype%
```

{{% expand title="Example" %}}
```vb
create a gui with chest inventory with 3 rows named "My GUI":
  make gui slot 1 with stone named "Click for hello world!":
    broadcast "Hello world!"
```
{{% /expand %}}

#### Delete an interactive element
```vb
(un(make|format)|remove) [the] next gui [slot]
```
```vb
(un(make|format)|remove) gui [slot[s]] %strings/numbers%
```
```vb
(un(make|format)|remove) all [[of] the] gui [slots]
```

#### When opening the GUI
The code inside this section will be executed after opening the GUI to the player.
```vb
run (when|while) open[ing] [[the] gui]
```
```vb
run (when|while) [the] gui opens
```
```vb
run on gui open[ing]
```

#### When closing the GUI
The code inside this section will be executed after the GUI is closed by the player.
```vb
run (when|while) clos(e|ing) [[the] gui]
```
```vb
run (when|while) [the] gui closes
```
```vb
run on gui clos(e|ing)
```

#### Last created GUI
Returns the last GUI created/modified
```vb
[the] last[ly] [(created|edited)] gui
```