---
# Copyright (c) 2019 Renault SW Labs
#
# Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
# intellectual property rights. Use of this software is subject to a specific
# license granted by RENAULT S.A.S.
disclaimer: 'This is a generated file: do not modify.'
title: Hfp Guidance View Model
lang: en-US
namespace: com/renault/parkassist/viewmodel/apa/hfp
description: Easy Park Assist (APA) guidance state
---

# Hfp Guidance View Model

Easy Park Assist (APA) guidance state

|property  |specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|namespace |`com/renault/parkassist/viewmodel/apa/hfp`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|name      |`HfpGuidanceViewModel`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
|properties|[backgroundParallelLeftVisible](#prop_backgroundParallelLeftVisible), [backgroundParallelRightVisible](#prop_backgroundParallelRightVisible), [backgroundPerpendicularCenterVisible](#prop_backgroundPerpendicularCenterVisible), [extendedInstruction](#prop_extendedInstruction), [gaugeColor](#prop_gaugeColor), [gaugeVisible](#prop_gaugeVisible), [isCameraVisible](#prop_isCameraVisible), [isForwardGauge](#prop_isForwardGauge), [maneuverCompletion](#prop_maneuverCompletion), [parallelLeftParkVisible](#prop_parallelLeftParkVisible), [parallelLeftVehicleCenterBackVisible](#prop_parallelLeftVehicleCenterBackVisible), [parallelLeftVehicleCenterCutVisible](#prop_parallelLeftVehicleCenterCutVisible), [parallelLeftVehicleCenterFrontVisible](#prop_parallelLeftVehicleCenterFrontVisible), [parallelLeftVehicleCenterVisible](#prop_parallelLeftVehicleCenterVisible), [parallelRightParkVisible](#prop_parallelRightParkVisible), [parallelRightVehicleCenterBackVisible](#prop_parallelRightVehicleCenterBackVisible), [parallelRightVehicleCenterCutVisible](#prop_parallelRightVehicleCenterCutVisible), [parallelRightVehicleCenterFrontVisible](#prop_parallelRightVehicleCenterFrontVisible), [parallelRightVehicleCenterVisible](#prop_parallelRightVehicleCenterVisible), [parkoutLeftVehicleLeftVisible](#prop_parkoutLeftVehicleLeftVisible), [parkoutRightVehicleRightVisible](#prop_parkoutRightVehicleRightVisible), [perpendicularLeftVehicleCenterCutVisible](#prop_perpendicularLeftVehicleCenterCutVisible), [perpendicularRightVehicleCenterCutVisible](#prop_perpendicularRightVehicleCenterCutVisible), [perpendicularVehicleCenterBackStopBackVisible](#prop_perpendicularVehicleCenterBackStopBackVisible), [perpendicularVehicleCenterBackVisible](#prop_perpendicularVehicleCenterBackVisible), [perpendicularVehicleCenterFrontStopFrontVisible](#prop_perpendicularVehicleCenterFrontStopFrontVisible), [perpendicularVehicleCenterFrontVisible](#prop_perpendicularVehicleCenterFrontVisible), [perpendicularVehicleCenterParkVisible](#prop_perpendicularVehicleCenterParkVisible), [raebSonarOffVisible](#prop_raebSonarOffVisible)|
|actions   |[requestCameraSwitch](#action_requestCameraSwitch)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |

> *Generated by midl v2.1.0 from 'ParkAssist/vm-specification/hfp-guidance.ts'.*

<a id="title_Properties"></a>

## Properties

<a id="prop_backgroundParallelLeftVisible"></a>

### backgroundParallelLeftVisible

```ts
backgroundParallelLeftVisible: boolean
```

Whether the background Parallel Left is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_backgroundParallelRightVisible"></a>

### backgroundParallelRightVisible

```ts
backgroundParallelRightVisible: boolean
```

Whether the background Parallel Right is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_backgroundPerpendicularCenterVisible"></a>

### backgroundPerpendicularCenterVisible

```ts
backgroundPerpendicularCenterVisible: boolean
```

Whether the background Perpendicular Center is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_extendedInstruction"></a>

### extendedInstruction

```ts
extendedInstruction: integer
```

Current text resource id for apa guidance instruction

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_gaugeColor"></a>

### gaugeColor

```ts
gaugeColor: integer
```

The color of the maneuver progress bar (gauge)

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_gaugeVisible"></a>

### gaugeVisible

```ts
gaugeVisible: boolean
```

Whether apa guidance maneuver progress (gauge) is Visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_isCameraVisible"></a>

### isCameraVisible

```ts
isCameraVisible: boolean
```

Whether avm display is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_isForwardGauge"></a>

### isForwardGauge

```ts
isForwardGauge: boolean
```

Gauge shall take forward shape if true, backward shape else

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

Current apa guidance maneuver completion

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_parallelLeftParkVisible"></a>

### parallelLeftParkVisible

```ts
parallelLeftParkVisible: boolean
```

Whether the Park finished icon is visible with parallel left background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parallelLeftVehicleCenterBackVisible"></a>

### parallelLeftVehicleCenterBackVisible

```ts
parallelLeftVehicleCenterBackVisible: boolean
```

Whether the vehicle center Back is visible with parallel left background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parallelLeftVehicleCenterCutVisible"></a>

### parallelLeftVehicleCenterCutVisible

```ts
parallelLeftVehicleCenterCutVisible: boolean
```

Whether the vehicle center Cut and Parking Large is visible with parallel left background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parallelLeftVehicleCenterFrontVisible"></a>

### parallelLeftVehicleCenterFrontVisible

```ts
parallelLeftVehicleCenterFrontVisible: boolean
```

Whether the vehicle center Front is visible with parallel left background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parallelLeftVehicleCenterVisible"></a>

### parallelLeftVehicleCenterVisible

```ts
parallelLeftVehicleCenterVisible: boolean
```

Whether the vehicle center is visible with parallel left background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parallelRightParkVisible"></a>

### parallelRightParkVisible

```ts
parallelRightParkVisible: boolean
```

Whether the Park finished icon is visible with parallel right background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parallelRightVehicleCenterBackVisible"></a>

### parallelRightVehicleCenterBackVisible

```ts
parallelRightVehicleCenterBackVisible: boolean
```

Whether the vehicle center Back is visible with parallel right background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parallelRightVehicleCenterCutVisible"></a>

### parallelRightVehicleCenterCutVisible

```ts
parallelRightVehicleCenterCutVisible: boolean
```

Whether the vehicle center Cut and Parking Large is visible with parallel right background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parallelRightVehicleCenterFrontVisible"></a>

### parallelRightVehicleCenterFrontVisible

```ts
parallelRightVehicleCenterFrontVisible: boolean
```

Whether the vehicle center Front is visible with parallel right background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parallelRightVehicleCenterVisible"></a>

### parallelRightVehicleCenterVisible

```ts
parallelRightVehicleCenterVisible: boolean
```

Whether the vehicle center is visible with parallel right background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parkoutLeftVehicleLeftVisible"></a>

### parkoutLeftVehicleLeftVisible

```ts
parkoutLeftVehicleLeftVisible: boolean
```

Whether the vehicle left is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_parkoutRightVehicleRightVisible"></a>

### parkoutRightVehicleRightVisible

```ts
parkoutRightVehicleRightVisible: boolean
```

Whether the vehicle right is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_perpendicularLeftVehicleCenterCutVisible"></a>

### perpendicularLeftVehicleCenterCutVisible

```ts
perpendicularLeftVehicleCenterCutVisible: boolean
```

Whether the vehicle center Cut and Parking Large is visible with perp left background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_perpendicularRightVehicleCenterCutVisible"></a>

### perpendicularRightVehicleCenterCutVisible

```ts
perpendicularRightVehicleCenterCutVisible: boolean
```

Whether the vehicle center Cut and Parking Large is visible with perp right background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_perpendicularVehicleCenterBackStopBackVisible"></a>

### perpendicularVehicleCenterBackStopBackVisible

```ts
perpendicularVehicleCenterBackStopBackVisible: boolean
```

Whether the stop back is active in vehicle center back view with perpendicular background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_perpendicularVehicleCenterBackVisible"></a>

### perpendicularVehicleCenterBackVisible

```ts
perpendicularVehicleCenterBackVisible: boolean
```

Whether the vehicle center back is visible in perpendicular background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_perpendicularVehicleCenterFrontStopFrontVisible"></a>

### perpendicularVehicleCenterFrontStopFrontVisible

```ts
perpendicularVehicleCenterFrontStopFrontVisible: boolean
```

Whether the stop front is active in vehicle center front view with perpendicular background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_perpendicularVehicleCenterFrontVisible"></a>

### perpendicularVehicleCenterFrontVisible

```ts
perpendicularVehicleCenterFrontVisible: boolean
```

Whether the vehicle center front is visible in perpendicular background

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_perpendicularVehicleCenterParkVisible"></a>

### perpendicularVehicleCenterParkVisible

```ts
perpendicularVehicleCenterParkVisible: boolean
```

Whether the vehicle center park is visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_raebSonarOffVisible"></a>

### raebSonarOffVisible

```ts
raebSonarOffVisible: boolean
```

Raeb Sonar Off Visible

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="title_Actions"></a>

## Actions

<a id="action_requestCameraSwitch"></a>

### requestCameraSwitch

```ts
requestCameraSwitch(cameraOn: boolean);
```

Requests the camera switch On/Off

#### Parameter

- `cameraOn`: true/false

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

