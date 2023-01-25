# App Architecture: Documentation

## HMI Architecture Breakdown

| Screen ID                    | Orientation | Feature  | Activity Name      | Fragment Name                                  |
| ---------------------------- | ----------- | -------- | ------------------ | ---------------------------------------------- |
| upa_popup                    | both        | upa      | PopUpActivity      | SonarPipFragment                               |
| upa_only                     | both        | upa      | FullscreenActivity | SonarFragment                                  |
| rvc_standardview-gen1        | both        | rvc      | FullscreenActivity | RvcFragment                                    |
| rvc_settings-gen1            | both        | rvc      | FullscreenActivity | RvcSettingsFragment                            |
| rvc_dealerview               | both        | rvc      | FullscreenActivity | DealerFragment                                 |
| avm_birdrearfront            | both        | avm      | FullscreenActivity | AvmFragment                                    |
| avm_panoramic                | both        | avm      | FullscreenActivity | AvmFragment                                    |
| avm_birdsides                | both        | avm      | FullscreenActivity | AvmFragment                                    |
| avm_3d                       | both        | avm      | FullscreenActivity | AvmFragment                                    |
| avm_settings                 | both        | avm      | FullscreenActivity | AvmSettingsFragment                            |
| avm_popup                    | both        | avm      | PopUpActivity      | AvmPipFragment                                 |
| avm_dealerview               | both        | avm      | FullscreenActivity | DealerFragment                                 |
| trailerview_main             | both        | rvc, avm | FullscreenActivity | TrailerFragment                                |
| fapk_scanning-choose         | both        | fapk     | FullscreenActivity | FapkFragment                                   |
| fapk_guidance                | both        | fapk     | FullscreenActivity | FapkFragment                                   |
| fapk_settings_perpendicular  | both        | fapk     | FullscreenActivity | FapkSettingsFragment                           |
| fapk_settings_parallel       | both        | fapk     | FullscreenActivity | FapkSettingsFragment                           |
| apa_mainscanningwithavm      | both        | apa      | FullscreenActivity | AvmHfpScanningFragment                         |
| apa_mainscanningwithrvc      | both        | apa      | FullscreenActivity | RvcHfpScanningFragment                         |
| apa_sonarwithavmguidance     | both        | apa      | FullscreenActivity | AvmHfpGuidanceFragment                         |
| apa_sonarwithrvcguidance     | both        | apa      | FullscreenActivity | RvcHfpGuidanceFragment                         |
| apa_settings_perpendicular   | both        | apa      | FullscreenActivity | AvmHfpSettingsFragment, RvcHfpSettingsFragment |
| apa_settings_parallel        | both        | apa      | FullscreenActivity | AvmHfpSettingsFragment, RvcHfpSettingsFragment |
| settings_parkingassist-sound | both        | fapk     | SettingsActivity   | MainSettingsFragment, SoundSettingsFragment    | apa_warningdisplayrequest2   | both        | apa      | ApaWarningActivity | N/A

## Application Architecture Breakdown

### Manifest Information

For each application define
[manifest information](https://developer.android.com/guide/topics/manifest/manifest-intro):

| Application name | ParkAssist             |
| ---------------- | ---------------------- |
| Package name     | com.renault.parkassist |
| Application ID   | com.renault.parkassist |

### App Components

Itemize Application components like described
[here](https://developer.android.com/guide/components/fundamentals#Components)

Component Types: Activity, Service, Broadcast Receiver, Content Provider

| Component                  | Type               | Brief description & role                                           |
| -------------------------- | ------------------ | ------------------------------------------------------------------ |
| SurroundLauncherActivity   | Activity (Regular) | Application entry point for camera screens                         |
| EasyParkLauncherActivity   | Activity (Regular) | Application entry point for easy-park screens                      |
| SettingsActivity           | Activity (Regular) | Parking Assistance Settings                                        |
| FullscreenActivity         | Activity (AWOS)    | Displays camera & easy-park fullscreen screens                     |
| FullscreenShadowActivity   | Activity (Regular) | Handles FullscreenActivity Android lifecycle                       |
| PopUpActivity              | Activity (AWOS)    | Displays UPA / AVM popup screen                                    |
| DisplayService             | Service            | Window overlay manager & router manager foreground service         |
| TrailerNotificationService | Service            | Displays trailer notification to current user                      |
| BootBroadcastReceiver      | Receiver           | Starts display service upon system boot completed intent reception |
| ApaWarningActivity         | Activity (AWOS)    | Display Easy Park Assist related warnings / messages               |
| SurroundWarningActivity    | Activity (AWOS)    | Display Surround View (AVM / RVC) related warnings / messages      |

### Itemize Intents

Itemize intent filters info like described
[here](https://developer.android.com/guide/components/intents-filters) and
[here](https://developer.android.com/guide/topics/manifest/intent-filter-element)

| Component                                            | Action                     | Category                              | Data | Comments                |
| ---------------------------------------------------- | -------------------------- | ------------------------------------- | ---- | ----------------------- |
| com.renault.parkassist/.ui.SurroundLauncherActivity  | com.alliance.SURROUND_VIEW | com.alliance.ALLIANCE_CAR_APPLICATION | -    | Camera screens launcher |
| com.renault.parkassist/.ui.settings.SettingsActivity |                            | android.intent.category.LAUNCHER      | -    | Settings screen         |
| com.renault.parkassist/.ui.EasyParkLauncherActivity  | com.alliance.PARK_ASSIST   | com.alliance.ALLIANCE_CAR_APPLICATION | -    | Apa screens launcher    |

### Itemize Dependencies

| Dependency type | Name                                                                                                                                      | Description                                                                      |
| --------------- | ----------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------- |
| permission      | android.permission.INTERNAL_SYSTEM_WINDOW and android.permission.SYSTEM_ALERT_WINDOW                                                      | to use the correct overlay types                                                 |
| permission      | alliance.car.permission.WINDOW_OVERLAY                                                                                                    | to use window overlay service                                                    |
| permission      | android.permission.FOREGROUND_SERVICE and android.permission.RECEIVE_BOOT_COMPLETED                                                       | to allow the service to start and listen to display requests as soon as possible |
| permission      | alliance.car.permission.SONAR and alliance.car.permission.SONAR_CONTROL                                                                   | to use sonar service                                                             |
| permission      | alliance.car.permission.SURROUNDVIEW and alliance.car.permission.SURROUNDVIEW_CONTROL                                                     | to use surround view service                                                     |
| permission      | alliance.car.permission.AUTOPARK and alliance.car.permission.AUTOPARK_CONTROL                                                             | to use autopark service                                                          |
| permission      | alliance.car.permission.ALLIANCE_CAR_AUDIO_SERVICE                                                                                        | to use sound service                                                             |
| permission      | alliance.car.permission.ADAS and alliance.car.permission.CONTROL_ADAS                                                                     | to access ADAS properties                                                        |
| permission      | alliance.car.permission.RCTA and alliance.car.permission.CONTROL_RCTA                                                                     | to access sonar RCTA properties                                                  |
| permission      | android.permission.INTERACT_ACROSS_USERS and android.permission.INTERACT_ACROSS_USERS_FULL and android.permission.GET_ACCOUNTS_PRIVILEGED | to manage multi-user service                                                     |
| feature         | android.hardware.type.automotive                                                                                                          | to use android automotive specific APIs                                          |
| library         | alliance.car                                                                                                                              | to be able to use alliance services                                              |
| library         | android.car                                                                                                                               | temporary, to access car properties                                              |

## System UI Case

No system UI customization done.
