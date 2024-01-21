+++
archetype = "default"
title = "Processing the results"
weight = 3
+++
# PROCESSING THE RESULTS
#### The player's open GUI
Returns the currently open GUI of the player
```vb
%players%'s gui
```
```vb
gui of %player%'s
```

#### Check whether any GUI is open or not for the player
```vb
%players% (has|have) a gui [open]
```
```vb
%players% do[es](n't| not) have a gui [open]
```

#### The next GUI slot
Returns the number or letter of the template of the next GUI slot
```vb
%guis%'[s] next gui slot[s]
```
```vb
[the] next gui slot[s] of %guis%
```
{{% notice style="warning" %}}
Subsequent expressions can only be used in the [GUI creation sections <i class="fas fa-link"></i>](../gui-creation/#создание-нового-gui) and [creating an interactive element <i class="fas fa-link"></i>](../gui-creation/#изменить-уже-созданное-gui)
{{% /notice %}}
```vb
[the] next gui slot
```

#### The name GUI inventory
```vb
%gui%'s gui(-| )name
```
```vb
gui(-| )name of %gui%
```

#### GUI Inventory Size
```vb
%gui%'s gui(-| )size
```
```vb
gui(-| )size of %gui%
```

#### GUI Template
```vb
%gui%'s gui(-| )shape
```
```vb
gui(-| )shape of %gui%
```

#### The ability to take items in the GUI
```vb
%gui%'s gui(-| )lock(-| )status
```
```vb
gui(-| )lock(-| )status of %gui%
```

#### Current, editable GUI
```vb
[the] gui
```

#### Clicked Slot
```vb
[the] gui(-| )raw(-| )slot
```

#### The hotkey of the clicked slot
```vb
[the] gui(-| )hotbar(-| )slot
```

#### GUI inventory that is being edited
```vb
[the] gui(-| )inventory
```

#### Action inside the GUI
For example, a player made a double click, which caused things to gather in the cursor slot.
```vb
[the] gui(-| )inventory(-| )action
```

#### Type of click
For example, a player made a click with the Shift key held down.
```vb
[the] gui(-| )click(-| )(type|action)
```

#### Cursor slot
```vb
[the] gui(-| )cursor[(-| )item]
```

#### The type of clicked slot
```vb
[the] gui(-| )slot(-| )type
```

#### The clicked item
```vb
[the] gui[(-| )(clicked|current)](-| )item
```

#### The expression used instead of the player inside the GUI section
```vb
[the] gui(-| )player
```
{{% notice style="warning" %}}
Be sure to use this expression inside the GUI or interactive element creation section instead of **player**, otherwise your code will not work as you expect it to.
{{% /notice %}}

#### Players who have a GUI open
```vb
[the] gui(-| )(viewer|player)s
```

#### ID of the clicked slot
```vb
[the] gui(-| )slot(-| )id
```

{{% notice style="warning" %}}
Subsequent expressions can only be used in the [closing GUI section <i class="fas fa-link"></i>](../gui-creation/#при-закрытии-gui)
{{% /notice %}}

#### Cancel the closure
Prohibits the player from closing the GUI by reopening it while preserving the GUI parameters
```vb
cancel [the] gui clos(e|ing)
```

#### Allow closure
Allows the player to close the GUI
```vb
uncancel [the] gui close(e|ing)
```