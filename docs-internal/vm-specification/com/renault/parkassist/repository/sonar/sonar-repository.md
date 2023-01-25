---
# Copyright (c) 2019 Renault SW Labs
#
# Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
# intellectual property rights. Use of this software is subject to a specific
# license granted by RENAULT S.A.S.
disclaimer: 'This is a generated file: do not modify.'
title: Sonar Repository
lang: en-US
namespace: com/renault/parkassist/repository/sonar
description: Exposes sonar service as livedatas.
---

# Sonar Repository

Exposes sonar service as livedatas.

|property  |specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|namespace |`com/renault/parkassist/repository/sonar`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
|name      |`SonarRepository`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|properties|[closeAllowed](#prop_closeAllowed), [collisionAlertEnabled](#prop_collisionAlertEnabled), [collisionAlertLevel](#prop_collisionAlertLevel), [collisionAlertSide](#prop_collisionAlertSide), [displayRequest](#prop_displayRequest), [fkpDisplayRequest](#prop_fkpDisplayRequest), [fkpFeaturePresent](#prop_fkpFeaturePresent), [flankState](#prop_flankState), [frontCenter](#prop_frontCenter), [frontLeft](#prop_frontLeft), [frontRight](#prop_frontRight), [frontState](#prop_frontState), [leftFront](#prop_leftFront), [leftFrontCenter](#prop_leftFrontCenter), [leftRear](#prop_leftRear), [leftRearCenter](#prop_leftRearCenter), [obstacle](#prop_obstacle), [raebAlertEnabled](#prop_raebAlertEnabled), [raebAlertState](#prop_raebAlertState), [raebFeaturePresent](#prop_raebFeaturePresent), [rctaFeaturePresent](#prop_rctaFeaturePresent), [rearCenter](#prop_rearCenter), [rearLeft](#prop_rearLeft), [rearRight](#prop_rearRight), [rearState](#prop_rearState), [rearUpaActivationSettingPresent](#prop_rearUpaActivationSettingPresent), [rightFront](#prop_rightFront), [rightFrontCenter](#prop_rightFrontCenter), [rightRear](#prop_rightRear), [rightRearCenter](#prop_rightRearCenter), [upaDisplayRequest](#prop_upaDisplayRequest), [upaFkpVisualFeedbackFeaturePresent](#prop_upaFkpVisualFeedbackFeaturePresent), [upaFrontFeaturePresent](#prop_upaFrontFeaturePresent), [upaRearFeaturePresent](#prop_upaRearFeaturePresent)|
|actions   |[enableCollisionAlert](#action_enableCollisionAlert), [enableRearAutoEmergencyBreak](#action_enableRearAutoEmergencyBreak), [setSonarGroup](#action_setSonarGroup)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
|types     |[DisplayType](#type_DisplayType), [FkpDisplayRequestType](#type_FkpDisplayRequestType), [GroupState](#type_GroupState), [SensorState](#type_SensorState), [UpaDisplayRequestType](#type_UpaDisplayRequestType)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |

> *Generated by midl v2.1.0 from 'ParkAssist/vm-specification/repository/sonar-repository.ts'.*

<a id="title_Properties"></a>

## Properties

<a id="prop_closeAllowed"></a>

### closeAllowed

```ts
closeAllowed: boolean
```

Close display allowed.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_collisionAlertEnabled"></a>

### collisionAlertEnabled

```ts
collisionAlertEnabled: boolean
```

Collision alert enabled.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_collisionAlertLevel"></a>

### collisionAlertLevel

```ts
collisionAlertLevel: integer
```

Collision alert level.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_collisionAlertSide"></a>

### collisionAlertSide

```ts
collisionAlertSide: integer
```

Collision alert side.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_displayRequest"></a>

### displayRequest

```ts
displayRequest: DisplayType
```

Current surround view displayed screen.

|attribute|specification                   |
|---------|--------------------------------|
|type     |[DisplayType](#type_DisplayType)|
|required |`Yes`                           |
|final    |`No`                            |
|default  |`"none"`                        |

<a id="prop_fkpDisplayRequest"></a>

### fkpDisplayRequest

```ts
fkpDisplayRequest: FkpDisplayRequestType
```

FKP view requested by the service.

|attribute|specification                                       |
|---------|----------------------------------------------------|
|type     |[FkpDisplayRequestType](#type_FkpDisplayRequestType)|
|required |`Yes`                                               |
|final    |`No`                                                |
|default  |`"no_display"`                                      |

<a id="prop_fkpFeaturePresent"></a>

### fkpFeaturePresent

```ts
fkpFeaturePresent: boolean
```

Indicate whether FlanK Protection feature is part of vehicle configuration.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`false`      |

<a id="prop_flankState"></a>

### flankState

```ts
flankState: GroupState
```

Flank sensor group

|attribute|specification                 |
|---------|------------------------------|
|type     |[GroupState](#type_GroupState)|
|required |`Yes`                         |
|final    |`No`                          |
|default  |`"disabled"`                  |

<a id="prop_frontCenter"></a>

### frontCenter

```ts
frontCenter: SensorState
```

Front center sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_frontLeft"></a>

### frontLeft

```ts
frontLeft: SensorState
```

Front left sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_frontRight"></a>

### frontRight

```ts
frontRight: SensorState
```

Front right sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_frontState"></a>

### frontState

```ts
frontState: GroupState
```

Front sensor group

|attribute|specification                 |
|---------|------------------------------|
|type     |[GroupState](#type_GroupState)|
|required |`Yes`                         |
|final    |`No`                          |
|default  |`"disabled"`                  |

<a id="prop_leftFront"></a>

### leftFront

```ts
leftFront: SensorState
```

Left front sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_leftFrontCenter"></a>

### leftFrontCenter

```ts
leftFrontCenter: SensorState
```

Left front-center sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_leftRear"></a>

### leftRear

```ts
leftRear: SensorState
```

Left rear sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_leftRearCenter"></a>

### leftRearCenter

```ts
leftRearCenter: SensorState
```

Left rear-center sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_obstacle"></a>

### obstacle

```ts
obstacle: boolean
```

Obstacle detection.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_raebAlertEnabled"></a>

### raebAlertEnabled

```ts
raebAlertEnabled: boolean
```

Rear Auto Emergency Break alert enabled.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`false`      |

<a id="prop_raebAlertState"></a>

### raebAlertState

```ts
raebAlertState: integer
```

Rear Auto Emergency Break alert level.

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|final    |`No`         |
|default  |`0`          |

<a id="prop_raebFeaturePresent"></a>

### raebFeaturePresent

```ts
raebFeaturePresent: boolean
```

Indicate whether Rear Automotive Emergency Breaking feature is part of vehicle configuration.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`false`      |

<a id="prop_rctaFeaturePresent"></a>

### rctaFeaturePresent

```ts
rctaFeaturePresent: boolean
```

Indicate whether Rear Cross Traffic Alert feature is part of vehicle configuration.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`false`      |

<a id="prop_rearCenter"></a>

### rearCenter

```ts
rearCenter: SensorState
```

Rear center sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_rearLeft"></a>

### rearLeft

```ts
rearLeft: SensorState
```

Rear left sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_rearRight"></a>

### rearRight

```ts
rearRight: SensorState
```

Rear right sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_rearState"></a>

### rearState

```ts
rearState: GroupState
```

Rear sensor group

|attribute|specification                 |
|---------|------------------------------|
|type     |[GroupState](#type_GroupState)|
|required |`Yes`                         |
|final    |`No`                          |
|default  |`"disabled"`                  |

<a id="prop_rearUpaActivationSettingPresent"></a>

### rearUpaActivationSettingPresent

```ts
rearUpaActivationSettingPresent: boolean
```

Indicate whether Ultrasonic Park Assist Visual Settings feature is part of vehicle configuration.
If true, 'rear' sonar can be (de)activated in the settings.
Else, 'rear' sonar activation cannot be change by the user

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`false`      |

<a id="prop_rightFront"></a>

### rightFront

```ts
rightFront: SensorState
```

Right front sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_rightFrontCenter"></a>

### rightFrontCenter

```ts
rightFrontCenter: SensorState
```

Right front-center sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_rightRear"></a>

### rightRear

```ts
rightRear: SensorState
```

Right rear sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_rightRearCenter"></a>

### rightRearCenter

```ts
rightRearCenter: SensorState
```

Right rear-center sensor

|attribute|specification                                                          |
|---------|-----------------------------------------------------------------------|
|type     |[SensorState](#type_SensorState)                                       |
|required |`Yes`                                                                  |
|final    |`No`                                                                   |
|default  |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`|

<a id="prop_upaDisplayRequest"></a>

### upaDisplayRequest

```ts
upaDisplayRequest: UpaDisplayRequestType
```

UPA view requested by the service.

|attribute|specification                                       |
|---------|----------------------------------------------------|
|type     |[UpaDisplayRequestType](#type_UpaDisplayRequestType)|
|required |`Yes`                                               |
|final    |`No`                                                |
|default  |`"no_display"`                                      |

<a id="prop_upaFkpVisualFeedbackFeaturePresent"></a>

### upaFkpVisualFeedbackFeaturePresent

```ts
upaFkpVisualFeedbackFeaturePresent: boolean
```

Indicate whether Ultrasonic Park Assist Visual Feedback feature is part of vehicle configuration.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`false`      |

<a id="prop_upaFrontFeaturePresent"></a>

### upaFrontFeaturePresent

```ts
upaFrontFeaturePresent: boolean
```

Indicate whether Ultrasonic Park Assist Front feature is part of vehicle configuration.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`false`      |

<a id="prop_upaRearFeaturePresent"></a>

### upaRearFeaturePresent

```ts
upaRearFeaturePresent: boolean
```

Indicate whether Ultrasonic Park Assist Rear feature is part of vehicle configuration.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|final    |`Yes`        |
|default  |`false`      |

<a id="title_Actions"></a>

## Actions

<a id="action_enableCollisionAlert"></a>

### enableCollisionAlert

```ts
enableCollisionAlert(enable: boolean);
```

Enable Collision Alert.

#### Parameter

- `enable`: if true enable alert, disable it else.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="action_enableRearAutoEmergencyBreak"></a>

### enableRearAutoEmergencyBreak

```ts
enableRearAutoEmergencyBreak(enable: boolean);
```

Enable Rear Auto Emergency Break.

#### Parameter

- `enable`: if true enable RAEB, disable it else.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="action_setSonarGroup"></a>

### setSonarGroup

```ts
setSonarGroup(
  sonarGroupId: integer,
  enable: boolean
);
```

Set sonar group.

#### Parameters

- `sonarGroupId`: identifier of the targeted sonar group

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |

- `enable`: if true enable the group, disable it else.

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |

<a id="title_Types"></a>

## Types

<a id="type_DisplayType"></a>

### DisplayType

```ts
type DisplayType = "none" | "widget" | "fullscreen"
```

Display type

|attribute|specification                       |
|---------|------------------------------------|
|type     |`string`                            |
|default  |`"none"`                            |
|enum     |`"none"`, `"widget"`, `"fullscreen"`|

<a id="type_FkpDisplayRequestType"></a>

### FkpDisplayRequestType

```ts
type FkpDisplayRequestType = "no_display" | "flank"
```

FKP display request type

|attribute|specification            |
|---------|-------------------------|
|type     |`string`                 |
|default  |`"no_display"`           |
|enum     |`"no_display"`, `"flank"`|

<a id="type_GroupState"></a>

### GroupState

```ts
type GroupState = "disabled" | "enabled"
```

Sensor group state

|attribute|specification            |
|---------|-------------------------|
|type     |`string`                 |
|default  |`"disabled"`             |
|enum     |`"disabled"`, `"enabled"`|

<a id="type_SensorState"></a>

### SensorState

```ts
type SensorState = {
  isHwSupported: boolean;
  isHatched: boolean;
  isScanned: boolean;
  level: integer
}
```

Sonar sensor state

|attribute |specification                                                                                                                                                         |
|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|type      |`object`                                                                                                                                                              |
|default   |`{"isHwSupported":false,"isHatched":false,"isScanned":false,"level":0}`                                                                                               |
|properties|[isHwSupported](#prop_SensorState_isHwSupported), [isHatched](#prop_SensorState_isHatched), [isScanned](#prop_SensorState_isScanned), [level](#prop_SensorState_level)|

<a id="prop_SensorState_isHwSupported"></a>

#### isHwSupported

```ts
isHwSupported: boolean
```

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|default  |`false`      |

<a id="prop_SensorState_isHatched"></a>

#### isHatched

```ts
isHatched: boolean
```

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|default  |`false`      |

<a id="prop_SensorState_isScanned"></a>

#### isScanned

```ts
isScanned: boolean
```

|attribute|specification|
|---------|-------------|
|type     |`boolean`    |
|required |`Yes`        |
|default  |`false`      |

<a id="prop_SensorState_level"></a>

#### level

```ts
level: integer
```

|attribute|specification|
|---------|-------------|
|type     |`integer`    |
|required |`Yes`        |
|default  |`0`          |

<a id="type_UpaDisplayRequestType"></a>

### UpaDisplayRequestType

```ts
type UpaDisplayRequestType = "no_display" | "rear" | "front" | "rear_front"
```

UPA display request type

|attribute|specification                                      |
|---------|---------------------------------------------------|
|type     |`string`                                           |
|default  |`"no_display"`                                     |
|enum     |`"no_display"`, `"rear"`, `"front"`, `"rear_front"`|

