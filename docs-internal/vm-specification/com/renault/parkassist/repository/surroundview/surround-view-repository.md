---
# Copyright (c) 2019 Renault SW Labs
#
# Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
# intellectual property rights. Use of this software is subject to a specific
# license granted by RENAULT S.A.S.
disclaimer: 'This is a generated file: do not modify.'
title: Surround View Repository
lang: en-US
namespace: com/renault/parkassist/repository/surroundview
description: Exposes surround view service as livedatas.
---

# Surround View Repository

Exposes surround view service as livedatas.

|property  |specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|namespace |`com/renault/parkassist/repository/surroundview`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
|name      |`SurroundViewRepository`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
|properties|[authorizedActions](#prop_authorizedActions), [autoZoomRearViewActivation](#prop_autoZoomRearViewActivation), [brightness](#prop_brightness), [brightnessMax](#prop_brightnessMax), [brightnessMin](#prop_brightnessMin), [color](#prop_color), [colorMax](#prop_colorMax), [colorMin](#prop_colorMin), [contrast](#prop_contrast), [contrastMax](#prop_contrastMax), [contrastMin](#prop_contrastMin), [dynamicGuidelinesActivation](#prop_dynamicGuidelinesActivation), [errorState](#prop_errorState), [featureConfig](#prop_featureConfig), [is3DViewSupported](#prop_is3DViewSupported), [isAutoZoomSupported](#prop_isAutoZoomSupported), [isBrightnessSupported](#prop_isBrightnessSupported), [isCameraOnTrunk](#prop_isCameraOnTrunk), [isColorSupported](#prop_isColorSupported), [isContrastSupported](#prop_isContrastSupported), [isDynamicGuidelinesSupported](#prop_isDynamicGuidelinesSupported), [isPanoramicViewSupported](#prop_isPanoramicViewSupported), [isRegulationApplicable](#prop_isRegulationApplicable), [isStaticGuidelinesSupported](#prop_isStaticGuidelinesSupported), [isTrailerGuidelinesSupported](#prop_isTrailerGuidelinesSupported), [isTrailerViewSupported](#prop_isTrailerViewSupported), [staticGuidelinesActivation](#prop_staticGuidelinesActivation), [surroundState](#prop_surroundState), [trailerGuidelinesActivation](#prop_trailerGuidelinesActivation), [trailerPresence](#prop_trailerPresence), [trunkState](#prop_trunkState), [warningState](#prop_warningState)|
|actions   |[acknowledgeWarning](#action_acknowledgeWarning), [request](#action_request), [screenPress](#action_screenPress), [screenRelease](#action_screenRelease), [setAutoZoomRearViewActivation](#action_setAutoZoomRearViewActivation), [setBrightness](#action_setBrightness), [setCameraPosition](#action_setCameraPosition), [setColor](#action_setColor), [setContrast](#action_setContrast), [setDynamicGuidelinesActivation](#action_setDynamicGuidelinesActivation), [setStaticGuidelinesActivation](#action_setStaticGuidelinesActivation), [setStatus](#action_setStatus), [setTrailerGuidelinesActivation](#action_setTrailerGuidelinesActivation)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
|types     |[Action](#type_Action), [ErrorState](#type_ErrorState), [FeatureConfig](#type_FeatureConfig), [ManeuverAvailability](#type_ManeuverAvailability), [Origin](#type_Origin), [SurroundState](#type_SurroundState), [TrailerPresence](#type_TrailerPresence), [TrunkState](#type_TrunkState), [UserAcknowledgement](#type_UserAcknowledgement), [View](#type_View), [WarningState](#type_WarningState)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |

> *Generated by midl v2.1.0 from 'ParkAssist/vm-specification/repository/surround-view-repository.ts'.*

<a id="title_Properties"></a>

## Properties

<a id="prop_authorizedActions"></a>

### authorizedActions

```ts
authorizedActions: integer[]
```

Authorized actions.

|attribute|specification|
|---------|-------------|
|type     |`integer`[]  |
|required |`Yes`        |
|final    |`No`         |
|default  |`[]`         |

<a id="prop_autoZoomRearViewActivation"></a>

### autoZoomRearViewActivation

```ts
autoZoomRearViewActivation: boolean
```

Auto-zoom feature activation setting.

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

Camera brightness setting.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_brightnessMax"></a>

### brightnessMax

```ts
brightnessMax: integer
```

Brightness adjustement maximum value

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

Brightness adjustement minimum value

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

Camera color setting.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_colorMax"></a>

### colorMax

```ts
colorMax: integer
```

Color adjustement maximum value

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

Color adjustement minimum value

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

Camera contrast setting.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

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

Contrast adjustement minimum value

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`0`          |

<a id="prop_dynamicGuidelinesActivation"></a>

### dynamicGuidelinesActivation

```ts
dynamicGuidelinesActivation: boolean
```

Dynamic guidelines activation setting.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_errorState"></a>

### errorState

```ts
errorState: ErrorState
```

Error state.

|attribute|specification                 |
|---------|------------------------------|
|type     |[ErrorState](#type_ErrorState)|
|required |`Yes`                         |
|final    |`No`                          |
|default  |`"error_state_no_error"`      |

<a id="prop_featureConfig"></a>

### featureConfig

```ts
featureConfig: FeatureConfig
```

Hardware configuration as exposed by the service.
Will be one of RVC, AVM or NONE.

|attribute|specification                       |
|---------|------------------------------------|
|type     |[FeatureConfig](#type_FeatureConfig)|
|required |`Yes`                               |
|final    |`Yes`                               |
|default  |`"rvc"`                             |

<a id="prop_is3DViewSupported"></a>

### is3DViewSupported

```ts
is3DViewSupported: boolean
```

3D view feature availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isAutoZoomSupported"></a>

### isAutoZoomSupported

```ts
isAutoZoomSupported: boolean
```

Auto-zoom feature availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isBrightnessSupported"></a>

### isBrightnessSupported

```ts
isBrightnessSupported: boolean
```

Brightness adjustement feature availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isCameraOnTrunk"></a>

### isCameraOnTrunk

```ts
isCameraOnTrunk: boolean
```

Is rear camera located on trunk door

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isColorSupported"></a>

### isColorSupported

```ts
isColorSupported: boolean
```

Color adjustement feature availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isContrastSupported"></a>

### isContrastSupported

```ts
isContrastSupported: boolean
```

Contrast adjustement feature availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isDynamicGuidelinesSupported"></a>

### isDynamicGuidelinesSupported

```ts
isDynamicGuidelinesSupported: boolean
```

Dynamic guidelines feature availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isPanoramicViewSupported"></a>

### isPanoramicViewSupported

```ts
isPanoramicViewSupported: boolean
```

Panoramic view feature availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isRegulationApplicable"></a>

### isRegulationApplicable

```ts
isRegulationApplicable: boolean
```

Is regulation applicable

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isStaticGuidelinesSupported"></a>

### isStaticGuidelinesSupported

```ts
isStaticGuidelinesSupported: boolean
```

Static guidelines feature availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isTrailerGuidelinesSupported"></a>

### isTrailerGuidelinesSupported

```ts
isTrailerGuidelinesSupported: boolean
```

Trailer guidelines feature availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_isTrailerViewSupported"></a>

### isTrailerViewSupported

```ts
isTrailerViewSupported: boolean
```

Trailer view feature availability

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`true`       |

<a id="prop_staticGuidelinesActivation"></a>

### staticGuidelinesActivation

```ts
staticGuidelinesActivation: boolean
```

Static guidelines activation setting.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_surroundState"></a>

### surroundState

```ts
surroundState: SurroundState
```

Current surround state.

|attribute|specification                            |
|---------|-----------------------------------------|
|type     |[SurroundState](#type_SurroundState)     |
|required |`Yes`                                    |
|final    |`No`                                     |
|default  |`{"view":"no_display","isRequest":false}`|

<a id="prop_trailerGuidelinesActivation"></a>

### trailerGuidelinesActivation

```ts
trailerGuidelinesActivation: boolean
```

Trailer guidelines activation setting.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_trailerPresence"></a>

### trailerPresence

```ts
trailerPresence: TrailerPresence
```

Trailer presence.

|attribute|specification                           |
|---------|----------------------------------------|
|type     |[TrailerPresence](#type_TrailerPresence)|
|required |`Yes`                                   |
|final    |`No`                                    |
|default  |`"trailer_presence_unavailable"`        |

<a id="prop_trunkState"></a>

### trunkState

```ts
trunkState: TrunkState
```

Trunk state.

|attribute|specification                   |
|---------|--------------------------------|
|type     |[TrunkState](#type_TrunkState)  |
|required |`Yes`                           |
|final    |`No`                            |
|default  |`"trunk_door_state_unavailable"`|

<a id="prop_warningState"></a>

### warningState

```ts
warningState: WarningState
```

Warning state.

|attribute|specification                     |
|---------|----------------------------------|
|type     |[WarningState](#type_WarningState)|
|required |`Yes`                             |
|final    |`No`                              |
|default  |`"warning_state_none"`            |

<a id="title_Actions"></a>

## Actions

<a id="action_acknowledgeWarning"></a>

### acknowledgeWarning

```ts
acknowledgeWarning(userAck: UserAcknowledgement);
```

Acknowledge a warning state. Client can provide confirmations to certain requests/states.
This is the case for clearing AVM warning messages or for RVC/HFP features.
Clients should acknowledge AVM warning states (as defined in the service UserAcknowledgement StateListener#onWarningStateChange).

#### Parameter

- `userAck`: Client user acknowledgement (as defined in the service UserAcknowledgement IntDef).

|attribute|specification                                   |
|---------|------------------------------------------------|
|type     |[UserAcknowledgement](#type_UserAcknowledgement)|
|required |`Yes`                                           |

<a id="action_request"></a>

### request

```ts
request(action: Action);
```

User action request to switch to another view

#### Parameter

- `action`: Requested user action (as defined in the service Action IntDef).

|attribute|specification         |
|---------|----------------------|
|type     |[Action](#type_Action)|
|required |`Yes`                 |

<a id="action_screenPress"></a>

### screenPress

```ts
screenPress(
  finger: integer,
  x: float,
  y: float
);
```

Notify surround view service about the screen press position in the screen in
pixels. The origin of the system coordinates in the top left of the screen.
Only available on 3D screen.

#### Parameters

- `finger`: Finger touch identifier for multi-touch

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

- `x`: Touch screen press position along x axis in pixels (sub-pixel precision supported).
Value range is from 0.0 to screen width - 1.0.

|attribute|specification|
|---------|-------------|
|type     |`float`      |
|required |`Yes`        |

- `y`: Touch screen press position along y axis in pixels (sub-pixel precision supported).
Value range is from 0.0 to screen height - 1.0.

|attribute|specification|
|---------|-------------|
|type     |`float`      |
|required |`Yes`        |

<a id="action_screenRelease"></a>

### screenRelease

```ts
screenRelease(finger: integer);
```

Notify surround view service about screen press release. Only available on 3D
screen.

#### Parameter

- `finger`: Finger touch identifier for multi-touch
(as defined in the service FingerTouch IntDef).

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

<a id="action_setAutoZoomRearViewActivation"></a>

### setAutoZoomRearViewActivation

```ts
setAutoZoomRearViewActivation(value: boolean);
```

Set Auto Zoom Rear View Activation setting.

#### Parameter

- `value`: Set to true to activate auto zoom rear view configuration, false otherwise.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="action_setBrightness"></a>

### setBrightness

```ts
setBrightness(value: integer);
```

Set brightness setting value.

#### Parameter

- `value`: Brightness setting value to set.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

<a id="action_setCameraPosition"></a>

### setCameraPosition

```ts
setCameraPosition(
  x0: integer,
  y0: integer,
  x1: integer,
  y1: integer
);
```

Notify surround view service about the current surface view location and
size in the screen in pixels. The origin of the system coordinates is the top
left of the screen.

#### Parameters

- `x0`: Position of the bottom left corner of the camera view along x axis in pixels. 
Value range is from 0 to screen width - 1.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

- `y0`: Position of the bottom left corner of the camera view along y axis in pixels. 
Value range is from 0 to screen width - 1.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

- `x1`: Position of the top right corner of the camera view along x axis in pixels.
Value range is from 0 to screen height - 1.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

- `y1`: Position of the top right corner of the camera view along y axis in pixels.
Value range is from 0 to screen height - 1.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

<a id="action_setColor"></a>

### setColor

```ts
setColor(value: integer);
```

Set color setting value.

#### Parameter

- `value`: Color setting value to set.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

<a id="action_setContrast"></a>

### setContrast

```ts
setContrast(value: integer);
```

Set contrast setting value. Note: if not supported, the listener
callback is not called.

#### Parameter

- `value`: Contrast setting value to set.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

<a id="action_setDynamicGuidelinesActivation"></a>

### setDynamicGuidelinesActivation

```ts
setDynamicGuidelinesActivation(value: boolean);
```

Set dynamic guidelines activation setting.

#### Parameter

- `value`: Set to true to activate dynamic guidelines, false otherwise.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="action_setStaticGuidelinesActivation"></a>

### setStaticGuidelinesActivation

```ts
setStaticGuidelinesActivation(value: boolean);
```

Set static guidelines activation setting.

#### Parameter

- `value`: Set to true to activate static guidelines, false otherwise.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="action_setStatus"></a>

### setStatus

```ts
setStatus(View: View);
```

Set display view status. Client shall indicate its current
display status to the surround view component, client
shall set display status to NO_DISPLAY to allow view
state change.

#### Parameter

- `View`: Current client displayed view (as defined in the service ViewState IntDef).

|attribute|specification     |
|---------|------------------|
|type     |[View](#type_View)|
|required |`Yes`             |

<a id="action_setTrailerGuidelinesActivation"></a>

### setTrailerGuidelinesActivation

```ts
setTrailerGuidelinesActivation(value: boolean);
```

Set trailer guidelines activation setting.

#### Parameter

- `value`: Set to true to activate trailer guideline, false otherwise.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="title_Types"></a>

## Types

<a id="type_Action"></a>

### Action

```ts
type Action = "close_view" | "activate_maneuver_view" | "activate_settings_view" | "activate_trailer_view" | "select_panoramic_view" | "select_standard_view" | "select_sides_view" | "select_three_dimension_view" | "select_front_camera" | "select_rear_camera" | "back_from_settings_view"
```

|attribute|specification                                                                                                                                                                                                                                                                               |
|---------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|type     |`string`                                                                                                                                                                                                                                                                                    |
|default  |`"close_view"`                                                                                                                                                                                                                                                                              |
|enum     |`"close_view"`, `"activate_maneuver_view"`, `"activate_settings_view"`, `"activate_trailer_view"`, `"select_panoramic_view"`, `"select_standard_view"`, `"select_sides_view"`, `"select_three_dimension_view"`, `"select_front_camera"`, `"select_rear_camera"`, `"back_from_settings_view"`|

<a id="type_ErrorState"></a>

### ErrorState

```ts
type ErrorState = "error_state_no_error" | "error_state_camera_failure"
```

|attribute|specification                                           |
|---------|--------------------------------------------------------|
|type     |`string`                                                |
|default  |`"error_state_no_error"`                                |
|enum     |`"error_state_no_error"`, `"error_state_camera_failure"`|

<a id="type_FeatureConfig"></a>

### FeatureConfig

```ts
type FeatureConfig = "rvc" | "avm" | "none"
```

Type of surround view hardware present in the vehicle.

|attribute|specification             |
|---------|--------------------------|
|type     |`string`                  |
|default  |`"rvc"`                   |
|enum     |`"rvc"`, `"avm"`, `"none"`|

<a id="type_ManeuverAvailability"></a>

### ManeuverAvailability

```ts
type ManeuverAvailability = "not_ready" | "ready" | "restricted"
```

State of maneuver feature availability.

|attribute|specification                           |
|---------|----------------------------------------|
|type     |`string`                                |
|default  |`"not_ready"`                           |
|enum     |`"not_ready"`, `"ready"`, `"restricted"`|

<a id="type_Origin"></a>

### Origin

```ts
type Origin = "request_from_vehicle" | "request_from_client" | "no_request"
```

|attribute|specification                                                    |
|---------|-----------------------------------------------------------------|
|type     |`string`                                                         |
|default  |`"request_from_vehicle"`                                         |
|enum     |`"request_from_vehicle"`, `"request_from_client"`, `"no_request"`|

<a id="type_SurroundState"></a>

### SurroundState

```ts
type SurroundState = {
  view: View;
  isRequest: boolean
}
```

State representation of surroundView service.
if isRequest is true, the state is currently request, else the state is established

|attribute |specification                                                               |
|----------|----------------------------------------------------------------------------|
|type      |`object`                                                                    |
|default   |`{"view":"no_display","isRequest":false}`                                   |
|properties|[view](#prop_SurroundState_view), [isRequest](#prop_SurroundState_isRequest)|

<a id="prop_SurroundState_view"></a>

#### view

```ts
view: View
```

|attribute|specification     |
|---------|------------------|
|type     |[View](#type_View)|
|required |`Yes`             |
|default  |`"no_display"`    |

<a id="prop_SurroundState_isRequest"></a>

#### isRequest

```ts
isRequest: boolean
```

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|default  |`false`      |

<a id="type_TrailerPresence"></a>

### TrailerPresence

```ts
type TrailerPresence = "trailer_presence_unavailable" | "trailer_presence_detected" | "trailer_presence_not_detected"
```

|attribute|specification                                                                                     |
|---------|--------------------------------------------------------------------------------------------------|
|type     |`string`                                                                                          |
|default  |`"trailer_presence_unavailable"`                                                                  |
|enum     |`"trailer_presence_unavailable"`, `"trailer_presence_detected"`, `"trailer_presence_not_detected"`|

<a id="type_TrunkState"></a>

### TrunkState

```ts
type TrunkState = "trunk_door_state_unavailable" | "trunk_door_opened" | "trunk_door_closed"
```

|attribute|specification                                                                 |
|---------|------------------------------------------------------------------------------|
|type     |`string`                                                                      |
|default  |`"trunk_door_state_unavailable"`                                              |
|enum     |`"trunk_door_state_unavailable"`, `"trunk_door_opened"`, `"trunk_door_closed"`|

<a id="type_UserAcknowledgement"></a>

### UserAcknowledgement

```ts
type UserAcknowledgement = "ack_ok" | "ack_cancel"
```

|attribute|specification             |
|---------|--------------------------|
|type     |`string`                  |
|default  |`"ack_ok"`                |
|enum     |`"ack_ok"`, `"ack_cancel"`|

<a id="type_View"></a>

### View

```ts
type View = "no_display" | "rear_view" | "front_view" | "panoramic_rear_view" | "panoramic_front_view" | "sides_view" | "three_dimension_view" | "pop_up_view" | "dealer_view" | "auto_zoom_rear_view" | "trailer_view" | "settings_rear_view" | "settings_front_view" | "apa_front_view" | "apa_rear_view"
```

|attribute|specification                                                                                                                                                                                                                                                                                                  |
|---------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|type     |`string`                                                                                                                                                                                                                                                                                                       |
|default  |`"no_display"`                                                                                                                                                                                                                                                                                                 |
|enum     |`"no_display"`, `"rear_view"`, `"front_view"`, `"panoramic_rear_view"`, `"panoramic_front_view"`, `"sides_view"`, `"three_dimension_view"`, `"pop_up_view"`, `"dealer_view"`, `"auto_zoom_rear_view"`, `"trailer_view"`, `"settings_rear_view"`, `"settings_front_view"`, `"apa_front_view"`, `"apa_rear_view"`|

<a id="type_WarningState"></a>

### WarningState

```ts
type WarningState = "warning_state_none" | "warning_state_speed_nok" | "warning_state_camera_misaligned" | "warning_state_camera_soiled" | "warning_state_trailer_not_detected" | "warning_state_obstacle_present" | "warning_state_trailer_access_limited"
```

|attribute|specification                                                                                                                                                                                                                                  |
|---------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|type     |`string`                                                                                                                                                                                                                                       |
|default  |`"warning_state_none"`                                                                                                                                                                                                                         |
|enum     |`"warning_state_none"`, `"warning_state_speed_nok"`, `"warning_state_camera_misaligned"`, `"warning_state_camera_soiled"`, `"warning_state_trailer_not_detected"`, `"warning_state_obstacle_present"`, `"warning_state_trailer_access_limited"`|

