---
# Copyright (c) 2019 Renault SW Labs
#
# Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
# intellectual property rights. Use of this software is subject to a specific
# license granted by RENAULT S.A.S.
disclaimer: 'This is a generated file: do not modify.'
title: Fapk View Model
lang: en-US
namespace: com/renault/parkassist/viewmodel/apa/fapk
description: |-
  Handles full automatic park assist state.

  _Automatic Parking Assist  - Feature ID : _
---

# Fapk View Model

Handles full automatic park assist state.

_Automatic Parking Assist  - Feature ID : _

|property  |specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|namespace |`com/renault/parkassist/viewmodel/apa/fapk`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|name      |`FapkViewModel`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|properties|[instruction](#prop_instruction), [maneuverParallelButtonEnabled](#prop_maneuverParallelButtonEnabled), [maneuverParallelButtonSelected](#prop_maneuverParallelButtonSelected), [maneuverParallelVisible](#prop_maneuverParallelVisible), [maneuverParkoutButtonEnabled](#prop_maneuverParkoutButtonEnabled), [maneuverParkoutButtonSelected](#prop_maneuverParkoutButtonSelected), [maneuverParkoutVisible](#prop_maneuverParkoutVisible), [maneuverPerpendicularButtonEnabled](#prop_maneuverPerpendicularButtonEnabled), [maneuverPerpendicularButtonSelected](#prop_maneuverPerpendicularButtonSelected), [maneuverPerpendicularVisible](#prop_maneuverPerpendicularVisible), [maneuverStartSwitchButtonEnabled](#prop_maneuverStartSwitchButtonEnabled), [maneuverStartSwitchButtonSelected](#prop_maneuverStartSwitchButtonSelected), [maneuverStartSwitchButtonVisible](#prop_maneuverStartSwitchButtonVisible), [maneuverStopSwitchButtonSelected](#prop_maneuverStopSwitchButtonSelected), [maneuverStopSwitchButtonVisible](#prop_maneuverStopSwitchButtonVisible), [settingsVisible](#prop_settingsVisible)|
|actions   |[maneuverStart](#action_maneuverStart), [maneuverStop](#action_maneuverStop), [setManeuver](#action_setManeuver)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |

> *Generated by midl v2.1.0 from 'ParkAssist/vm-specification/fapk.ts'.*

<a id="title_Properties"></a>

## Properties

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

<a id="prop_maneuverParallelVisible"></a>

### maneuverParallelVisible

```ts
maneuverParallelVisible: boolean
```

Whether we display parallel button

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
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

<a id="prop_maneuverParkoutVisible"></a>

### maneuverParkoutVisible

```ts
maneuverParkoutVisible: boolean
```

Whether we display parkout button

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
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

<a id="prop_maneuverPerpendicularVisible"></a>

### maneuverPerpendicularVisible

```ts
maneuverPerpendicularVisible: boolean
```

Whether we display perpendicular button

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`false`      |

<a id="prop_maneuverStartSwitchButtonEnabled"></a>

### maneuverStartSwitchButtonEnabled

```ts
maneuverStartSwitchButtonEnabled: boolean
```

Current maneuver start switch button enabled state

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_maneuverStartSwitchButtonSelected"></a>

### maneuverStartSwitchButtonSelected

```ts
maneuverStartSwitchButtonSelected: boolean
```

Current maneuver start switch button selection state

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_maneuverStartSwitchButtonVisible"></a>

### maneuverStartSwitchButtonVisible

```ts
maneuverStartSwitchButtonVisible: boolean
```

Current maneuver start switch button visibility state

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_maneuverStopSwitchButtonSelected"></a>

### maneuverStopSwitchButtonSelected

```ts
maneuverStopSwitchButtonSelected: boolean
```

Current maneuver stop switch button selection state

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_maneuverStopSwitchButtonVisible"></a>

### maneuverStopSwitchButtonVisible

```ts
maneuverStopSwitchButtonVisible: boolean
```

Current maneuver stop switch button visibility state

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

Whether we display settings icon or not

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="title_Actions"></a>

## Actions

<a id="action_maneuverStart"></a>

### maneuverStart

```ts
maneuverStart();
```

Start the maneuver if all conditions boolean met

<a id="action_maneuverStop"></a>

### maneuverStop

```ts
maneuverStop();
```

Stop the maneuver if all conditions boolean met

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

