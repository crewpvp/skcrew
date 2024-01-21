+++
archetype = "default"
title = "Painting on canvas"
weight = 2
+++
# Painting on canvas

#### Get the color of a pixel on the canvas
```vb
pixel %number%(,[ ]| )%number% of %map%
```
{{% notice style="warning" %}}
It does not allow you to change the color of a pixel, use the expression [paint over a pixel <i class="fas fa-link"></i>](./#закрасить-пиксель)
{{% /notice %}}
#### Paint over a pixel
Allows you to set a specific color for a pixel based on its coordinates.
The first two arguments point to the coordinates on the canvas, the subsequent ones are in RGB or RGBA format, and the last one is the canvas.
```vb
draw pixel [at] %number%(,[ ]| )%number% [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```
{{% expand title="Example" %}}
```vb
set {_map} to new drawing map
draw pixel at 1,1 with color 255,0,0 on map {_map}
# will draw pixel at 1,1 with pure red color
```
{{% /expand %}}
#### Paint over pixels
Allows you to set a specific color for pixels in the range. \
The first four arguments point to the coordinates on the canvas, the next color is in RGB or RGBA format, and the last one is the canvas.

```vb
draw pixels (from|between) %number%(,[ ]| )%number% [to] %number%(,[ ]| )%number% [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```
{{% expand title="Example" %}}
```vb
set {_map} to new drawing map
draw pixels from 10,10 to 50,50 with color 255,0,0 on map {_map}
# will draw pure red square on map
```
{{% /expand %}}

#### Draw a line
Accepts similar arguments with pixel coloring.
```vb
draw line (from|between) %number%(,[ ]| )%number% [to] %number%(,[ ]| )%number% [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```

#### Draw a bezier curve
The first four arguments are the points between which the main part of the curve will be. The following four arguments allow you to optionally specify control points for the curve offset. The following arguments are similar to drawing a line or coloring pixels.
```vb
draw bezier curve (from|between) %number%(,[ ]| )%number% [to] %number%(,[ ]| )%number% [[with] control points] %number%(,[ ]| )%number% [and] %number%(,[ ]| )%number% [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```

#### Draw a circle
The first two arguments are the center of the circle, the next one is the radius of the circle. The other arguments are color and canvas.
```vb
draw circle at %number%(,[ ]| )%number% with radius %number% [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```

#### Draw an image
The image should be in the `images/` folder of the addon. Takes the name of the image file as the first argument.
```vb
draw image %string% on [[the] map] %map%
```

#### Write text on the map
The first argument is the text to be written, the next two arguments describe the font name and font size.
```vb
draw text %string% [at] %number%(,[ ]| )%number% [with font %-string%] [with size %-number%] [with colo[u]r] %number%(,[ ]| )%number%(,[ ]| )%number%[(,[ ]| )%-number%] on [map] %map%
```