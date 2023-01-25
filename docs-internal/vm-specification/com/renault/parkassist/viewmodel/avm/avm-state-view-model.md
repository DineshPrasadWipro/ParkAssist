---
# Copyright (c) 2019 Renault SW Labs
#
# Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
# intellectual property rights. Use of this software is subject to a specific
# license granted by RENAULT S.A.S.
disclaimer: 'This is a generated file: do not modify.'
title: Avm State View Model
lang: en-US
namespace: com/renault/parkassist/viewmodel/avm
description: Arround View Monitoring (AVM) video display current state
---

# Avm State View Model

Arround View Monitoring (AVM) video display current state

|property  |specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|namespace |`com/renault/parkassist/viewmodel/avm`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
|name      |`AvmStateViewModel`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
|properties|[backButtonVisible](#prop_backButtonVisible), [birdSideCameraMargin](#prop_birdSideCameraMargin), [buttonsEnabled](#prop_buttonsEnabled), [closeVisible](#prop_closeVisible), [easyparkShortcutVisible](#prop_easyparkShortcutVisible), [maneuverVisible](#prop_maneuverVisible), [modeSelected](#prop_modeSelected), [selectPanoramicViewVisible](#prop_selectPanoramicViewVisible), [selectSidesViewVisible](#prop_selectSidesViewVisible), [selectStandardViewVisible](#prop_selectStandardViewVisible), [selectThreeDimensionViewVisible](#prop_selectThreeDimensionViewVisible), [settingsVisible](#prop_settingsVisible), [threeDimensionInfoVisible](#prop_threeDimensionInfoVisible), [trailerVisible](#prop_trailerVisible)|
|actions   |[closeView](#action_closeView), [requestView](#action_requestView), [requestViewMode](#action_requestViewMode)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
|types     |[AvmModeRequest](#type_AvmModeRequest), [AvmModeSelected](#type_AvmModeSelected)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |

> *Generated by midl v2.1.0 from 'ParkAssist/vm-specification/avm-state.ts'.*

<a id="title_Properties"></a>

## Properties

<a id="prop_backButtonVisible"></a>

### backButtonVisible

```ts
backButtonVisible: boolean
```

Back button visibility

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_birdSideCameraMargin"></a>

### birdSideCameraMargin

```ts
birdSideCameraMargin: boolean
```

Whether or not we should add camera horizontal margin in order
to center it on screen for bird side view

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_buttonsEnabled"></a>

### buttonsEnabled

```ts
buttonsEnabled: boolean
```

Whether buttons are enabled or not

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_closeVisible"></a>

### closeVisible

```ts
closeVisible: boolean
```

Close camera view action visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_easyparkShortcutVisible"></a>

### easyparkShortcutVisible

```ts
easyparkShortcutVisible: boolean
```

Easypark shortcut visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`false`      |

<a id="prop_maneuverVisible"></a>

### maneuverVisible

```ts
maneuverVisible: boolean
```

Activate maneuver view action visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_modeSelected"></a>

### modeSelected

```ts
modeSelected: AvmModeSelected
```

Current display type button selected

|attribute|specification                           |
|---------|----------------------------------------|
|type     |[AvmModeSelected](#type_AvmModeSelected)|
|required |`Yes`                                   |
|final    |`No`                                    |
|default  |`"standard"`                            |

<a id="prop_selectPanoramicViewVisible"></a>

### selectPanoramicViewVisible

```ts
selectPanoramicViewVisible: boolean
```

Select panoramic view action visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_selectSidesViewVisible"></a>

### selectSidesViewVisible

```ts
selectSidesViewVisible: boolean
```

Select sides view action visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_selectStandardViewVisible"></a>

### selectStandardViewVisible

```ts
selectStandardViewVisible: boolean
```

Select standard view action visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_selectThreeDimensionViewVisible"></a>

### selectThreeDimensionViewVisible

```ts
selectThreeDimensionViewVisible: boolean
```

Select three dimension view action visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_settingsVisible"></a>

### settingsVisible

```ts
settingsVisible: boolean
```

Activate settings view action visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_threeDimensionInfoVisible"></a>

### threeDimensionInfoVisible

```ts
threeDimensionInfoVisible: boolean
```

Three dimension info text visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_trailerVisible"></a>

### trailerVisible

```ts
trailerVisible: boolean
```

Activate trailer view action visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="title_Actions"></a>

## Actions

<a id="action_closeView"></a>

### closeView

```ts
closeView();
```

This function is the exit point for Avm camera scenarios.
It will decide what how to properly leave Avm.
To be called when lifecycle is detached to viewModels livedatas

<a id="action_requestView"></a>

### requestView

```ts
requestView();
```

This function is the entry point for Avm camera scenarios.
It will decide what mode to set by default.
To be called when lifecycle is attached to viewModels livedatas

<a id="action_requestViewMode"></a>

### requestViewMode

```ts
requestViewMode(request: AvmModeRequest);
```

Requests the AVM to display a specific view mode

#### Parameter

- `request`: AVM view mode request

|attribute|specification                         |
|---------|--------------------------------------|
|type     |[AvmModeRequest](#type_AvmModeRequest)|
|required |`Yes`                                 |

<a id="title_Types"></a>

## Types

<a id="type_AvmModeRequest"></a>

### AvmModeRequest

```ts
type AvmModeRequest = "standard" | "panoramic" | "sides" | "view-3d" | "maneuver" | "close" | "settings" | "back-from-settings"
```

View mode request, asked by a user when he clicks on a bottom navigation view button in the AVM app:
- `standard` requests for a standard view (bird + rear/front view)
- `panoramic` requests for a panoramic view (rear or front panoramic view)
- `sides` requests for a sides view
- `view-3d` request for a 3D view
- 'settings' requests a settings view
- 'back-from-settings' requests a back from settings view

|attribute|specification                                                                                                     |
|---------|------------------------------------------------------------------------------------------------------------------|
|type     |`string`                                                                                                          |
|default  |`"standard"`                                                                                                      |
|enum     |`"standard"`, `"panoramic"`, `"sides"`, `"view-3d"`, `"maneuver"`, `"close"`, `"settings"`, `"back-from-settings"`|

<a id="type_AvmModeSelected"></a>

### AvmModeSelected

```ts
type AvmModeSelected = "standard" | "panoramic" | "sides" | "three-d"
```

AVM video current display mode selected
- `standard` Standard view avm selected
- `panoramic` Panoramic view selected
- `sides` Sides view selected
- `three-d` Three-d view selected

|attribute|specification                                      |
|---------|---------------------------------------------------|
|type     |`string`                                           |
|default  |`"standard"`                                       |
|enum     |`"standard"`, `"panoramic"`, `"sides"`, `"three-d"`|

