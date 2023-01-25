---
# Copyright (c) 2019 Renault SW Labs
#
# Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
# intellectual property rights. Use of this software is subject to a specific
# license granted by RENAULT S.A.S.
disclaimer: 'This is a generated file: do not modify.'
title: Hfp Scanning View Model
lang: en-US
namespace: com/renault/parkassist/viewmodel/apa/hfp
description: |-
  Handles automatic park assist state.

  _Automatic Parking Assist  - Feature ID : _
---

# Hfp Scanning View Model

Handles automatic park assist state.

_Automatic Parking Assist  - Feature ID : _

|property  |specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|namespace |`com/renault/parkassist/viewmodel/apa/hfp`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
|name      |`HfpScanningViewModel`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
|properties|[backgroundResource](#prop_backgroundResource), [carFrontArrowResourceVisible](#prop_carFrontArrowResourceVisible), [carFrontStopResourceVisible](#prop_carFrontStopResourceVisible), [displayParkout](#prop_displayParkout), [instruction](#prop_instruction), [leftIndicatorSelected](#prop_leftIndicatorSelected), [leftParkingSlotParallelVisible](#prop_leftParkingSlotParallelVisible), [leftParkingSlotPerpendicularVisible](#prop_leftParkingSlotPerpendicularVisible), [leftSlotResource](#prop_leftSlotResource), [maneuverParallelButtonEnabled](#prop_maneuverParallelButtonEnabled), [maneuverParallelButtonSelected](#prop_maneuverParallelButtonSelected), [maneuverParkoutButtonEnabled](#prop_maneuverParkoutButtonEnabled), [maneuverParkoutButtonSelected](#prop_maneuverParkoutButtonSelected), [maneuverPerpendicularButtonEnabled](#prop_maneuverPerpendicularButtonEnabled), [maneuverPerpendicularButtonSelected](#prop_maneuverPerpendicularButtonSelected), [rearArrowResourceVisible](#prop_rearArrowResourceVisible), [rearLeftLongArrowVisible](#prop_rearLeftLongArrowVisible), [rearLeftShortArrowVisible](#prop_rearLeftShortArrowVisible), [rearRightLongArrowVisible](#prop_rearRightLongArrowVisible), [rearRightShortArrowVisible](#prop_rearRightShortArrowVisible), [rightIndicatorSelected](#prop_rightIndicatorSelected), [rightParkingSlotParallelVisible](#prop_rightParkingSlotParallelVisible), [rightParkingSlotPerpendicularVisible](#prop_rightParkingSlotPerpendicularVisible), [rightSlotResource](#prop_rightSlotResource), [sonarAvmVisible](#prop_sonarAvmVisible), [sonarRvcVisible](#prop_sonarRvcVisible), [upaDisabledApaScanning](#prop_upaDisabledApaScanning)|
|actions   |[setManeuver](#action_setManeuver), [start](#action_start), [stop](#action_stop)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
|types     |[Side](#type_Side)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |

> *Generated by midl v2.1.0 from 'ParkAssist/vm-specification/hfp-scanning.ts'.*

<a id="title_Properties"></a>

## Properties

<a id="prop_backgroundResource"></a>

### backgroundResource

```ts
backgroundResource: integer
```

The background parking slot to display

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_carFrontArrowResourceVisible"></a>

### carFrontArrowResourceVisible

```ts
carFrontArrowResourceVisible: boolean
```

Whether we have to display arrow front car

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_carFrontStopResourceVisible"></a>

### carFrontStopResourceVisible

```ts
carFrontStopResourceVisible: boolean
```

Whether we have to display stop front car

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_displayParkout"></a>

### displayParkout

```ts
displayParkout: boolean
```

Whether we display `hfp parkout confirmation` or not

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_instruction"></a>

### instruction

```ts
instruction: integer
```

The instruction the user needs to follow as a resource id.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_leftIndicatorSelected"></a>

### leftIndicatorSelected

```ts
leftIndicatorSelected: boolean
```

Whether we selected left scanning

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_leftParkingSlotParallelVisible"></a>

### leftParkingSlotParallelVisible

```ts
leftParkingSlotParallelVisible: boolean
```

Whether the left Parking Slot in Parallel Maneuver is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_leftParkingSlotPerpendicularVisible"></a>

### leftParkingSlotPerpendicularVisible

```ts
leftParkingSlotPerpendicularVisible: boolean
```

Whether the left Parking Slot in Perpendicular Maneuver is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_leftSlotResource"></a>

### leftSlotResource

```ts
leftSlotResource: integer
```

The left Parking Slot Resource to display

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_maneuverParallelButtonEnabled"></a>

### maneuverParallelButtonEnabled

```ts
maneuverParallelButtonEnabled: boolean
```

Current maneuver parallel button state

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_maneuverParallelButtonSelected"></a>

### maneuverParallelButtonSelected

```ts
maneuverParallelButtonSelected: boolean
```

Current maneuver parallel button selection state

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_maneuverParkoutButtonEnabled"></a>

### maneuverParkoutButtonEnabled

```ts
maneuverParkoutButtonEnabled: boolean
```

Current maneuver parkout button button state

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_maneuverParkoutButtonSelected"></a>

### maneuverParkoutButtonSelected

```ts
maneuverParkoutButtonSelected: boolean
```

Current maneuver parkout button selection state

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_maneuverPerpendicularButtonEnabled"></a>

### maneuverPerpendicularButtonEnabled

```ts
maneuverPerpendicularButtonEnabled: boolean
```

Current maneuver perpendicular button state

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_maneuverPerpendicularButtonSelected"></a>

### maneuverPerpendicularButtonSelected

```ts
maneuverPerpendicularButtonSelected: boolean
```

Current maneuver perpendicular button selection state

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rearArrowResourceVisible"></a>

### rearArrowResourceVisible

```ts
rearArrowResourceVisible: boolean
```

Whether we have to display rear arrow

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rearLeftLongArrowVisible"></a>

### rearLeftLongArrowVisible

```ts
rearLeftLongArrowVisible: boolean
```

Whether we have to display rear Left Long Arrow

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rearLeftShortArrowVisible"></a>

### rearLeftShortArrowVisible

```ts
rearLeftShortArrowVisible: boolean
```

Whether we have to display rear Left Short Arrow

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rearRightLongArrowVisible"></a>

### rearRightLongArrowVisible

```ts
rearRightLongArrowVisible: boolean
```

Whether we have to display rear Right Long Arrow

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rearRightShortArrowVisible"></a>

### rearRightShortArrowVisible

```ts
rearRightShortArrowVisible: boolean
```

Whether we have to display rear Right Short Arrow

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rightIndicatorSelected"></a>

### rightIndicatorSelected

```ts
rightIndicatorSelected: boolean
```

Whether we selected right scanning

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rightParkingSlotParallelVisible"></a>

### rightParkingSlotParallelVisible

```ts
rightParkingSlotParallelVisible: boolean
```

Whether the right Parking Slot in Parallel Maneuver is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rightParkingSlotPerpendicularVisible"></a>

### rightParkingSlotPerpendicularVisible

```ts
rightParkingSlotPerpendicularVisible: boolean
```

Whether the right Parking Slot in Perpendicular Maneuver is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rightSlotResource"></a>

### rightSlotResource

```ts
rightSlotResource: integer
```

The right Parking Slot Resource to display

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_sonarAvmVisible"></a>

### sonarAvmVisible

```ts
sonarAvmVisible: boolean
```

Listen to Surround State to handle avm sonar visibility when speed too high

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_sonarRvcVisible"></a>

### sonarRvcVisible

```ts
sonarRvcVisible: boolean
```

Listen to DisplayRequest to handle rvc sonar visibility when speed too high

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_upaDisabledApaScanning"></a>

### upaDisabledApaScanning

```ts
upaDisabledApaScanning: boolean
```

Whether upa sensors are disabled in apa scanning

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="title_Actions"></a>

## Actions

<a id="action_setManeuver"></a>

### setManeuver

```ts
setManeuver(maneuverType: integer);
```

Switch the maneuver mode.

#### Parameter

- `maneuverType`

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

<a id="action_start"></a>

### start

```ts
start();
```

This function is the entry point for Apa camera scenarios.
It will decide what mode to set by default.
To be called when lifecycle is attached to viewModels livedatas

<a id="action_stop"></a>

### stop

```ts
stop();
```

This function is the exit point for Apa camera scenarios.
It will decide what how to properly leave Apa.
To be called when lifecycle is detached to viewModels livedatas

<a id="title_Types"></a>

## Types

<a id="type_Side"></a>

### Side

```ts
type Side = "straight" | "left" | "right" | "leftRight"
```

Parking Side where slots are suitable or selected

|attribute|specification                                   |
|---------|------------------------------------------------|
|type     |`string`                                        |
|default  |`"straight"`                                    |
|enum     |`"straight"`, `"left"`, `"right"`, `"leftRight"`|

