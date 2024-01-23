+++
archetype = "default"
title = "Processing the results"
weight = 3
+++
# Processing the results
#### Get the player inside the form
{{% notice style="note" %}}
Use this expression to get the player inside the form, instead of the usual player or any variables.
{{% /notice %}}
```vb
form(-| )player
```

#### Getting the type of the created form
```vb
form[(-| )]type of %form%
```
```vb
%form%'s form[(-| )]type
```
Available types of forms for comparison:
- custom form
- modal form
- simple form

#### Execute the code when opening/closing the form
```vb
run (when|while) (open[ing]|clos(e|ing)) [[the] form]
```
```vb
run (when|while) [the] form (opens|closes)
```
```vb
run on form (open[ing]|clos(e|ing))
```
{{% expand title="Example" %}}
```vb
create modal form named "Modal form":
  run on form close:
  	broadcast "%formplayer%" #will show name of player what close form
open last created form to player
```
{{% /expand %}}
#### Cancel or allow closing of the form
By default, closing the form is allowed. If you disable it, the form will be reopened after the selection
```vb
cancel [the] form clos(e|ing)
```
```vb
uncancel [the] form clos(e|ing)
```
#### Get the reason for closing the form
{{% notice style="note" %}}
This expression can only be used in the [section when closing the form <i class="fas fa-link"></i>](./getting-results#execute-the-code-when-openingclosing-the-form)
{{% /notice %}}
```vb
[form(-| )]close reason
```
Available reasons for closing for comparison:
- close
- (submit|success)
- invalid[ response]

#### Execute the code on form submit
```vb
run on form (result|submit)
```
{{% expand title="Example" %}}
```vb
create custom form named "Custom form":
  form toggle named "toggle value"
  run on form result:
    broadcast "%form toggle 1 value%"
open last created form to player
```
{{% /expand %}}

#### Custom form elements
{{% notice style="note" %}}
This expression can only be used in the [section when the form is successfully closed <i class="fas fa-link"></i>](./getting-results#execute-the-code-on-form-submit)
{{% /notice %}}
```vb
[form[(-| )]](drop[(-| )]down|input|slider|step[(-| )]slider|toggle) %number% [value]
```
```vb
value of [form[(-| )]](drop[(-| )]down|input|slider|step[(-| )]slider|toggle) %number%
```

{{% notice style="note" %}}
For button processing [Modal form <i class="fas fa-link"></i>](../form-types/#modal-form) и [Simple form <i class="fas fa-link"></i>](../form-types/#simple-form) use the [button creation section <i class="fas fa-link"></i>](../elements/#buttons) 
{{% /notice %}}
