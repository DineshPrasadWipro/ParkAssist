# High-Level Design

## Reference docs

-   [AWOS](https://partners.gitlab-pages.dt.renault.com/loire/hmi/architecture/alliance-apis/alliance-car/reference/alliance/car/windowoverlay/AllianceCarWindowOverlay.html)
-   [Screen Priority Matrix](https://grouperenault.sharepoint.com/:x:/r/teams/RNSLTeamProjectHMIDomain/Shared%20Documents/Park%20Assist/F-M01-06_A-IVI2_Renault_PriorityScreen_v3_00.xlsx?d=w6eec46e3836646e4b6b968cdc5f40341&csf=1&e=wkPube)

## Activities & Intents

### Public Activities

Public activities export their Intents for access by the outside world.

| Activity                        | Intent category            | Intent action       | Activity launch mode flags | Car Control Button |
| ------------------------------- | -------------------------- | ------------------- | -------------------------- | ------------------ |
| SurroundActivity (Shadow)       | `ALLIANCE_CAR_APPLICATION` | `SURROUND_VIEW`     | `FLAG_ACTIVITY_NEW_TASK`   | Camera             |
| EasyParkAssistActivity (Shadow) | `ALLIANCE_CAR_APPLICATION` | `PARK_ASSIST`       | `FLAG_ACTIVITY_NEW_TASK`   | Easypark Assist    |
| SettingsActivity                | `ALLIANCE_CAR_APPLICATION` | `SURROUND_SETTINGS` | `FLAG_ACTIVITY_NEW_TASK`   | Parking Aids       |

Notes:

-   Intent categories are all prefixed by `alliance.intent.category.`
-   Intent actions are all prefixed by `alliance.intent.action.`

[Activity launch mode flags](https://developer.android.com/guide/components/activities/tasks-and-back-stack):

-   FLAG_ACTIVITY_NEW_TASK
-   FLAG_ACTIVITY_CLEAR_TOP
-   FLAG_ACTIVITY_SINGLE_TOP

### Private Activities

Private activities do not export their intents and are visible uniquely from the application. These
activities are displayed in overlay layers.

| Activity           | AWOS Layer               | AWOS prio | Layer config name |
| ------------------ | ------------------------ | --------- | ----------------- |
| PopupActivity      | `LAYER_SYSTEM_OVERLAY`   | 7         |                   |
| FullscreenActivity | `LAYER_CRITICAL_OVERLAY` | 7         |                   |

## Static diagram

### Overview Diagram

This is an high-level picture that is not accurate. Look at the detailed design below for more
accuraccy.

```plantuml
title Overview

package viewmodel <<Rectangle>> {
  class SettingsViewModel
  class AvmViewModel
  class RvcViewModel
  class SonarViewModel
  class ConfigViewModel
}

package "repository" <<Rectangle>> {
  class SurroundViewRepository
  class SonarRepository
  class AudioRepository
  class AutoParkRepository
}

package "aasp service manager" <<Rectangle>> {
  class SurroundViewManager
  class SonarManager
  class AudioManager
  class AutoParkManager
  class PlatformDisplayBridge
}

package service <<Rectangle>> {
  class DisplayService
  class DisplayRouter
  class OverlayLauncher
  class DisplayManager

  DisplayService *-u- DisplayRouter
  DisplayService *-r- OverlayLauncher
  DisplayService *-u- DisplayManager
  DisplayRouter -u- DisplayManager
}

package activity <<Rectangle>> {
  class SettingsActivity

  package overlay <<Rectangle>> {
    abstract OverlayActivityBase
    class PopupActivity <<Overlay>>
    class FullscreenActivity <<Overlay>>
    class Fragment

    OverlayActivityBase <|-- PopupActivity
    OverlayActivityBase <|-- FullscreenActivity
    PopupActivity *-- Fragment
    FullscreenActivity *-- Fragment
  }

  package shadow <<Rectangle>> {
    abstract ShadowActivityBase
    class SurroundActivity <<Shadow>>
    class ParkAssistActivity <<Shadow>>

    ShadowActivityBase <|-- SurroundActivity
    ShadowActivityBase <|-- ParkAssistActivity
  }
}

SurroundViewRepository *-- SurroundViewManager
SonarRepository *-- SonarManager
AudioRepository *-- AudioManager
AutoParkRepository *-- AutoParkManager

AvmViewModel *-- SurroundViewRepository
RvcViewModel *-- SurroundViewRepository
SonarViewModel *-- SonarRepository

DisplayService *-d- PlatformDisplayBridge
PlatformDisplayBridge *-d- AutoParkManager
PlatformDisplayBridge *-d- SonarManager
PlatformDisplayBridge *-d- SurroundViewManager

OverlayLauncher <--> FullscreenActivity
OverlayLauncher <--> PopupActivity
OverlayActivityBase *-u- DisplayManager
ShadowActivityBase *-u- DisplayManager

Fragment *-- AvmViewModel
Fragment *-- RvcViewModel
Fragment *-- SonarViewModel
SettingsActivity *-- SettingsViewModel
```

### Display Policy Diagram

```plantuml
title Display Policy

frame "AllianceCar" {
  [Service]
}
frame "SystemUI" {
  [AWOS]
}
frame "Android" {
  [Lifecycle]
}
frame "Apllication" #LightGrey {
  frame "OverlayActivities" #White {
    [OverlayActivityX]
    [OverlayActivityY]
  }
  frame "ShadowActivities" #White {
    [ShadowActivityX]
    [ShadowActivityY]
  }
  frame "DisplayService" #White {
    [DisplayRouter]
    [OverlayLauncher]
  }
}

Lifecycle -up-> ShadowActivities : manages
ShadowActivities -up-> DisplayRouter : feeds
DisplayRouter --> ShadowActivities : (pause / resume)?
Service --> DisplayRouter : feeds
DisplayRouter -left-> OverlayLauncher : requests activity
OverlayLauncher -up-> AWOS : creates overlay
AWOS --> OverlayActivities
```

### Multi-User Diagram

```plantuml
title Multi-User

package "Android" {
  class UserManager << (S,#FF7700) Singleton >>
  class LifecycleManager << (S,#FF7700) Singleton >>
}
package "Apllication" {
  class DisplayService << (S,#FF7700) Singleton >>
  class OverlayActivity
  class ShadowActivity
  DisplayService *- OverlayActivity

  ShadowActivity --> DisplayService : feeds

  note left of ShadowActivity : "Run in Regular User context"

  note left of DisplayService : "Persistent to user switch\nRun in Headless System User context"
}

Lifecycle --> ShadowActivity : manages
```

## Roles & responsibilities

### DisplayService

This service handles all navigation requests.

-   It is launched on `BOOT_COMPLETED` intent reception and runs in foreground till device shutdown
-   It registers to Alliance services that could request a parking app display.
-   It handles private activity launches, display and release via Display Policy & the Activity
    Launcher.

#### PlatformDisplayBridge

Aggregates display & navigation of all relevant parking aids services as a single interface to
DisplayRouter.

#### DisplayRouter

Collects all display requests. Accordingly to DisplayPolicy feedback, it transforms requests into
adequate actions :

-   Requests from AASP services are tranformed into activities lifecycle actions and fragment
    navigation action.
-   Requests from activities are forwarded to AASP services.

Request sources:

-   Services' managers
-   Shadow activities
-   Overlay Activities

#### DisplayPolicy

Retain current display state and available actors (shadow & overlay activity) and arbitrate
concurrent ParkAssist use-cases priorities.

Note: AWOS handle priorities between ParkAssist & the rest of the world.

#### OverlayLauncher

Launches & stops activities according to Display Policy output.

### Activities

There are 4 different activities for ParkAssist :

-   SurroundPopup : handles RVC/AVM Sonar feedback visualization activity in a popup view
-   Surround : handles camera streams visualization activity including Sonar visual feedback
    (fullscreen)
-   ParkAssist : handles parking assistance activity (relying on camera & sonar)
-   Settings : handles surround settings activity

**Settings** activity is a regular Android `SettingsActivity`.

**SurroundPopup** activity can only be triggered upon vehicle request, thus is has no associated
shadow activity, but only an overlay `PopupActivity` (as it must be displayed over regular
activities).

**Surround** activity can be launched by a user using the shadow `SurroundActivity`, then it is
displayed in the overlay `FullscreenActivity`.

**ParkAssist** activity can be launched by a user using the shadow `EasyParkAssistActivity`, then it
is displayed in the overlay `FullscreenActivity`.

#### OverlayActivity

These activities implement HMI ParkAssist features. They rely on Alliance Car services. They are
managed ONLY through AWOS service (meaning they are never used as regular Android activities).

#### ShadowActivity

Each Overlay Activity is associated to a Shadow Activity. A Shadow Activity is the representative
for an Overlay Activity to the Android Framework. It is responsible to handle regular Android
lifecycle events for its associated Overlay Activity and forward it appropriately to the
DisplayRouter.

Shadow Activities are user's entry-points to Overlay Activities: they handle public Park Assist
activity intents.

They are needed to handle non-regulatory use-cases and are part of the regulatory to/from
non-regulatory switch mechanism.

#### Repository

Repositories abstract service implementation and provide LiveData interface for view-models. A
repository is a **singleton** instantiated by injection.

#### ViewModel

A view-model aggregates data from different repositories for the need of a specific view (or
assimilated).

ViewModels are injected in the views (or assimilated) by configuration.

For testing purpose, when related service APIs are not yet available, let's mock the view-model to
stimulate the component under test.

For demo or simulation (when repository is not yet ready) purpose, let's inject a simulation
view-model implementation or a remote-control view-model.

## Principle Sequence diagrams

### Nominal Regulatory UC

```plantuml
title Nominal Regulatory UC

participant System
participant PlatformDisplayBridge
participant DisplayRouter
participant OverlayLauncher
participant OverlayActivity

== Activation ==

PlatformDisplayBridge -> DisplayRouter : routeChange(routeId, closable = false)
DisplayRouter -> DisplayRouter : evaluatePolicy
DisplayRouter -> OverlayLauncher : createOverlay
OverlayLauncher --> OverlayActivity ** : onCreate
OverlayActivity -> DisplayRouter : register(activityId)
DisplayRouter -> OverlayActivity : routeChange(routeId)

== De-activation ==

PlatformDisplayBridge -> DisplayRouter : routeChange(routeId = NONE, closable = false)
DisplayRouter -> DisplayRouter : evaluatePolicy
group Optional
  DisplayRouter -> OverlayActivity : routeChange(routeId = NONE)
end
DisplayRouter -> OverlayLauncher : destroy
OverlayLauncher --> OverlayActivity !! : onDestroy
```

### Nominal Non-Regulatory UC

```plantuml
title Nominal Non-Regulatory UC

participant System
participant PlatformDisplayBridge
participant DisplayRouter
participant OverlayLauncher
participant ShadowActivity
participant OverlayActivity

== Activation ==

System --> ShadowActivity ** : onCreate (Intent)
System --> ShadowActivity : onResume
group #aliceblue Optimistic UI (not POR)
  ShadowActivity -> ShadowActivity : displayEarlyContent
end
ShadowActivity -> DisplayRouter : register(activityId)
ShadowActivity -> DisplayRouter : startPursuit(pursuit)
DisplayRouter -> DisplayRouter : evaluatePolicy
group #aliceblue Optimistic UI & request denied (not POR)
  DisplayRouter -> ShadowActivity : displayWarningAndExit (thru Listener)
end
DisplayRouter -> PlatformDisplayBridge : startPursuit(pursuit)
PlatformDisplayBridge -> DisplayRouter : routeChange(routeId, closable = true)
DisplayRouter -> DisplayRouter : evaluatePolicy
DisplayRouter -> OverlayLauncher : createOverlay
OverlayLauncher --> OverlayActivity ** : onCreate (Intent)
OverlayActivity -> DisplayRouter : register(activityId)
DisplayRouter -> OverlayActivity : routeChange(routeId)

== De-activation by User ==

System --> ShadowActivity : onPause
ShadowActivity -> DisplayRouter : closePursuit
ShadowActivity -> DisplayRouter : unregister(activityId)
DisplayRouter -> DisplayRouter : evaluatePolicy
DisplayRouter -> PlatformDisplayBridge : closePursuit
group Optional
  PlatformDisplayBridge -> DisplayRouter : routeChange(routeId = NONE, closable = true)
  DisplayRouter -> DisplayRouter : evaluatePolicy
  DisplayRouter -> OverlayActivity : routeChange(routeId = NONE)
end
DisplayRouter -> OverlayLauncher : destroy
OverlayLauncher --> OverlayActivity !! : onDestroy

== De-activation by Vehicle ==

PlatformDisplayBridge -> DisplayRouter : routeChange(routeId = NONE, closable = false)
DisplayRouter -> DisplayRouter : evaluatePolicy
DisplayRouter --> ShadowActivity : onClose
ShadowActivity -> DisplayRouter : unregister(activityId)
group Optional
  DisplayRouter -> OverlayActivity : routeChange(routeId = NONE)
end
DisplayRouter -> OverlayLauncher : destroy
OverlayLauncher --> OverlayActivity !! : onDestroy
```

### Navigation

DisplayRouter acts as a navigation proxy, every navigation state and event shall go thru it and be
evualed prior to being forwarded.

```plantuml
title View change

participant System
participant PlatformDisplayBridge
participant DisplayRouter
participant OverlayLauncher
participant ShadowActivity
participant OverlayActivity

== User request ==

OverlayActivity -> DisplayRouter : navigateTo(view)

DisplayRouter -> DisplayRouter : evaluatePolicy
DisplayRouter -> PlatformDisplayBridge : navigateTo(view)

PlatformDisplayBridge -> DisplayRouter : routeChange(routeId, closable = true)
DisplayRouter -> DisplayRouter : evaluatePolicy
DisplayRouter -> OverlayActivity : routeChange(routeId)

== Vehicle request ==

PlatformDisplayBridge -> DisplayRouter : routeChange(routeId, closable = false)
DisplayRouter -> DisplayRouter : evaluatePolicy
DisplayRouter -> OverlayActivity : routeChange(routeId)

```

### Non-Regulatory to Regulatory switch UC

```plantuml
title Non-Regulatory to Regulatory switch UC

participant System
participant PlatformDisplayBridge
participant DisplayRouter
participant OverlayLauncher
participant ShadowActivity
participant OverlayActivity

PlatformDisplayBridge -> DisplayRouter : routeChange(routeId, closable = false)
DisplayRouter -> DisplayRouter : evaluatePolicy
DisplayRouter -> OverlayLauncher : update(modeless = false)
DisplayRouter -> OverlayActivity : routeChange(routeId)
```

### Regulatory to Non-Regulatory switch UC

Only 1 UC identified :

-   Sonar AVM popup is displayed
-   User clicks on 'camera' button to open AVM fullscreen view
-   Popup shall close & AVM shall display

```plantuml
title Regulatory to Non-Regulatory switch UC

participant System
participant PlatformDisplayBridge
participant DisplayRouter
participant OverlayLauncher
participant ShadowActivity
participant OverlayActivity

OverlayActivity -> DisplayRouter : switchPursuit(pursuit)

== Close current activity ==

DisplayRouter -> DisplayRouter : evaluatePolicy
group Optional
  DisplayRouter -> OverlayActivity : routeChange(routeId = NONE)
end
DisplayRouter -> OverlayLauncher : destroy
OverlayLauncher --> OverlayActivity !! : onDestroy

== Open new activity ==

DisplayRouter --> ShadowActivity ** : onCreate (Intent)
System --> ShadowActivity : onResume

ShadowActivity -> DisplayRouter : register(activityId)
ShadowActivity -> DisplayRouter : startPursuit(pursuit)
DisplayRouter -> DisplayRouter : evaluatePolicy
DisplayRouter -> PlatformDisplayBridge : startPursuit(pursuit)
PlatformDisplayBridge -> DisplayRouter : routeChange(routeId, closable = true)
DisplayRouter -> DisplayRouter : evaluatePolicy
DisplayRouter -> OverlayLauncher : createOverlay
OverlayLauncher --> OverlayActivity : onCreate (Intent)
OverlayActivity -> DisplayRouter : register(activityId)
DisplayRouter -> OverlayActivity : routeChange(routeId)
```

### Navigation States & Actions

#### Services Navigation API

Interfaces exposed by the AASP Services to the `PlatformDisplayBridge` (thru repositories).

##### Services Navigation States

| Service  | Service Callback     | Repository State       |
| -------- | -------------------- | ---------------------- |
| Sonar    | onDisplayRequest     | displayRequest         |
| Surround | onViewStateChange    | surroundState          |
|          | onViewChangeRequest  | requestedSurroundState |
| APA      | onDisplayStateChange | displayState           |

##### Services Navigation Actions

| Service  | Action                           | Comment                         |
| -------- | -------------------------------- | ------------------------------- |
| Surround | request(CLOSE_VIEW)              | Close                           |
|          | request(ACTIVATE_MANEUVER_VIEW)  | Open                            |
|          | request(ACTIVATE_TRAILER_VIEW)   | Open                            |
|          | request(ACTIVATE_SETTINGS_VIEW)  | Navigation                      |
|          | request(BACK_FROM_SETTINGS_VIEW) | Navigation                      |
|          | request(SELECT\_\*)              | Navigation                      |
| APA      | switchActivation                 | Open / Close                    |
|          | goToSettings                     | /!\ Missing Navigation ??? /!\  |
|          | backFromSettings                 | /!\ Missing Navigation ??? /!\  |

#### PlatformDisplayBridge Navigation API

Interfaces exposed by the `PlatformDisplayBridge` to the `DisplayRouter`. As part of its orther
roles, `PlatformDisplayBridge` translates its API to Services Navigation API.

##### PlatformDisplayBridge States

```ts
onRouteChange(RouteIdentifier routeId)
```

`RouteIdentifier` values :

-   SURROUND_MAIN
-   SURROUND_SETTINGS
-   PARK_SCANNING
-   ...

##### PlatformDisplayBridge Actions

> _Pursuit_ : an activity of a specified kind, especially a recreational or sporting one.

```ts
startPursuit(Pursuit pursuit)
closePursuit(Pursuit pursuit)
switchPursuit(Pursuit pursuit)
navigateTo(DestinationView view)
```

`Pursuit` values :

-   MANEUVER
-   WATCH_TRAILER
-   PARK

`DestinationView` values :

-   SETTINGS
-   ...

#### DisplayManager Navigation API

Interfaces exposed by the `DisplayManager` to the Overlay Activities.

This API is strictly identical to `PlatformDisplayBridge` Navigation API.

## DisplayRouter Detail Design

### Interaction between DisplayRouter & Shadows

```plantuml
title DisplayRouter & Shadows interactions

package service <<Rectangle>> {
  class DisplayService
  class DisplayRouter
  class DisplayManager {
    void navigateTo()
    void registerNavigationListener(ActivityId id, IRouteListener listener)
    void unregisterNavigationListener(ActivityId id)
  }

  DisplayService *-u- DisplayRouter
  DisplayService *-u- DisplayManager
  DisplayRouter -u- DisplayManager
}

package shadow <<Rectangle>> {
  abstract ShadowActivityBase {
    int id
    DisplayManager manager
    DisplayManager getPolicyManager()
  }
  class SurroundActivity <<Shadow>> {
    int id = ACTIVITY_SURROUND
  }
  class ParkAssistActivity <<Shadow>> {
    int id = ACTIVITY_PARK_ASSIST
  }
  interface IRouteListener {
    onRouteChange()
  }

  IRouteListener <|-- ShadowActivityBase
  ShadowActivityBase <|-- SurroundActivity
  ShadowActivityBase <|-- ParkAssistActivity

  ShadowActivityBase *-- DisplayManager
  DisplayManager *-- IRouteListener
}

```

### Interaction between DisplayRouter & Overlays

```plantuml
title DisplayRouter & Shadows interactions

package service <<Rectangle>> {
  class DisplayService
  class DisplayRouter
  class OverlayLauncher {
    void create(ActivityId id, Boolean modeless)
    void destroy()
    void registerListener()
    void unregisterListener()
  }
  class DisplayManager {
    void navigateTo()
    void registerNavigationListener(ActivityId id, IRouteListener listener)
    void unregisterNavigationListener(ActivityId id)
  }
  class AllianceCarWindowOverlay #lightblue {
    void add()
    void update()
    void remove()
    void dispose()
    void registerListener()
    void unregisterListener()
  }
  interface IAllianceCarOverlayEventListener  #lightblue {
    onAdd()
    onUpdate()
    onRemove()
    onError()
  }
  interface IRouteListener {
    onRouteChange()
  }

  DisplayService *-- DisplayRouter
  DisplayRouter *-- OverlayLauncher
  OverlayLauncher *-- AllianceCarWindowOverlay
  DisplayRouter -l-|> IAllianceCarOverlayEventListener
  DisplayService *-- DisplayManager
  DisplayRouter -- DisplayManager
}

package overlay <<Rectangle>> {
  abstract OverlayActivityBase {
    int id
    DisplayManager manager
    DisplayManager getPolicyManager()
  }
  class PopupActivity <<Overlay>>
  class FullscreenActivity <<Overlay>>
  class Fragment

  OverlayActivityBase <|-- PopupActivity
  OverlayActivityBase <|-- FullscreenActivity
  PopupActivity *-- Fragment
  FullscreenActivity *-- Fragment

  OverlayActivityBase *-- DisplayManager
}

class AllianceCarWindowOverlayManager #lightblue {
  AllianceCarWindowOverlay createOverlay()
}

AllianceCarWindowOverlayManager *-r- IAllianceCarOverlayEventListener
OverlayLauncher *-l- AllianceCarWindowOverlayManager

AllianceCarWindowOverlay -- OverlayActivityBase
IRouteListener <|-- OverlayActivityBase
DisplayManager *-- IRouteListener
```

### Interaction between DisplayRouter & AASP Services

```plantuml
title DisplayRouter & Shadows interactions

package service <<Rectangle>> {
  class DisplayService
  class DisplayRouter
  class DisplayManager

  DisplayService *-u- DisplayRouter
  DisplayService *-u- DisplayManager
  DisplayRouter -u- DisplayManager
}

package "aasp services repository" <<Rectangle>> {
  class PlatformDisplayBridge {
    onRouteChange(RoutingRequest request)
    void startPursuit(Pursuit pursuit)
    void closePursuit(Pursuit pursuit)
    void switchPursuit(Pursuit pursuit)
    void navigateTo(DestinationView view)
  }
  class SurroundViewManager
  class SonarManager
  class AutoParkManager

  PlatformDisplayBridge *-- SurroundViewManager
  PlatformDisplayBridge *-- SonarManager
  PlatformDisplayBridge *-- AutoParkManager
}

DisplayRouter *-- PlatformDisplayBridge
```

### DisplayRouter Interactions Summary

```plantuml
interface IAllianceCarOverlayEventListener  #lightblue {
  onAdd()
  onUpdate()
  onRemove()
  onError()
}
note top: API exposed by DisplayRouter\nto observe overlay lifecycle

class OverlayLauncher {
  void create(ActivityId id, Boolean modeless)
  void destroy()
  void registerListener()
  void unregisterListener()
}
note top: API exposed to DisplayRouter\nto control overlays lifecycle

class DisplayManager {
  void navigateTo()
  void registerNavigationListener(ActivityId id, IRouteListener listener)
  void unregisterNavigationListener(ActivityId id)
}
interface IRouteListener {
  onRouteChange()
}
note top: API exposed by Overlay/Shadow Activities\nto DisplayManager

class DisplayRouter
class DisplayPolicy

class PlatformDisplayBridge {
  onNavigationRequest(RoutingRequest request)
  void startPursuit(Pursuit pursuit)
  void closePursuit(Pursuit pursuit)
  void switchPursuit(Pursuit pursuit)
  void navigateTo(DestinationView view)
}

DisplayRouter *-l- DisplayPolicy

DisplayRouter -u- DisplayManager
DisplayRouter -- PlatformDisplayBridge
DisplayRouter -u- OverlayLauncher
DisplayRouter -u-|> IAllianceCarOverlayEventListener

DisplayManager *-u- IRouteListener
```

## Camera Detail Design

To handle RVC and AVM in a common way, we use one main fragment:`CameraFragment`

#### CameraFragment:

Hence CameraFragment owns two main components:
- an `EvsCameraView` displaying the camera stream
- a `CameraOverlayView` applying multiple xml layouts

##### EvsCameraView:

This camera component owns the surface provided to SurroundView Service to display the camera stream.

##### CameraOverlayView:

This camera component fits the camera stream size and overlays `EvsCameraView` to manage all required layouts.

 These dedicated layouts (layout_ovl_xxx.xml) are commonly applied to AVM/RVC use cases when it exists, and splitted as follow:
  - standard view [ Rear/Front + Bird ]
  - panoramic
  - bird sides (i.e. fixed side masks)
  - 3D view
  - camera_error_overlay (default one)

This camera component manages:
  - CameraSonarAlerts (RTCA/RAEB flags)
  - CameraSonarMute (MuteButton)
  - CameraIndicator
  - Boot open status
  - EasyPark Assist icon


```plantuml
top to bottom direction
[*] --> Initial


Initial -right--> RunningAndReleasing
Initial -left--> Running
Initial -right--> Stopped
Running --> Stopped


```
### Camera Interactions Summary

```plantuml
skinparam class {
	BackgroundColor PaleGreen
	ArrowColor SeaGreen
	BorderColor SpringGreen
}
skinparam stereotypeCBackgroundColor YellowGreen

  package ui.avm {
    class AvmFragment
  }

  package ui.rvc {
    class RvcFragment
  }

  package ui.camera {
    class CameraFragment
    class CameraOverlayView
    class CameraIndicatorView
    class BootStatus::TextView
    interface IEvsCameraListener
    class EvsCameraView {
       -EvsCameraListener evsCameraListener
    }
    class TextureView
    EvsCameraView o-- TextureView
    package TouchInterceptor {
      interface TouchListener
    }
  }

  package ui.sonar {
    class SonarFragment {
      -ImageView car
      -VectorMasterView vectors
      -SonarAlertsView sonarAlertsView
      -SonarMuteView sonarMuteView
    }
  }

  package ui.alerts {
    class SonarAlertsView
    class SonarMuteView
  }

CameraFragment *-- EvsCameraView
TouchListener <|-- EvsCameraView
CameraFragment *-- CameraOverlayView
CameraOverlayView *-- CameraIndicatorView
CameraOverlayView *-- BootStatus::TextView
EvsCameraView  *-- IEvsCameraListener

CameraOverlayView *-- SonarMuteView
CameraOverlayView *-- SonarAlertsView

AvmFragment *-- CameraFragment
RvcFragment *-- CameraFragment

SonarFragment *-- SonarMuteView
SonarFragment *-- SonarAlertsView
```

## Annexes

### Screen priority

#### Screen Priority Matrix Extract

Here is the extract of priority matrix regarding Park Assist screens:

| Prio | Screen             | Type   |
| ---- | ------------------ | ------ |
| 5    | ADAS mandatory     | Screen |
| ...  |                    |        |
| 12   | UPA popup          | Left   |
| ...  |                    |        |
| 17   | ADAS non-mandatory | Screen |

#### Screen Priority Guess

| OutlinR screen              | Cam | Sonar | User           | Regulatory | Activity      | Fragment               |
| --------------------------- | --- | ----- | -------------- | ---------- | ------------- | ---------------------- |
| upa_only                    |     | Y     |                | Y          | Surround      | RvcFragment            |
| rvc_standardview            | Y   | Y     |                | Y          | Surround      | RvcFragment            |
| rvc_standardviewnoupa       | Y   |       |                | Y          | Surround      | RvcFragment            |
| rvc_panoramicview           | Y   | Y     |                | Y          | Surround      | RvcFragment            |
| rvc_settings                | Y   | Y     | Y [DEPRECATED] | Y          | Surround      | CameraSettingsFragment |
| avm_3d                      |     | Y     | Y              |            | Surround      | AvmFragment            |
| avm_birdsides               |     | Y     | Y              |            | Surround      | AvmFragment            |
| avm_panoramic               | Y   | Y     | Y              | Y          | Surround      | AvmFragment            |
| avm_fullbird [DEPRECATED]   |     | Y     | Y              |            | Surround      | AvmFragment            |
| avm_birdrearfront           | Y   | Y     | Y              | Y          | Surround      | AvmFragment            |
| avm_settings                | Y   | Y     | Y              |            | Surround      | AvmFragment            |
| rvc_dealerview              | Y   |       |                | Y          | Surround      |                        |
| avm_dealerview              | Y   |       |                | Y          | Surround      |                        |
| trailerview_main            | Y   |       | Y              | Y          | Surround      | AvmFragment            |
| avm_popup                   |     | Y     |                | Y          | SurroundPopup | AvmPopUpFragment       |
| upa_popup                   |     | Y     |                | Y          | SurroundPopup | RvcPopUpFragment       |
| settings_parkingassist-raeb |     |       | Y              |            | Settings      | mainSettingsFragment   |
| settings_parkingassistsound |     |       | Y              |            | Settings      | SoundSettingsFragment  |
| apa_sonarwithrvcguidance    | Y   | Y     | Y              | Y          | Surround      | HfpGuidanceFragment    |
| apa_sonarwithrvcguidance1   |     | Y     | Y              | ???        | Surround      | HfpGuidanceFragment    |
| apa_sonarwithrvcguidance2   | Y   | Y     | Y              | Y          | Surround      | HfpGuidanceFragment    |
| apa_mainscanningwithrvc     |     | Y     | Y              | ???        | Surround      | HfpScanningFragment    |
| apa_sonarwithavmguidance    | Y   | Y     | Y              | Y          | Surround      | HfpGuidanceFragment    |
| apa_sonarwithavmguidance1   |     | Y     | Y              | ?          | Surround      | HfpGuidanceFragment    |
| apa_sonarwithavmguidance2   | Y   | Y     | Y              | Y          | Surround      | HfpGuidanceFragment    |
| apa_mainscanningwithavm     |     | Y     | Y              | ???        | Surround      | FapkFragment           |
| fapk_scanning               | Y   | Y     | Y              | Y          | Surround      | FapkFragment           |
| fapk_guidance               | Y   | Y     | Y              | Y          | Surround      | FapkFragment           |
| fapk_settings_parallel      |     |       | Y              |            | Surround      | HfpSettingsFragment    |
| fapk_settings_perpendicular |     |       | Y              |            | Surround      | HfpSettingsFragment    |

### UI Hierarchy

#### Settings Activity

```plantuml
SettingsActivity *-down- SoundSettingsFragment
SettingsActivity *-down- mainSettingsFragment
SoundSettingsFragment *-down- SonarPreferencesFragment
mainSettingsFragment *-down- SonarPreferencesFragment
```

#### Surround Activity

```plantuml
SurroundActivity *-down- AvmFragment
SurroundActivity *-down- RvcFragment
SurroundActivity *-down- CameraSettingsFragment
```

#### APA Activity

```plantuml
ApaActivity *-down- HfpScanningFragment
ApaActivity *-down- HfpGuidanceFragment
ApaActivity *-down- HfpSettingsFragment
ApaActivity *-down- FapkFragment
```

#### Pop-Up Activity

```plantuml
PopupActivity *-down- AvmPopUpFragment
PopupActivity *-down- RvcPopUpFragment
```

### Logging
Specifications:
https://partners.gitlab-pages.dt.renault.com/loire/hmi/architecture/architecture-board/policies/analytics-and-logging.html#log-util
https://gitlabee.dt.renault.com/partners/loire/hmi/architecture/aaspac-hmi/-/blob/master/aafw-logging-requirements.md

Log debug use cases:
- dev's choice
- log class name

Log info use case:
- Every output (ex: surround request) at the repository level for u10 and bridge level for u0
- Every input (ex: surround viewState) //
- Boot info (ex: services onCreate, config read) //
- analytics logs

Log warning use case:
- unknown input mapping value (ex: Apa mapper unknown DisplayType)
- unexpected lifecycle event (ex: shadow activity goes on pause while in regulatory)
- unexpected user action
- unexpected state but non blocking

Log error use case:
- internal error due to non existing mapping (ex: missing CameraOverlay)
- routing error or unexpected route
- error within viewmodel
- unexpected state, blocking or fallback

Log wtf use case:
- cannot read config
- cannot register listeners
- cannot connect to manager
- cannot connect to display service
- display service disconnection
- managers disconnection

Log debug format:
dev's choice, same as before, keep old ones

Log info format:
[context] - [action] [property?] [value?]

Log warning format:
[context] - [fact]

Log error format:
[context] - [action] [property?] [value?] [fallback?]

Log wtf format:
[context] - [fact]

? => optional
context = autopark|surround|sonar|internal|boot|camera
action = get|send|receive|load
fact = sentence to be compliant with spec
fallback = sentence to be compliant with spec







```plantuml
title Camera transitions overview

participant User
participant SurroundView
participant CameraStateMachine
participant CameraStatus
participant CameraFragment

== Generic case ==

SurroundView -> CameraStateMachine: viewRequest(<VIEW>)
CameraStateMachine -> CameraStatus: showTransition() optional
CameraStateMachine -> SurroundView: setStatus(NO_DISPLAY)

SurroundView -> CameraStateMachine: viewState(<VIEW>)
CameraStateMachine -> CameraStatus: stopTransition() optional
CameraStateMachine -> SurroundView: setStatus(<VIEW>)

== Switch AVM view ==

User -> SurroundView : REQUEST_STANDARD
SurroundView -> CameraStateMachine: viewRequest(AVM_STANDARD)
CameraStateMachine -> CameraStatus: showTransition()
CameraStateMachine -> SurroundView: setStatus(NO_DISPLAY)

SurroundView -> CameraStateMachine: viewState(AVM_STANDARD)
CameraStateMachine -> CameraStatus: stopTransition()
CameraStateMachine -> SurroundView: setStatus(AVM_STANDARD)

== AVM Standard view (Regulatory) ==

SurroundView -> CameraStateMachine: viewRequest(AVM_STANDARD)
CameraStateMachine -> CameraStatus: showTransition()
CameraStateMachine -> SurroundView: setStatus(NO_DISPLAY)

SurroundView -> CameraStateMachine: viewState(AVM_STANDARD)
CameraStateMachine -> CameraStatus: stopTransition()
CameraStateMachine -> SurroundView: setStatus(AVM_STANDARD)

== Switch AVM Autozoom ==

User -> SurroundView : REQUEST_AUTOZOOM
SurroundView -> CameraStateMachine: viewRequest(AUTOZOOM)
CameraStateMachine -> SurroundView: setStatus(NO_DISPLAY)

SurroundView -> CameraStateMachine: viewState(AUTOZOOM)
CameraStateMachine -> SurroundView: setStatus(AUTOZOOM)

== Entering AVM ==

User -> SurroundView : 
SurroundView -> CameraStateMachine: viewRequest(AVM_STANDARD)
CameraStateMachine -> CameraStatus: showTransition()
CameraStateMachine -> SurroundView: setStatus(NO_DISPLAY)

SurroundView -> CameraStateMachine: viewState(AVM_STANDARD)
CameraStateMachine -> CameraStatus: stopTransition()
CameraStateMachine -> SurroundView: setStatus(AVM_STANDARD)

== Entering RVC Settings ==

User -> SurroundView : ACTIVATE_SETTINGS_VIEW
SurroundView -> CameraStateMachine: viewRequest(SETTINGS)
CameraStateMachine -> CameraStatus: showTransition()
CameraStateMachine -> SurroundView: setStatus(NO_DISPLAY)

SurroundView -> CameraStateMachine: viewState(SETTINGS)
CameraStateMachine -> CameraStatus: stopTransition()
CameraStateMachine -> SurroundView: setStatus(SETTINGS)

== Exiting RVC Settings ==

User -> SurroundView : BACK_FROM_SETTINGS
SurroundView -> CameraStateMachine: viewRequest(RVC_STANDARD)
CameraStateMachine -> CameraStatus: showTransition()
CameraStateMachine -> SurroundView: setStatus(NO_DISPLAY)

SurroundView -> CameraStateMachine: viewState(RVC_STANDARD)
CameraStateMachine -> CameraStatus: stopTransition()
CameraStateMachine -> SurroundView: setStatus(RVC_STANDARD)

== Error case ==

SurroundView -> CameraStateMachine: viewRequest(RVC_STANDARD)
CameraStateMachine -> CameraStatus: showTransition()
CameraStateMachine -> SurroundView: setStatus(NO_DISPLAY)

SurroundView -> CameraStateMachine: CameraError
SurroundView -> CameraFragment: showError()
CameraStateMachine -> CameraStatus: stopTransition()

SurroundView -> CameraStateMachine: viewState(RVC_STANDARD)
CameraStateMachine -> CameraStatus: stopTransition()
CameraStateMachine -> SurroundView: setStatus(RVC_STANDARD)

== Exit from Error case ==

SurroundView -> CameraStateMachine: CameraNoError
CameraStateMachine -> CameraStatus: showTransition()
SurroundView -> CameraFragment: hideError()

```