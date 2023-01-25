---
# Copyright (c) 2019 Renault SW Labs
#
# Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
# intellectual property rights. Use of this software is subject to a specific
# license granted by RENAULT S.A.S.
disclaimer: 'This is a generated file: do not modify.'
title: Apa Repository
lang: en-US
namespace: com/renault/parkassist/repository/apa
description: Exposes automatic park assist service as livedatas.
---

# Apa Repository

Exposes automatic park assist service as livedatas.

|property  |specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|namespace |`com/renault/parkassist/repository/apa`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|name      |`ApaRepository`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
|properties|[automaticManeuver](#prop_automaticManeuver), [defaultManeuverType](#prop_defaultManeuverType), [displayState](#prop_displayState), [extendedInstruction](#prop_extendedInstruction), [featureConfiguration](#prop_featureConfiguration), [leftSelected](#prop_leftSelected), [leftSuitable](#prop_leftSuitable), [maneuverCompletion](#prop_maneuverCompletion), [maneuverMove](#prop_maneuverMove), [maneuverSelection](#prop_maneuverSelection), [maneuverSwitchSelection](#prop_maneuverSwitchSelection), [parallelManeuverSelection](#prop_parallelManeuverSelection), [parkOutManeuverSelection](#prop_parkOutManeuverSelection), [perpendicularManeuverSelection](#prop_perpendicularManeuverSelection), [rightSelected](#prop_rightSelected), [rightSuitable](#prop_rightSuitable), [scanningSide](#prop_scanningSide), [supportedManeuvers](#prop_supportedManeuvers), [viewMask](#prop_viewMask), [warningMessage](#prop_warningMessage)|
|actions   |[acknowledgeWarning](#action_acknowledgeWarning), [requestManeuverType](#action_requestManeuverType), [setDefaultManeuverType](#action_setDefaultManeuverType), [switchManeuverStartActivation](#action_switchManeuverStartActivation)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|types     |[DisplayState](#type_DisplayState), [FeatureConfig](#type_FeatureConfig), [Instruction](#type_Instruction), [ManeuverMove](#type_ManeuverMove), [ManeuverSelection](#type_ManeuverSelection), [ManeuverStartSwitch](#type_ManeuverStartSwitch), [ManeuverType](#type_ManeuverType), [ScanningSide](#type_ScanningSide), [ViewMask](#type_ViewMask)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |

> *Generated by midl v2.1.0 from 'ParkAssist/vm-specification/repository/apa-repository.ts'.*

<a id="title_Properties"></a>

## Properties

<a id="prop_automaticManeuver"></a>

### automaticManeuver

```ts
automaticManeuver: boolean
```

Automatic Maneuver is a boolean reporting if Status display is AUTOMATIC_MANEUVER_ON or not
Status Display is a notification of the current APA status IVI and cluster are supposed to
notify through a pictogram. On cluster it is regulatory, but not on IVI.
Note that if  DisplayState is DISPLAY_GUIDANCE, we already
know that an APA maneuver is ongoing, but status display provides more information such as
AUTOMATIC_MANEUVER_STANDBY which indicates that ongoing maneuver is paused.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_defaultManeuverType"></a>

### defaultManeuverType

```ts
defaultManeuverType: integer
```

Notify change on default maneuver type.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`1`          |

<a id="prop_displayState"></a>

### displayState

```ts
displayState: DisplayState
```

The display state is the main auto park state. It is controlled by ECU and is not managed
by the auto park service. It controls APA application activation and also its current display.
If state is DISPLAY_NONE, APA is disabled and can be activated on user request.
Otherwise for all other display states, APA is enabled and can be deactivated.

|attribute|specification                     |
|---------|----------------------------------|
|type     |[DisplayState](#type_DisplayState)|
|required |`Yes`                             |
|final    |`No`                              |
|default  |`"display_none"`                  |

<a id="prop_extendedInstruction"></a>

### extendedInstruction

```ts
extendedInstruction: Instruction
```

When APA is activated and DisplayState is not DISPLAY_NONE,
ECU can provide instructions to display on client HMI application to guide user.
Labels corresponding to each ExtendedInstruction depend on the vehicle's FeatureConfiguration.

|attribute|specification                   |
|---------|--------------------------------|
|type     |[Instruction](#type_Instruction)|
|required |`Yes`                           |
|final    |`No`                            |
|default  |`"select_side"`                 |

<a id="prop_featureConfiguration"></a>

### featureConfiguration

```ts
featureConfiguration: FeatureConfig
```

APA feature configuration
according to vehicle configuration as exposed by the service

|attribute|specification                       |
|---------|------------------------------------|
|type     |[FeatureConfig](#type_FeatureConfig)|
|required |`Yes`                               |
|final    |`Yes`                               |
|default  |`"none"`                            |

<a id="prop_leftSelected"></a>

### leftSelected

```ts
leftSelected: boolean
```

Whether the driver selected the left side with its indicator or not.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_leftSuitable"></a>

### leftSuitable

```ts
leftSuitable: boolean
```

Whether the left side of the car is suitable to park or not.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_maneuverCompletion"></a>

### maneuverCompletion

```ts
maneuverCompletion: integer
```

During an ongoing maneuver in DISPLAY_GUIDANCE DisplayState,
maneuver completion monitors maneuver progress as reported by ECU. It is used by HMI client
for drawing a progress bar in the arrow widget.
Value expressed in % unit.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |
|maximum  |`100`        |
|minimum  |`0`          |

<a id="prop_maneuverMove"></a>

### maneuverMove

```ts
maneuverMove: ManeuverMove
```

Maneuver move is an indication of the APA maneuver vehicle direction during an ongoing
maneuver. Depending on ManeuverMove client APA application draws different
indications for the gauge arrow widget.

|attribute|specification                     |
|---------|----------------------------------|
|type     |[ManeuverMove](#type_ManeuverMove)|
|required |`Yes`                             |
|final    |`No`                              |
|default  |`"first"`                         |

<a id="prop_maneuverSelection"></a>

### maneuverSelection

```ts
maneuverSelection: ManeuverSelection
```

Maneuver selection notifies of the current status for each
maneuver type supported in the current configuration

|attribute|specification                               |
|---------|--------------------------------------------|
|type     |[ManeuverSelection](#type_ManeuverSelection)|
|required |`Yes`                                       |
|final    |`No`                                        |
|default  |`"selected"`                                |

<a id="prop_maneuverSwitchSelection"></a>

### maneuverSwitchSelection

```ts
maneuverSwitchSelection: ManeuverStartSwitch
```

Indicates the current maneuver switch send by the ECU.

|attribute|specification                                   |
|---------|------------------------------------------------|
|type     |[ManeuverStartSwitch](#type_ManeuverStartSwitch)|
|required |`Yes`                                           |
|final    |`No`                                            |
|default  |`"none"`                                        |

<a id="prop_parallelManeuverSelection"></a>

### parallelManeuverSelection

```ts
parallelManeuverSelection: ManeuverSelection
```

Indicates the current selection of the parallel maneuver.

|attribute|specification                               |
|---------|--------------------------------------------|
|type     |[ManeuverSelection](#type_ManeuverSelection)|
|required |`Yes`                                       |
|final    |`No`                                        |
|default  |`"selected"`                                |

<a id="prop_parkOutManeuverSelection"></a>

### parkOutManeuverSelection

```ts
parkOutManeuverSelection: ManeuverSelection
```

Indicates the current selection of the parkout maneuver.

|attribute|specification                               |
|---------|--------------------------------------------|
|type     |[ManeuverSelection](#type_ManeuverSelection)|
|required |`Yes`                                       |
|final    |`No`                                        |
|default  |`"selected"`                                |

<a id="prop_perpendicularManeuverSelection"></a>

### perpendicularManeuverSelection

```ts
perpendicularManeuverSelection: ManeuverSelection
```

Indicates the current selection of the perpendicular maneuver.

|attribute|specification                               |
|---------|--------------------------------------------|
|type     |[ManeuverSelection](#type_ManeuverSelection)|
|required |`Yes`                                       |
|final    |`No`                                        |
|default  |`"selected"`                                |

<a id="prop_rightSelected"></a>

### rightSelected

```ts
rightSelected: boolean
```

Whether the driver selected the right side with its indicator or not.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rightSuitable"></a>

### rightSuitable

```ts
rightSuitable: boolean
```

Whether the right side of the car is suitable to park or not.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_scanningSide"></a>

### scanningSide

```ts
scanningSide: ScanningSide
```

Scanning side indicates which side of the vehicle the ECU is currently scanning for a
suitable ParkingSlot. It is a notification HMI application uses to draw
images and arrows on the display.

|attribute|specification                     |
|---------|----------------------------------|
|type     |[ScanningSide](#type_ScanningSide)|
|required |`Yes`                             |
|final    |`No`                              |
|default  |`"scanning_side_none"`            |

<a id="prop_supportedManeuvers"></a>

### supportedManeuvers

```ts
supportedManeuvers: integer[]
```

Supported Maneuver types
according to supported maneuver types as exposed by the service

|attribute|specification|
|---------|-------------|
|type     |`integer`[]  |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`[]`         |

<a id="prop_viewMask"></a>

### viewMask

```ts
viewMask: ViewMask
```

View mask is a mechanism used in FAPK configuration only.
It controls the HMI masking of the camera part of the video stream sent by the AVM ECU.
Such masking can happen while scanning for parking slots when speed is comprised in a
certain range. This is to avoid driver distraction while still allowing for APA features.

|attribute|specification             |
|---------|--------------------------|
|type     |[ViewMask](#type_ViewMask)|
|required |`Yes`                     |
|final    |`No`                      |
|default  |`"unavailable"`           |

<a id="prop_warningMessage"></a>

### warningMessage

```ts
warningMessage: integer
```

Warning messages are notifications sent by ECU that provide information to user about APA
feature. They are displayed as dialog boxes with button controls so that user can acknowledge
or give feedback to the ECU. Buttons are linked to acknowledgeWarning(int).
Note: Dialog box are to be displayed on overlay over HMI no matter the current
DisplayState of APA!

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="title_Actions"></a>

## Actions

<a id="action_acknowledgeWarning"></a>

### acknowledgeWarning

```ts
acknowledgeWarning(userAck: integer);
```

Acknowledge a warning message.
In autopark, user can give input to dialog boxes which are raised through warning messages.
If a message is not acknowledged, should it be not raised again it will clear itself.
On dialog boxes with one button, ack. 1 should be used. With two buttons: ack. 1 and ack. 2.
It is automatically reset to no ack. by VHAL after a 300ms timeout.

#### Parameter

- `userAck`: Client user acknowledgement UserAcknowledgement.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

<a id="action_requestManeuverType"></a>

### requestManeuverType

```ts
requestManeuverType(maneuverType: ManeuverType);
```

Request a new user maneuver type choice.
In APA, user can select different kinds of maneuvers that the ECU will try to perform.
User cannot request a new maneuver type if ECU indicates MANEUVER_CHOICE_UNAVAILABLE.
In that case service will raise an exception and will not perform the request.
It is mapped to HFP_SelectedManeuverTypeRequest. Is is automatically reset to no request by
VHAL after a 3000ms timeout, or before if HFP_SelectedManeuverType is changed by ECU.

#### Parameter

- `maneuverType`: Maneuver type.

|attribute|specification                     |
|---------|----------------------------------|
|type     |[ManeuverType](#type_ManeuverType)|
|required |`Yes`                             |

<a id="action_setDefaultManeuverType"></a>

### setDefaultManeuverType

```ts
setDefaultManeuverType(maneuverType: ManeuverType);
```

Set default maneuver type setting.
This setting is applied on ECU through a dedicated signal. Service will automatically
set the setting on the ECU upon initialization. It will also apply it on change.

#### Parameter

- `maneuverType`

|attribute|specification                     |
|---------|----------------------------------|
|type     |[ManeuverType](#type_ManeuverType)|
|required |`Yes`                             |

<a id="action_switchManeuverStartActivation"></a>

### switchManeuverStartActivation

```ts
switchManeuverStartActivation();
```

Request switch maneuver start of AutoPark to ECU.
This is a switch activation: used both for activation and deactivation.
Success or failure of this operation is known later through the display state callback.
Autopark can only be activated by user request. This request is always available.
As per specification, VHAL will maintain this signal pushed for 600ms before resetting.

<a id="title_Types"></a>

## Types

<a id="type_DisplayState"></a>

### DisplayState

```ts
type DisplayState = "display_none" | "display_scanning" | "display_parkout_confirmation" | "display_guidance"
```

Auto Park Assist display states
- `display none` no display
- `display scanning` scanning display
- `display parkout confirmation` parkout display
- `display guidance` scanning display

|attribute|specification                                                                                 |
|---------|----------------------------------------------------------------------------------------------|
|type     |`string`                                                                                      |
|default  |`"display_none"`                                                                              |
|enum     |`"display_none"`, `"display_scanning"`, `"display_parkout_confirmation"`, `"display_guidance"`|

<a id="type_FeatureConfig"></a>

### FeatureConfig

```ts
type FeatureConfig = "none" | "hfp" | "hfpb" | "fpk" | "fapk"
```

Auto Park Assist vehicle configuration
- `none` no auto-park assist available
- `hfp` hands-free parking available
- `hfpb` auto-break hands-free parking available
- `fpk`  full park ??? available ** FIXME **
- `fapk` full auto-park available

|attribute|specification                                 |
|---------|----------------------------------------------|
|type     |`string`                                      |
|default  |`"none"`                                      |
|enum     |`"none"`, `"hfp"`, `"hfpb"`, `"fpk"`, `"fapk"`|

<a id="type_Instruction"></a>

### Instruction

```ts
type Instruction = "select_side" | "drive_forward_to_find_parking_slot" | "drive_forward_slot_suitable" | "stop" | "select_reverse_gear_or_press_start_button" | "select_or_engage_forward_gear" | "drive_forward" | "reverse" | "maneuver_complete_or_finished" | "go_forward_or_reverse" | "maneuver_finished_take_back_control" | "engage_rear_gear" | "stop_after_rear_gear_engaged" | "accelerate_and_hold_the_pedal_pressed" | "maneuver_finished_release_the_accelerator_pedal" | "hold_the_accelerator_pedal_pressed" | "stand_by_no_text" | "unavailable"
```

|attribute|specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
|---------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|type     |`string`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
|default  |`"select_side"`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
|enum     |`"select_side"`, `"drive_forward_to_find_parking_slot"`, `"drive_forward_slot_suitable"`, `"stop"`, `"select_reverse_gear_or_press_start_button"`, `"select_or_engage_forward_gear"`, `"drive_forward"`, `"reverse"`, `"maneuver_complete_or_finished"`, `"go_forward_or_reverse"`, `"maneuver_finished_take_back_control"`, `"engage_rear_gear"`, `"stop_after_rear_gear_engaged"`, `"accelerate_and_hold_the_pedal_pressed"`, `"maneuver_finished_release_the_accelerator_pedal"`, `"hold_the_accelerator_pedal_pressed"`, `"stand_by_no_text"`, `"unavailable"`|

<a id="type_ManeuverMove"></a>

### ManeuverMove

```ts
type ManeuverMove = "first" | "backward" | "forward" | "unavailable"
```

|attribute|specification                                        |
|---------|-----------------------------------------------------|
|type     |`string`                                             |
|default  |`"first"`                                            |
|enum     |`"first"`, `"backward"`, `"forward"`, `"unavailable"`|

<a id="type_ManeuverSelection"></a>

### ManeuverSelection

```ts
type ManeuverSelection = "selected" | "not selected" | "unavailable"
```

Auto Park Assist maneuver selection
- `selected` maneuver not selected
- `not selected` maneuver selected
- `unavailable` maneuver unavailable

|attribute|specification                                  |
|---------|-----------------------------------------------|
|type     |`string`                                       |
|default  |`"selected"`                                   |
|enum     |`"selected"`, `"not selected"`, `"unavailable"`|

<a id="type_ManeuverStartSwitch"></a>

### ManeuverStartSwitch

```ts
type ManeuverStartSwitch = "none" | "unusable start" | "display start" | "display start auto mode" | "display start parallel" | "display start perpendicular" | "display start parkout" | "display cancel"
```

Auto Park Assist maneuver start switch

- 'none' maneuver start switch is not displayed or is unavailable
- 'unusable start' maneuver start switch is displayed as start but is not enabled
- 'display start' maneuver start switch is enabled and displayed as start
- 'display start' Maneuver start switch is enabled and displayed as start (auto mode)
- 'display start parallel' maneuver start switch is enabled and displayed as start (parallel mode)
- 'display start perpendicular' maneuver start switch is enabled and displayed as start (perpendicular mode)
- 'display start parkout' maneuver start switch is enabled and displayed as start (park out mode)
- 'display cancel' maneuver start switch is enabled and displayed as cancel

|attribute|specification                                                                                                                                                                           |
|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|type     |`string`                                                                                                                                                                                |
|default  |`"none"`                                                                                                                                                                                |
|enum     |`"none"`, `"unusable start"`, `"display start"`, `"display start auto mode"`, `"display start parallel"`, `"display start perpendicular"`, `"display start parkout"`, `"display cancel"`|

<a id="type_ManeuverType"></a>

### ManeuverType

```ts
type ManeuverType = "parallel" | "perpendicular" | "angled" | "parkout" | "auto mode" | "unavailable"
```

Auto Park Assist maneuver types
- `parallel` park in parallel
- `perpendicular` park in perpendicular
- `angled` park in angled
- `parkout` park out
- `auto mode` apa selects automatically the maneuver
- `unavailable` no maneuver move

|attribute|specification                                                                           |
|---------|----------------------------------------------------------------------------------------|
|type     |`string`                                                                                |
|default  |`"parallel"`                                                                            |
|enum     |`"parallel"`, `"perpendicular"`, `"angled"`, `"parkout"`, `"auto mode"`, `"unavailable"`|

<a id="type_ScanningSide"></a>

### ScanningSide

```ts
type ScanningSide = "scanning_side_none" | "scanning_side_right" | "scanning_side_left" | "scanning_side_unavailable"
```

|attribute|specification                                                                                         |
|---------|------------------------------------------------------------------------------------------------------|
|type     |`string`                                                                                              |
|default  |`"scanning_side_none"`                                                                                |
|enum     |`"scanning_side_none"`, `"scanning_side_right"`, `"scanning_side_left"`, `"scanning_side_unavailable"`|

<a id="type_ViewMask"></a>

### ViewMask

```ts
type ViewMask = "unavailable" | "requested"
```

|attribute|specification                 |
|---------|------------------------------|
|type     |`string`                      |
|default  |`"unavailable"`               |
|enum     |`"unavailable"`, `"requested"`|

