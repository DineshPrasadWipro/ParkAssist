---
# Copyright (c) 2019 Renault SW Labs
#
# Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
# intellectual property rights. Use of this software is subject to a specific
# license granted by RENAULT S.A.S.
disclaimer: 'This is a generated file: do not modify.'
title: Camera Settings View Model
lang: en-US
namespace: com/renault/parkassist/viewmodel/camera
description: Control camera settings
---

# Camera Settings View Model

Control camera settings

|property  |specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|namespace |`com/renault/parkassist/viewmodel/camera`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|name      |`CameraSettingsViewModel`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|properties|[backButtonVisibility](#prop_backButtonVisibility), [brightness](#prop_brightness), [brightnessMax](#prop_brightnessMax), [brightnessMin](#prop_brightnessMin), [color](#prop_color), [colorMax](#prop_colorMax), [colorMin](#prop_colorMin), [contrast](#prop_contrast), [contrastMax](#prop_contrastMax), [contrastMin](#prop_contrastMin), [isAutoZoomActive](#prop_isAutoZoomActive), [isAutoZoomAvailable](#prop_isAutoZoomAvailable), [isDynamicGuidelinesActive](#prop_isDynamicGuidelinesActive), [isDynamicGuidelinesAvailable](#prop_isDynamicGuidelinesAvailable), [isStaticGuidelinesActive](#prop_isStaticGuidelinesActive), [isStaticGuidelinesAvailable](#prop_isStaticGuidelinesAvailable), [isTrailerGuidelinesActive](#prop_isTrailerGuidelinesActive), [isTrailerGuidelinesAvailable](#prop_isTrailerGuidelinesAvailable), [toolbarEnabled](#prop_toolbarEnabled)|
|actions   |[navigateBack](#action_navigateBack), [setAutoZoom](#action_setAutoZoom), [setBrightness](#action_setBrightness), [setColor](#action_setColor), [setContrast](#action_setContrast), [setDynamicGuidelines](#action_setDynamicGuidelines), [setStaticGuidelines](#action_setStaticGuidelines), [setTrailerGuidelines](#action_setTrailerGuidelines)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |

> *Generated by midl v2.1.0 from 'ParkAssist/vm-specification/camera-settings.ts'.*

<a id="title_Properties"></a>

## Properties

<a id="prop_backButtonVisibility"></a>

### backButtonVisibility

```ts
backButtonVisibility: boolean
```

Whether back button in toolbar is enabled or not

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_brightness"></a>

### brightness

```ts
brightness: integer
```

Current brightness value for the camera

|attribute |specification|
|----------|-------------|
|type      |`integer`    |
|required  |`Yes`        |
|final     |`No`         |
|default   |`0`          |
|maximum   |`100`        |
|minimum   |`0`          |
|multipleOf|`5`          |

<a id="prop_brightnessMax"></a>

### brightnessMax

```ts
brightnessMax: integer
```

Brightness adjustment maximum value

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`20`         |

<a id="prop_brightnessMin"></a>

### brightnessMin

```ts
brightnessMin: integer
```

Brightness adjustment minimum value

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`0`          |

<a id="prop_color"></a>

### color

```ts
color: integer
```

Current color value for the camera

|attribute |specification|
|----------|-------------|
|type      |`integer`    |
|required  |`Yes`        |
|final     |`No`         |
|default   |`0`          |
|maximum   |`100`        |
|minimum   |`0`          |
|multipleOf|`5`          |

<a id="prop_colorMax"></a>

### colorMax

```ts
colorMax: integer
```

Color adjustment maximum value

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`20`         |

<a id="prop_colorMin"></a>

### colorMin

```ts
colorMin: integer
```

Color adjustment minimum value

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`0`          |

<a id="prop_contrast"></a>

### contrast

```ts
contrast: integer
```

Current contrast value for the camera

|attribute |specification|
|----------|-------------|
|type      |`integer`    |
|required  |`Yes`        |
|final     |`No`         |
|default   |`0`          |
|maximum   |`100`        |
|minimum   |`0`          |
|multipleOf|`5`          |

<a id="prop_contrastMax"></a>

### contrastMax

```ts
contrastMax: integer
```

Contrast adjustement maximum value

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`20`         |

<a id="prop_contrastMin"></a>

### contrastMin

```ts
contrastMin: integer
```

Contrast adjustment minimum value

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`0`          |

<a id="prop_isAutoZoomActive"></a>

### isAutoZoomActive

```ts
isAutoZoomActive: boolean
```

Define if auto-zoom feature is active

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_isAutoZoomAvailable"></a>

### isAutoZoomAvailable

```ts
isAutoZoomAvailable: boolean
```

Auto-zoom setting availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isDynamicGuidelinesActive"></a>

### isDynamicGuidelinesActive

```ts
isDynamicGuidelinesActive: boolean
```

Define if dynamic guidelines have to be displayed

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_isDynamicGuidelinesAvailable"></a>

### isDynamicGuidelinesAvailable

```ts
isDynamicGuidelinesAvailable: boolean
```

Dynamic guidelines setting availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isStaticGuidelinesActive"></a>

### isStaticGuidelinesActive

```ts
isStaticGuidelinesActive: boolean
```

Define if static guidelines have to be displayed

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_isStaticGuidelinesAvailable"></a>

### isStaticGuidelinesAvailable

```ts
isStaticGuidelinesAvailable: boolean
```

Static guidelines setting availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isTrailerGuidelinesActive"></a>

### isTrailerGuidelinesActive

```ts
isTrailerGuidelinesActive: boolean
```

Define if trailer guidelines have to be displayed

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_isTrailerGuidelinesAvailable"></a>

### isTrailerGuidelinesAvailable

```ts
isTrailerGuidelinesAvailable: boolean
```

Trailer guidelines setting availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_toolbarEnabled"></a>

### toolbarEnabled

```ts
toolbarEnabled: boolean
```

Whether toolbar is enabled or not

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="title_Actions"></a>

## Actions

<a id="action_navigateBack"></a>

### navigateBack

```ts
navigateBack();
```

Navigate back from settings request to Surround View service
Send request to go back to previous fragment

<a id="action_setAutoZoom"></a>

### setAutoZoom

```ts
setAutoZoom(active: boolean);
```

Activate or de-activate auto-zoom feature

#### Parameter

- `active`: true to activate, false to de-activate

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="action_setBrightness"></a>

### setBrightness

```ts
setBrightness(brightness: integer);
```

Sets the brightness

#### Parameter

- `brightness`: the brightness to set

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

<a id="action_setColor"></a>

### setColor

```ts
setColor(color: integer);
```

Sets the color

#### Parameter

- `color`: the color to set

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

<a id="action_setContrast"></a>

### setContrast

```ts
setContrast(contrast: integer);
```

Sets the contrast

#### Parameter

- `contrast`: the contrast to set

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

<a id="action_setDynamicGuidelines"></a>

### setDynamicGuidelines

```ts
setDynamicGuidelines(active: boolean);
```

Activate or de-activate dynamic guidelines

#### Parameter

- `active`: true to activate, false to de-activate

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="action_setStaticGuidelines"></a>

### setStaticGuidelines

```ts
setStaticGuidelines(active: boolean);
```

Activate or de-activate static guidelines

#### Parameter

- `active`: true to activate, false to de-activate

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="action_setTrailerGuidelines"></a>

### setTrailerGuidelines

```ts
setTrailerGuidelines(active: boolean);
```

Activate or de-activate trailer guidelines

#### Parameter

- `active`: true to activate, false to de-activate

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

