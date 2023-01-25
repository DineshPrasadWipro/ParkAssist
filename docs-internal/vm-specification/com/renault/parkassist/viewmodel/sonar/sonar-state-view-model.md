---
# Copyright (c) 2019 Renault SW Labs
#
# Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
# intellectual property rights. Use of this software is subject to a specific
# license granted by RENAULT S.A.S.
disclaimer: 'This is a generated file: do not modify.'
title: Sonar State View Model
lang: en-US
namespace: com/renault/parkassist/viewmodel/sonar
description: |-
  Control parking sensors activation and report
  collision detection information.

  _Ultrasonic Parking Assist  - Feature ID : 10905_
---

# Sonar State View Model

Control parking sensors activation and report
collision detection information.

_Ultrasonic Parking Assist  - Feature ID : 10905_

|property  |specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|namespace |`com/renault/parkassist/viewmodel/sonar`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
|name      |`SonarStateViewModel`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
|properties|[avatarVisible](#prop_avatarVisible), [closeVisible](#prop_closeVisible), [flankEnabled](#prop_flankEnabled), [frontCenter](#prop_frontCenter), [frontEnabled](#prop_frontEnabled), [frontLeft](#prop_frontLeft), [frontRight](#prop_frontRight), [leftFront](#prop_leftFront), [leftFrontCenter](#prop_leftFrontCenter), [leftRear](#prop_leftRear), [leftRearCenter](#prop_leftRearCenter), [obstacle](#prop_obstacle), [rearCenter](#prop_rearCenter), [rearEnabled](#prop_rearEnabled), [rearLeft](#prop_rearLeft), [rearRight](#prop_rearRight), [rightFront](#prop_rightFront), [rightFrontCenter](#prop_rightFrontCenter), [rightRear](#prop_rightRear), [rightRearCenter](#prop_rightRearCenter)|
|actions   |[enableFlank](#action_enableFlank), [enableFront](#action_enableFront), [enableRear](#action_enableRear)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
|types     |[ParkingSensor](#type_ParkingSensor), [SensorLevel](#type_SensorLevel)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |

> *Generated by midl v2.1.0 from 'ParkAssist/vm-specification/sonar-state.ts'.*

<a id="title_Properties"></a>

## Properties

<a id="prop_avatarVisible"></a>

### avatarVisible

```ts
avatarVisible: boolean
```

Whether car avatar should be displayed

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

<a id="prop_flankEnabled"></a>

### flankEnabled

```ts
flankEnabled: boolean
```

Whether flank sensors are enabled

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_frontCenter"></a>

### frontCenter

```ts
frontCenter: ParkingSensor
```

Front center UPA Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_frontEnabled"></a>

### frontEnabled

```ts
frontEnabled: boolean
```

Whether front sensors are enabled

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_frontLeft"></a>

### frontLeft

```ts
frontLeft: ParkingSensor
```

Front left UPA Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_frontRight"></a>

### frontRight

```ts
frontRight: ParkingSensor
```

Front right UPA Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_leftFront"></a>

### leftFront

```ts
leftFront: ParkingSensor
```

Left front FKP Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_leftFrontCenter"></a>

### leftFrontCenter

```ts
leftFrontCenter: ParkingSensor
```

Left front-center FKP Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_leftRear"></a>

### leftRear

```ts
leftRear: ParkingSensor
```

Left rear FKP Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_leftRearCenter"></a>

### leftRearCenter

```ts
leftRearCenter: ParkingSensor
```

Left rear-center FKP Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_obstacle"></a>

### obstacle

```ts
obstacle: boolean
```

Whether an obstacle has been detected on the vehicle path

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rearCenter"></a>

### rearCenter

```ts
rearCenter: ParkingSensor
```

Rear center UPA Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_rearEnabled"></a>

### rearEnabled

```ts
rearEnabled: boolean
```

Whether rear sensors are enabled

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_rearLeft"></a>

### rearLeft

```ts
rearLeft: ParkingSensor
```

Rear left UPA Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_rearRight"></a>

### rearRight

```ts
rearRight: ParkingSensor
```

Rear right UPA Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_rightFront"></a>

### rightFront

```ts
rightFront: ParkingSensor
```

Right front FKP Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_rightFrontCenter"></a>

### rightFrontCenter

```ts
rightFrontCenter: ParkingSensor
```

Right front-center FKP Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_rightRear"></a>

### rightRear

```ts
rightRear: ParkingSensor
```

Right rear FKP Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="prop_rightRearCenter"></a>

### rightRearCenter

```ts
rightRearCenter: ParkingSensor
```

Right rear-center FKP Sensor state

|attribute|specification                          |
|---------|---------------------------------------|
|type     |[ParkingSensor](#type_ParkingSensor)   |
|required |`Yes`                                  |
|final    |`No`                                   |
|default  |`{"hatched":false,"level":"invisible"}`|

<a id="title_Actions"></a>

## Actions

<a id="action_enableFlank"></a>

### enableFlank

```ts
enableFlank(enable: boolean);
```

Activate or de-activate flank sensors

#### Parameter

- `enable`: true to enable, false to disable

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="action_enableFront"></a>

### enableFront

```ts
enableFront(enable: boolean);
```

Activate or de-activate front sensors

#### Parameter

- `enable`: true to enable, false to disable

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="action_enableRear"></a>

### enableRear

```ts
enableRear(enable: boolean);
```

Activate or de-activate rear sensors

#### Parameter

- `enable`: true to enable, false to disable

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="title_Types"></a>

## Types

<a id="type_ParkingSensor"></a>

### ParkingSensor

```ts
type ParkingSensor = {
  hatched: boolean;
  level: SensorLevel
}
```

Parking sensor state

|attribute |specification                                                             |
|----------|--------------------------------------------------------------------------|
|type      |`object`                                                                  |
|default   |`{"hatched":false,"level":"invisible"}`                                   |
|properties|[hatched](#prop_ParkingSensor_hatched), [level](#prop_ParkingSensor_level)|

<a id="prop_ParkingSensor_hatched"></a>

#### hatched

```ts
hatched: boolean
```

Hatched state for a single FKP Sensor.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|default  |`false`      |

<a id="prop_ParkingSensor_level"></a>

#### level

```ts
level: SensorLevel
```

Sensor level.

|attribute|specification                   |
|---------|--------------------------------|
|type     |[SensorLevel](#type_SensorLevel)|
|required |`Yes`                           |
|default  |`"invisible"`                   |

<a id="type_SensorLevel"></a>

### SensorLevel

```ts
type SensorLevel = "invisible" | "greyed" | "very_far" | "far" | "medium" | "close" | "very_close"
```

Sensor level enumeration.

|attribute|specification                                                                          |
|---------|---------------------------------------------------------------------------------------|
|type     |`string`                                                                               |
|default  |`"invisible"`                                                                          |
|enum     |`"invisible"`, `"greyed"`, `"very_far"`, `"far"`, `"medium"`, `"close"`, `"very_close"`|

