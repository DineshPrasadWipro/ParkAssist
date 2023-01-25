 # ParkAssist Specifications

Glossary :

| Notation | Description                                   |
| -------- | --------------------------------------------- |
| AVM      | Around View Monitoring                        |
| UPA      | Ultrasonic Park Assist                        |
| FKP      | FlanK Protection                              |
| RVC      | Rear View Camera                              |
| HFP      | Hands Free Parking (= APA = Easy Park Assist) |
| APA      | Automatic Park Assist                         |
| RCTA     | Rear Cross-Traffic Alert                      |
| RAEB     | Rear Automotive Emergency Breaking            |
| OSE      | Occupant Safe Exit                            |
| FAPK     | Full Auto Park Assist                         |

## Feature: Display Sonar view when rear gear engaged [CCSEXT-2827](https://jira.dt.renault.com/browse/CCSEXT-2827)

sonar view displayed when rear gear is engaged

> Epic: [CCSEXT-3016](https://jira.dt.renault.com/browse/CCSEXT-3016)

### Scenario: Display Sonar view when rear gear engaged - w/ UPA [HMI-PARKASSIST-1]

- GIVEN CFG_ADAS_UPA_Visual is not equal to 0
- WHEN rear gear is engaged by the user
- THEN a picture is displayed representing the car from a top-view surrounded by
  colored zones showing where the obstacles are located and how close they are.

### Scenario: Display Sonar view when rear gear engaged - w/ UPA/FKP [HMI-PARKASSIST-2]

- GIVEN CFG_ADAS_UPA_Visual and CFG_ADAS_FKP are not equal to 0
- WHEN rear gear is engaged by the user
- THEN a picture is displayed representing the car from a top-view, surrounded
  by colored zones (including flank areas) showing where the obstacles are
  located and how close they are.

### Scenario: Display Sonar view when rear gear engaged - Hatched [HMI-PARKASSIST-3]

- GIVEN CFG_ADAS_UPA_Visual and CFG_ADAS_FKP are not equal to 0
- WHEN rear gear is engaged by the user
- AND obstacles are detected in a direction
- AND the steering wheel is engaged in the opposite direction
- THEN the concerned zones are hatched-grayed.

### Scenario: Display Sonar widget when low speed [HMI-PARKASSIST-4]

- GIVEN CFG_ADAS_UPA_Visual is not equal to 0
- WHEN the vehicle goes forward
- AND an obstacle is detected
- THEN a picture is displayed representing the car from a top-view, surrounded
  by colored zones showing where the obstacles are located and how close they
  are. This picture is displayed in a window on top of the current windows.

## Feature: Add mute/un-mute Sonar/RVC sound support [CCSEXT-3381](https://jira.dt.renault.com/browse/CCSEXT-3381)

implement audio service API call from sonar screen to mute / unmute audio
sounds.

> Epic: [CCSEXT-918](https://jira.dt.renault.com/browse/CCSEXT-918), [CCSEXT-930](https://jira.dt.renault.com/browse/CCSEXT-930)

### Scenario: Mute UPA/FKP sound [HMI-PARKASSIST-5]

- GIVEN Sonar View is displayed
- AND Sonar sound is not muted
- WHEN the user selects the mute button
- THEN the Sonar sound is muted

### Scenario: Un-mute UPA/FKP sound [HMI-PARKASSIST-6]

- GIVEN Sonar View is displayed
- AND Sonar sound is muted
- WHEN the user selects the un-mute button
- THEN the Sonar sound is un-muted

### Scenario: Hide Sonar sound mute button when no UPA Audio [HMI-PARKASSIST-7]

- GIVEN Sonar View is displayed
- AND UPA Audio not present
- THEN the Sonar sound mute button is not displayed

### ~~Scenario: Hide Sonar sound mute button when sound off - w/o settings [HMI-PARKASSIST-8]~~

> Deprecated: Misunderstood specification F-M10-09_STRComp_MM2020_HMI_ADAS_UPA_FKP_DisplayPart - HMI_UPA_MUTE_004
> "ParkAssist_VolumeState = Level 0" shall be interpreted as "Sound disabled"

- GIVEN Sonar View is displayed
- AND UPA Audio without settings
- AND UPA sound off
- THEN the Sonar sound mute button is not displayed

### Scenario: Show Sonar sound mute button when sound on [HMI-PARKASSIST-9]

- GIVEN Sonar View is displayed
- AND UPA Audio without settings
- AND UPA sound on
- THEN the Sonar sound mute button is visible and active

### ~~Scenario: Hide Sonar sound mute button when sound off [HMI-PARKASSIST-10]~~

> Deprecated: Misunderstood specification F-M10-09_STRComp_MM2020_HMI_ADAS_UPA_FKP_DisplayPart - HMI_UPA_MUTE_004
> "ParkAssist_VolumeState = Level 0" shall be interpreted as "Sound disabled"

- GIVEN Sonar View is displayed
- AND UPA Audio with settings
- AND UPA sound off
- THEN the Sonar sound mute button is not displayed

### ~~Scenario: Disable Sonar sound mute button when no volume [HMI-PARKASSIST-11]~~

> Deprecated: Misunderstood specification F-M10-09_STRComp_MM2020_HMI_ADAS_UPA_FKP_DisplayPart - HMI_UPA_MUTE_004
> "ParkAssist_VolumeState = Level 0" shall be interpreted as "Sound disabled"

- GIVEN Sonar View is displayed
- AND UPA Audio with settings
- AND UPA sound on
- AND UPA volume is at 0
- THEN the Sonar sound mute button is disabled

### ~~Scenario: Show Sonar sound mute button when some volume [HMI-PARKASSIST-12]~~

> Deprecated: Misunderstood specification F-M10-09_STRComp_MM2020_HMI_ADAS_UPA_FKP_DisplayPart - HMI_UPA_MUTE_004
> "ParkAssist_VolumeState = Level 0" shall be interpreted as "Sound disabled"

- GIVEN Sonar View is displayed
- AND UPA Audio with settings
- AND UPA sound on
- AND UPA volume is not at 0
- THEN the Sonar sound mute button is visible and active

### Scenario: Show disabled Sonar sound mute button when sound off - w/ settings [HMI-PARKASSIST-188]

- GIVEN Sonar View is displayed
- AND UPA Audio with settings
- AND UPA sound off
- THEN the Sonar sound mute button is displayed and disabled

## Feature: Display RVC overlay when rear gear engaged [CCSEXT-3355](https://jira.dt.renault.com/browse/CCSEXT-3355)

> Epic: [CCSEXT-3017](https://jira.dt.renault.com/browse/CCSEXT-3017)

### Scenario: Display RVC screen when rear gear engaged - w/ UPA [HMI-PARKASSIST-13]

- GIVEN RVC is present
- AND CFG_ADAS_UPA_Visual is not equal to 0
- WHEN rear gear is engaged by the user
- THEN an RVC screen is displayed: title + RVC view + a picture representing the
  car from a top-view surrounded by colored zones showing where the obstacles
  are located and how close they are

### Scenario: Display RVC screen when rear gear engaged - w/ UPA/FKP [HMI-PARKASSIST-14]

- GIVEN RVC is present
- AND CFG_ADAS_UPA_Visual and CFG_ADAS_FKP are not equal to 0
- WHEN rear gear is engaged by the user
- THEN an RVC screen is displayed: title + RVC view + a picture representing the
  car from a top-view, surrounded by colored zones (including flank areas)
  showing where the obstacles are located and how close they are.

### Scenario: Display RVC screen when rear gear engaged - Hatched [HMI-PARKASSIST-15]

- GIVEN RVC is present
- AND CFG_ADAS_UPA_Visual and CFG_ADAS_FKP are not equal to 0
- WHEN rear gear is engaged by the user
- AND obstacles are detected in a direction
- AND the steering wheel is engaged in the opposite direction
- THEN an RVC screen is displayed: title + RVC view + a picture representing the
  car from a top-view, surrounded by colored zones (including flank areas)
  showing where the obstacles are located and how close they are, the concerned
  zones are hatched-grayed.

### Scenario: Display RVC widget when low speed [HMI-PARKASSIST-16]

- GIVEN RVC is present
- AND CFG_ADAS_UPA_Visual is not equal to 0
- WHEN the vehicle goes forward and below a certain threshold
- AND an obstacle is detected
- THEN a picture is displayed representing the car from a top-view, surrounded
  by colored zones showing where the obstacles are located and how close they
  are. This picture is displayed in a window on top of the current windows.

## Feature: Display AVM regulatory views when rear gear engaged [CCSEXT-3349](https://jira.dt.renault.com/browse/CCSEXT-3349), [CCSEXT-31879](https://jira.dt.renault.com/browse/CCSEXT-31879)

> Epic: [CCSEXT-3020](https://jira.dt.renault.com/browse/CCSEXT-3020), [CCSEXT-3021](https://jira.dt.renault.com/browse/CCSEXT-3021), [CCSEXT-31880](https://jira.dt.renault.com/browse/CCSEXT-31880)

### Scenario: Display AVM standard view when rear gear engaged [HMI-PARKASSIST-17]

- GIVEN AVM is present
- WHEN the rear gear is engaged
- THEN the AVM main screen is displayed (default view: bird view + rear view):
  title, camera view area, indicator bar, bird rear/front, panoramic view, side view and 3D view
  selection buttons

### Scenario: Display AVM panoramic view when user selects panoramic [HMI-PARKASSIST-18]

- GIVEN the AVM view (bird view + rear view) is displayed
- WHEN the user selects the panoramic view
- THEN the AVM panoramic view is displayed: title, camera view area, indicator
  bar, bird rear/front and panoramic views selection buttons

### Scenario: Display AVM standard view when user selects standard [HMI-PARKASSIST-19]

- GIVEN the AVM view (panoramic view) is displayed
- WHEN the user selects the (bird + rear) view
- THEN the AVM (bird + rear) view is displayed: title, camera view area,
  indicator bar, bird rear/front and panoramic views selection buttons

### Scenario: Display Side view when user selects side [HMI-PARKASSIST-190]

- GIVEN the AVM view (bird + rear) view is displayed
- WHEN the user selects the side view
- THEN the AVM side view is displayed: title, camera view area, bird rear/front, panoramic views,
  3D view and side view selection buttons
  
### Scenario: Display 3D view when user selects 3D [HMI-PARKASSIST-191]

- GIVEN the AVM view (bird + rear) view is displayed
- WHEN the user selects the 3D view
- THEN the AVM 3D view is displayed: title, camera view area,
  indicator bar, bird rear/front, panoramic views, side view and 3D view selection buttons

## Feature: Display AVM widget when low speed and obstacle detected [CCSEXT-3351](https://jira.dt.renault.com/browse/CCSEXT-3351)

> Epic: [CCSEXT-3020](https://jira.dt.renault.com/browse/CCSEXT-3020), [CCSEXT-3021](https://jira.dt.renault.com/browse/CCSEXT-3021)

### Scenario: Display AVM popup when low speed [HMI-PARKASSIST-20]

- GIVEN AVM is present
- WHEN the speed is below a certain threshold
- AND obstacles are detected
- THEN display AVM Popup with obstacle detection information

## Feature: Display AVM view when user launches application [CCSEXT-3350](https://jira.dt.renault.com/browse/CCSEXT-3350)

> Epic: [CCSEXT-3020](https://jira.dt.renault.com/browse/CCSEXT-3020), [CCSEXT-3021](https://jira.dt.renault.com/browse/CCSEXT-3021)

### Scenario: Display AVM when user launches AVM application [HMI-PARKASSIST-21]

- GIVEN AVM is present
- WHEN the user selects AVM feature from Car control screen
- THEN display AVM main view: title, settings button, camera view area,
  indicator bar, panoramic/standard/3d/sides views selection buttons,
  camera direction indicator

## Feature: Select and display non-regulatory AVM views [CCSEXT-3352](https://jira.dt.renault.com/browse/CCSEXT-3352)

> Epic: [CCSEXT-3020](https://jira.dt.renault.com/browse/CCSEXT-3020), [CCSEXT-3021](https://jira.dt.renault.com/browse/CCSEXT-3021)

### ~~Scenario: Display AVM full bird view when user selects bird view [HMI-PARKASSIST-22]~~

> Deprecated: FBV is no more supported CCSEXT-20872

- GIVEN AVM is present AND AVM application is shown
- AND orientation is portrait
- WHEN user selects Full Bird View
- THEN Full Bird view is displayed

### Scenario: Display AVM standard view when user selects standard view [HMI-PARKASSIST-23]

- GIVEN AVM is present
- AND AVM application is shown
- WHEN user selects Standard Front View
- THEN Standard Front view is displayed

### Scenario: Display AVM panoramic view when user selects panoramic view [HMI-PARKASSIST-24]

- GIVEN AVM is present
- AND AVM application is shown
- WHEN user selects Panoramic Front View
- THEN Panoramic Front view is displayed

### Scenario: Display AVM sides view when user selects sides view [HMI-PARKASSIST-25]

- GIVEN AVM is present
- AND AVM application is shown
- WHEN user selects Sides View
- THEN Sides view is displayed

### Scenario: Display AVM 3D view when user selects 3D view [HMI-PARKASSIST-26]

- GIVEN AVM is present
- AND AVM application is shown
- WHEN user selects 3D View
- THEN 3D view is displayed

## Feature: Display HFP basic scanning view [CCSEXT-3370](https://jira.dt.renault.com/browse/CCSEXT-3370)

> Epic: [CCSEXT-924](https://jira.dt.renault.com/browse/CCSEXT-924), [CCSEXT-925](https://jira.dt.renault.com/browse/CCSEXT-925)

### Scenario: Display HFP scanning screen with sonar view - w/o AVM [HMI-PARKASSIST-27]

- GIVEN AVM not present
- AND Speed <12kph
- WHEN easy park assist is launched
- THEN scanning screen is displayed that includes : maneuver
  selection area, map area (including top view of the vehicle and sides
  indicators), instruction area, disclaimer area, sonar view (including mute
  button), settings button

### Scenario: Display HFP scanning screen with AVM standard view - w/ AVM [HMI-PARKASSIST-28]

- GIVEN AVM present
- AND Speed <12kph
- WHEN easy park assist is launched
- THEN scanning screen is displayed that includes : maneuver
  selection, map area (including top view of the vehicle and sides indicators),
  instruction area, disclaimer area, AVM standard view with sonar indications
  (including mute button), settings button

### Scenario: Display HFP scanning screen without AVM/sonar view when high speed [HMI-PARKASSIST-29]

- GIVEN Speed >12kph
- WHEN easy park assist is launched
- THEN scanning screen is displayed that includes : maneuver
  selection, map area (including top view of the vehicle and sides indicators),
  instruction area, disclaimer area, settings button (no
  sonar or AVM standard view)

### Scenario: Update map area when maneuver mode change [HMI-PARKASSIST-30]

- GIVEN scanning screen is displayed
- WHEN the user changes the maneuver mode
- THEN map area is updated accordingly (all transitions between parallel,
  perpendicular and park out have to be supported)

### Scenario: Update map area when direction change [HMI-PARKASSIST-31]

- GIVEN scanning screen is displayed
- WHEN the user selects a direction
- THEN map area is updated accordingly (left or right area is deactivated)

### Scenario: Enable/disable UPA/FKP sound when sound switch change [HMI-PARKASSIST-32]

- GIVEN scanning screen is displayed
- WHEN the user mutes/un-mutes UPA/FKP sound
- THEN UPA/FKP sound is muted/un-muted accordingly

### Scenario: Display Car Main Control screen when the user quit HFP [HMI-PARKASSIST-33]

- GIVEN scanning screen is displayed
- WHEN the user selects vehicle in navigation bar
- THEN it exits HFP screen and comes back to car main control screen

## Feature: Display main settings when selected by the user [CCSEXT-3353](https://jira.dt.renault.com/browse/CCSEXT-3353)

> Epic: [CCSEXT-617](https://jira.dt.renault.com/browse/CCSEXT-617), [CCSEXT-942](https://jira.dt.renault.com/browse/CCSEXT-942), [CCSEXT-618](https://jira.dt.renault.com/browse/CCSEXT-618), [CCSEXT-917](https://jira.dt.renault.com/browse/CCSEXT-917), [CCSEXT-6942](https://jira.dt.renault.com/browse/CCSEXT-6942), [CCSEXT-920](https://jira.dt.renault.com/browse/CCSEXT-920), [CCSEXT-921](https://jira.dt.renault.com/browse/CCSEXT-921)

### Scenario: Display main settings with RTCA switch [HMI-PARKASSIST-34]

- GIVEN RCTA is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND the user can switch On/Off the RCTA Park Assist feature

### Scenario: Display main settings with RAEB switch [HMI-PARKASSIST-35]

- GIVEN RAEB is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND the user can switch On/Off the RAEB Park Assist feature

### Scenario: Display main settings with OSE switch [HMI-PARKASSIST-36]

- GIVEN OSE is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND the user can switch On/Off the OSE Park Assist feature

### ~~Scenario: Display main settings with APA switch [HMI-PARKASSIST-37]~~

> Deprecated: APA Switch is now accessible from CAR control screen

- GIVEN APA is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND the user can select APA menu item to enter Easy-Park Assist settings
  screen

### Scenario: Display Car Control screen when user quit the settings [HMI-PARKASSIST-38]

- GIVEN the user is in the main settings Park Assist screen
- WHEN the user selects the back button
- OR vehicle in navigation bar
- THEN the car control screen is displayed

### Scenario: Display main settings with rear sonar [HMI-PARKASSIST-39]

- GIVEN UPA is supported with rear sensors only
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND it contains an ego car surrounded with rear sonar zones
- AND it contains a `rear` toggle button

### Scenario: Display main settings with front/rear sonar [HMI-PARKASSIST-40]

- GIVEN UPA is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND it contains an ego car surrounded with front and rear sonar zones
- AND it contains 2 toggle buttons `front` & `rear`

### Scenario: Display main settings with front/rear/side sonar [HMI-PARKASSIST-41]

- GIVEN UPA is supported
- AND FKP is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND it contains an ego car surrounded with 4 sonar zones
- AND it contains 3 toggle buttons `front`, `rear` & `side`

### Scenario: Highlight front sonar when enabled [HMI-PARKASSIST-42]

- GIVEN UPA is supported
- AND the main settings screen is displayed
- WHEN the user clicks the `front` toggle button
- THEN front sonar sensors group activation state toggles

### Scenario: Highlight rear sonar when enabled [HMI-PARKASSIST-43]

- GIVEN UPA is supported
- AND main settings screen is displayed
- WHEN the user clicks the `rear` toggle button
- THEN rear sonar sensors group activation state toggles

### Scenario: Highlight lateral sonar when enabled [HMI-PARKASSIST-44]

- GIVEN UPA is supported
- AND FKP is supported
- AND main settings screen is displayed
- WHEN the user clicks the `side` toggle button
- THEN sides sonar sensors group activation state toggles

### ~~Scenario: Display main settings without Camera menu [HMI-PARKASSIST-45]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC and AVM not supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the Camera menu item should not be displayed

### ~~Scenario: Display main settings with Camera menu - w/ RVC [HMI-PARKASSIST-46]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the Camera menu item should be displayed

### ~~Scenario: Display main settings with Camera menu - AVM [HMI-PARKASSIST-47]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN AVM is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the Camera menu item should be displayed

### Scenario: Display main settings without Easy Park Assist menu [HMI-PARKASSIST-48]

- GIVEN APA is not supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the Easy Park Assist menu item should not be displayed

### Scenario: Display main settings without Sound menu [HMI-PARKASSIST-49]

- GIVEN UPA Sound Settings is not supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the Sound menu item should not be displayed

### Scenario: Display main settings with Sound menu [HMI-PARKASSIST-50]

- GIVEN UPA Sound Settings is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the Sound menu item should be displayed

## ~~Feature: Display sound settings when selected by the user [CCSEXT-3354](https://jira.dt.renault.com/browse/CCSEXT-3354)~~

> Deprecated: sound settings is now part of main settings

> Epic: [CCSEXT-617](https://jira.dt.renault.com/browse/CCSEXT-617), [CCSEXT-942](https://jira.dt.renault.com/browse/CCSEXT-942)

### ~~Scenario: Display sound settings with sound switch [HMI-PARKASSIST-51]~~

> Deprecated: sound settings is now part of main settings

- GIVEN UPA supported
- OR UPA/FKP is supported
- WHEN the user selects the "Sound" menu from the Parking Assistance settings
  screen
- THEN the Parking Assistance Sound Settings screen is displayed
- AND the user can select the sound on/off switch

### ~~Scenario: Display sound settings with sound type selector [HMI-PARKASSIST-52]~~

> Deprecated: sound settings is now part of main settings

- GIVEN UPA supported
- OR UPA/FKP is supported
- WHEN the user selects the "Sound" menu from the Parking Assistance settings
  screen
- THEN the Parking Assistance Sound Settings screen is displayed
- AND the user can select sound type

### ~~Scenario: Display sound settings with sound volume [HMI-PARKASSIST-53]~~

> Deprecated: sound settings is now part of main settings

- GIVEN UPA supported
- OR UPA/FKP is supported
- WHEN the user selects the "Sound" menu from the Parking Assistance settings
  screen
- THEN the Parking Assistance Sound Settings screen is displayed
- AND the user can select the volume of the sound

### ~~Scenario: Display main settings when user presses back button [HMI-PARKASSIST-54]~~

> Deprecated: sound settings is now part of main settings

- GIVEN the user is in the Parking Assistance Sound settings screen
- WHEN user press the back button
- THEN Parking Assistance main settings screen is displayed

## Feature: Display camera settings when selected by the user [CCSEXT-3358](https://jira.dt.renault.com/browse/CCSEXT-3358)

> Epic: [CCSEXT-618](https://jira.dt.renault.com/browse/CCSEXT-917), [CCSEXT-917](https://jira.dt.renault.com/browse/CCSEXT-917), [CCSEXT-6942](https://jira.dt.renault.com/browse/CCSEXT-6942), [CCSEXT-920](https://jira.dt.renault.com/browse/CCSEXT-920), [CCSEXT-921](https://jira.dt.renault.com/browse/CCSEXT-921)

### ~~Scenario: Display camera settings with luminosity selector [HMI-PARKASSIST-55]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC or AVM is present
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed
- AND the user can set a luminosity value through seekbar

### ~~Scenario: Display camera settings with contrast selector [HMI-PARKASSIST-56]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC or AVM is present
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed
- AND the user can set a contrast value through seekbar

### ~~Scenario: Display camera settings with color selector [HMI-PARKASSIST-57]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC or AVM is present
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed
- AND the user can set a color value through seekbar

### ~~Scenario: Display camera settings with fixed guidelines switch [HMI-PARKASSIST-58]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC or AVM is present
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed
- AND the user can activate/deactivate the fixed guidelines with the toggle
  switch

### ~~Scenario: Display camera settings with dynamic guidelines switch [HMI-PARKASSIST-59]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC or AVM is present
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed
- AND the user can activate/deactivate the dynamic guidelines with the toggle
  switch

### ~~Scenario: Display main settings when user presses back button [HMI-PARKASSIST-60]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN the user is in the Parking Assistance Camera settings screen
- WHEN the user presses the back button
- THEN Parking Assistance main settings screen is displayed

## Feature: Add RCTA information in Sonar view [CCSEXT-3377](https://jira.dt.renault.com/browse/CCSEXT-3377)

> Epic: [CCSEXT-922](https://jira.dt.renault.com/browse/CCSEXT-922), [CCSEXT-923](https://jira.dt.renault.com/browse/CCSEXT-923)

### Scenario: Display left RCTA in Sonar view on left detection [HMI-PARKASSIST-61]

- GIVEN RCTA is supported
- AND activated by the user
- AND Rear Gear is engaged
- AND AVM is not present
- AND UPA/FKP is present
- WHEN a coming vehicle or obstacle is detected on the left
- THEN the Left RCTA is displayed in Sonar view
- AND RCTA sound is generated

### Scenario: Display right RCTA in Sonar view on right detection [HMI-PARKASSIST-62]

- GIVEN RCTA is supported
- AND activated by the user
- AND Rear Gear is engaged
- AND AVM is not present
- AND UPA/FKP is present
- WHEN a coming vehicle or obstacle is detected on the right
- THEN the Right RCTA is displayed in Sonar view
- AND RCTA sound is generated

### Scenario: Display both RCTA in Sonar view on left & right detection [HMI-PARKASSIST-63]

- GIVEN RCTA is supported
- AND activated by the user
- AND Rear Gear is engaged
- AND AVM is not present
- AND UPA/FKP is present
- WHEN a coming vehicle or obstacle is detected on the left
- AND a coming vehicle or obstacle is detected on the right
- THEN the Left RCTA is displayed in Sonar view
- AND the Right RCTA is displayed in Sonar view
- AND RCTA sound is generated

## Feature: Add RCTA information in RVC view [CCSEXT-3378](https://jira.dt.renault.com/browse/CCSEXT-3378)

> Epic: [CCSEXT-922](https://jira.dt.renault.com/browse/CCSEXT-922), [CCSEXT-923](https://jira.dt.renault.com/browse/CCSEXT-923)

### Scenario: Display left RCTA in RVC view on left detection [HMI-PARKASSIST-64]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND RVC is present
- WHEN a coming vehicle or obstacle is detected on the left
- THEN the Left RCTA is displayed in RVC camera view
- AND RCTA sound is generated

### Scenario: Display right RCTA in RVC view on right detection [HMI-PARKASSIST-65]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND RVC is present
- WHEN a coming vehicle or obstacle is detected on the right
- THEN the Right RCTA is displayed in RVC camera view
- AND RCTA sound is generated

### Scenario: Display both RCTA in RVC view on left & right detection [HMI-PARKASSIST-66]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND RVC is present
- WHEN a coming vehicle or obstacle is detected on both the left and the right
- THEN the Left and Right RCTA is displayed in RVC camera view
- AND RCTA sound is generated

## Feature: Add RCTA information in AVM view [CCSEXT-3379](https://jira.dt.renault.com/browse/CCSEXT-3379)

> Epic: [CCSEXT-922](https://jira.dt.renault.com/browse/CCSEXT-922), [CCSEXT-923](https://jira.dt.renault.com/browse/CCSEXT-923)

### Scenario: Display left RCTA in AVM panoramic front view on left detection [HMI-PARKASSIST-67]

- GIVEN RCTA is supported and activated by the user
- AND AVM Panoramic Front View is displayed
- WHEN a coming vehicle or obstacle is detected on the left
- THEN the Left RCTA is not displayed in AVM camera view
- AND no RCTA sound is generated

### Scenario: Display right RCTA in AVM panoramic front view on right detection [HMI-PARKASSIST-68]

- GIVEN RCTA is supported and activated by the user
- AND AVM Panoramic Front View is displayed
- WHEN a coming vehicle or obstacle is detected on the right
- THEN the Right RCTA is not displayed in AVM camera view
- AND no RCTA sound is generated

### Scenario: Display both RCTA in AVM panoramic front view on left & right detection [HMI-PARKASSIST-69]

- GIVEN RCTA is supported and activated by the user
- AND AVM Panoramic Front View is displayed
- WHEN a coming vehicle or obstacle is detected on both the left and the right
- THEN Left RCTA is not displayed in AVM camera view
- AND Right RCTA is not displayed in AVM camera view
- AND no RCTA sound is generated

### ~~Scenario: Hide RCTA in AVM panoramic view when no detection [HMI-PARKASSIST-70]~~

> RCTA widget are already not displayed in front view 

- GIVEN RCTA is supported and activated by the user
- AND AVM Panoramic View is displayed
- WHEN no obstacle is detected
- THEN no RCTA are displayed in AVM camera view

### Scenario: Display left RCTA in AVM Panoramic rear view on left detection [HMI-PARKASSIST-71]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Panoramic rear View is displayed
- WHEN a coming vehicle or obstacle is detected on the left
- THEN the Left RCTA is displayed in AVM camera view
- AND RCTA sound is generated

### Scenario: Display right RCTA in AVM Panoramic rear view on right detection [HMI-PARKASSIST-72]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Panoramic View is displayed
- WHEN a coming vehicle or obstacle is detected on the right
- THEN the Right RCTA is displayed in AVM camera view
- AND RCTA sound is generated

### Scenario: Display both RCTA in AVM panoramic rear view on left & right detection [HMI-PARKASSIST-73]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Panoramic rear View is displayed
- WHEN a coming vehicle or obstacle is detected on both the left and the right
- THEN Left RCTA is displayed in AVM camera view
- AND Right RCTA is displayed in AVM camera view
- AND RCTA sound is generated

### Scenario: Hide RCTA in AVM panoramic rear view when no detection [HMI-PARKASSIST-74]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Panoramic Rear View is displayed
- WHEN no obstacle is detected
- THEN no RCTA are displayed in AVM camera view

### Scenario: Display left RCTA in AVM standard rear view on left detection [HMI-PARKASSIST-75]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Rear View is displayed
- WHEN a coming vehicle or obstacle is detected on the left
- THEN the Left RCTA is displayed in AVM camera view
- AND RCTA sound is generated

### Scenario: Display right RCTA in AVM standard rear view on right detection [HMI-PARKASSIST-76]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Rear View is displayed
- WHEN a coming vehicle or obstacle is detected on the right
- THEN the Right RCTA is displayed in AVM camera view
- AND RCTA sound is generated

### Scenario: Display both RCTA in AVM standard rear view on left & right detection [HMI-PARKASSIST-77]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Side View is displayed
- WHEN a coming vehicle or obstacle is detected on both the left and the right
- THEN Left RCTA is displayed in AVM camera view
- AND Right RCTA is displayed in AVM camera view
- AND RCTA sound is generated

### Scenario: Hide RCTA in AVM standard rear view when no detection [HMI-PARKASSIST-78]

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Rear View is displayed
- WHEN no obstacle is detected
- THEN no RCTA are displayed in AVM camera view

### Scenario: Display left RCTA in AVM standard front view on left detection [HMI-PARKASSIST-79]

- GIVEN RCTA is supported and activated by the user
- AND AVM Standard Front View is displayed
- WHEN a coming vehicle or obstacle is detected on the left
- THEN the Left RCTA is not displayed in AVM camera view
- AND no RCTA sound is not generated

### Scenario: Display right RCTA in AVM front standard view on right detection [HMI-PARKASSIST-80]

- GIVEN RCTA is supported and activated by the user
- AND AVM Standard Front View is displayed
- WHEN a coming vehicle or obstacle is detected on the right
- THEN the Right RCTA is not displayed in AVM camera view
- AND no RCTA sound is not generated

### Scenario: Display both RCTA in AVM front standard view on left & right detection [HMI-PARKASSIST-81]

- GIVEN RCTA is supported and activated by the user
- AND AVM Standard View is displayed
- WHEN a coming vehicle or obstacle is detected on both the left and the right
- THEN Left RCTA is not displayed in AVM camera view
- AND Right RCTA is not displayed in AVM camera view
- AND no RCTA sound is generated

### ~~Scenario: Hide RCTA in AVM standard view when no detection [HMI-PARKASSIST-82]~~

> RCTA widget are already not displayed in front view 

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Standard View is displayed
- WHEN no obstacle is detected
- THEN no RCTA are displayed in AVM camera view

### ~~Scenario: Hide RCTA in AVM front view on left and right detection [HMI-PARKASSIST-181]~~

> Duplicated to [HMI-PARKASSIST-81]

- GIVEN RCTA is supported and activated by the user
- AND AVM Front View is displayed
- WHEN obstacle is detected
- THEN no RCTA are displayed in AVM camera view

## Feature: Add RCTA information in AVM bird view [CCSEXT-4634](https://jira.dt.renault.com/browse/CCSEXT-4634)

> Epic: [CCSEXT-922](https://jira.dt.renault.com/browse/CCSEXT-922), [CCSEXT-923](https://jira.dt.renault.com/browse/CCSEXT-923)

### ~~Scenario: Display left RCTA in AVM bird view on left detection [HMI-PARKASSIST-83]~~

> Deprecated: FBV is no more supported CCSEXT-20872

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Bird View is displayed
- WHEN a coming vehicle or obstacle is detected on the left
- THEN the Left RCTA is displayed in AVM camera view
- AND RCTA sound is generated

### ~~Scenario: Display right RCTA in AVM bird view on right detection [HMI-PARKASSIST-84]~~

> Deprecated: FBV is no more supported CCSEXT-20872

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Bird View is displayed
- WHEN a coming vehicle or obstacle is detected on the right
- THEN the Right RCTA is displayed in AVM camera view
- AND RCTA sound is generated

### ~~Scenario: Display both RCTA in AVM bird view on left & right detection [HMI-PARKASSIST-85]~~

> Deprecated: FBV is no more supported CCSEXT-20872

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Bird View is displayed
- WHEN a coming vehicle or obstacle is detected on both the left and the right
- THEN Left RCTA is displayed in AVM camera view
- AND Right RCTA is displayed in AVM camera view
- AND RCTA sound is generated

### ~~Scenario: Hide RCTA in AVM bird view when no detection [HMI-PARKASSIST-86]~~

> Deprecated: FBV is no more supported CCSEXT-20872

- GIVEN RCTA is supported and activated by the user
- AND Rear Gear is engaged
- AND AVM Bird View is displayed
- WHEN no obstacle is detected
- THEN no RCTA are displayed in AVM views

## Feature: AVM Touch and Gesture [CCSEXT-6475](https://jira.dt.renault.com/browse/CCSEXT-6475)

> Epic: **[CCSEXT-6726](https://jira.dt.renault.com/browse/CCSEXT-6726)**

### Scenario: Rotate around 3D view with one finger [HMI-PARKASSIST-87]

- GIVEN AVM 3D View is displayed
- WHEN user touches the Avm 3D View
- AND drags one finger on the AVM 3D View
- THEN AVM 3D view is moving from initial finger position to final position.

### Scenario: Zoom in and out 3D view with two fingers [HMI-PARKASSIST-88]

- GIVEN AVM 3D View is displayed
- WHEN user touches the AVM 3D View with two fingers simultaneously
- THEN AVM 3D view is zoomed in or out depending on the touch gestures.

### Scenario: Mute/un-mute button intercepts touch events over 3D view [HMI-PARKASSIST-89]

- GIVEN AVM 3D View is displayed
- AND Mute button is present (in its center)
- WHEN user touches the Mute button on the AVM 3D View
- THEN Mute button is toggled accordingly (and AVM 3D view does not change)

## Feature: Camera direction indicator in AVM views [CCSEXT-8052](https://jira.dt.renault.com/browse/CCSEXT-8052)

> Epic: [CCSEXT-3020](https://jira.dt.renault.com/browse/CCSEXT-3020)

### Scenario: front AVM camera direction indicator [HMI-PARKASSIST-90]

- GIVEN AVM is present
- AND AVM application is shown
- AND front camera is displayed
- WHEN user selects Standard or Panoramic view
- THEN front camera indicator is displayed

### Scenario: rear AVM camera direction indicator [HMI-PARKASSIST-91]

- GIVEN AVM is present
- AND AVM application is shown
- AND rear camera is displayed
- WHEN user selects Standard or Panoramic view
- THEN rear camera indicator is displayed

### ~~Scenario: no AVM camera direction indicator [HMI-PARKASSIST-92]~~

> Deprecated: a 360° camera indicator is displayed in 3D View

- GIVEN AVM is present
- AND AVM application is shown
- AND rear camera is displayed
- WHEN user selects sides or 3D view
- THEN camera indicator is not displayed

### Scenario: no AVM camera direction indicator in sides view [HMI-PARKASSIST-185]

- GIVEN AVM is present
- AND AVM application is shown
- AND front camera is displayed
- WHEN user selects sides
- THEN camera indicator is not displayed

### Scenario: 360° AVM camera indicator in 3D view [HMI-PARKASSIST-186]

- GIVEN AVM is present
- AND AVM application is shown
- AND front camera is displayed
- WHEN user selects 3D View
- THEN 360° camera indicator is displayed

## Feature:  [CCSEXT-8040](https://jira.dt.renault.com/browse/CCSEXT-8040)

> Epic: [CCSEXT-7281](https://jira.dt.renault.com/browse/CCSEXT-7281)

### Scenario: Display main settings when OSE feature is not supported [HMI-PARKASSIST-93]

- GIVEN OSE is not supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND the OSE toggle is not displayed

### Scenario: Enable OSE feature in main settings [HMI-PARKASSIST-94]

- GIVEN OSE is supported but disabled
- AND the main settings screen is displayed
- WHEN the user clicks the `ose` toggle button
- THEN OSE settings button is toggled to enabled (neither audio nor visual feedback other than button state)

### Scenario: Disable OSE feature in main settings [HMI-PARKASSIST-95]

- GIVEN OSE is supported and enabled
- AND the main settings screen is displayed
- WHEN the user clicks the `ose` toggle button
- THEN OSE settings button is toggled to disabled (neither audio no visual feedback other than button state)

## Feature: Display Easy Park Assist - Scanning view [CCSEXT-4117](https://jira.dt.renault.com/browse/CCSEXT-4117)

> Epic: [CCSEXT-924](https://jira.dt.renault.com/browse/CCSEXT-924), [CCSEXT-925](https://jira.dt.renault.com/browse/CCSEXT-925)

### Scenario: display left parking slot available [HMI-PARKASSIST-96]

- GIVEN APA scanning screen is displayed
- WHEN a slot on the left is detected
- THEN a parking available indicator appears over the left slot

### Scenario: display right parking slot available [HMI-PARKASSIST-97]

- GIVEN APA scanning screen is displayed
- WHEN a slot on the right is detected
- THEN a parking available indicator appears over the right slot

### Scenario: display left & right parking slots available [HMI-PARKASSIST-98]

- GIVEN APA scanning screen is displayed
- WHEN slots on the left&right are detected
- THEN a parking available indicator appears over the left&right slot

### Scenario: display left parking slot selected [HMI-PARKASSIST-99]

- GIVEN APA scanning screen is displayed
- AND left slot is detected
- WHEN the user selects the left slot
- THEN the a big P is displayed in the left slot

### Scenario: display right parking slot selected [HMI-PARKASSIST-100]

- GIVEN APA scanning screen is displayed
- AND right slot is detected
- WHEN the user selects the right slot
- THEN the a big P is displayed in the right slot

## Feature: Display Easy Park Assist - Guidance view [CCSEXT-3371](https://jira.dt.renault.com/browse/CCSEXT-3371)

> Epic: [CCSEXT-924](https://jira.dt.renault.com/browse/CCSEXT-924), [CCSEXT-925](https://jira.dt.renault.com/browse/CCSEXT-925)

### Scenario: Display AVM guidance (Landscape) [HMI-PARKASSIST-101]

- GIVEN AVM is present
- AND scanning phase park-in maneuver has detected slot
- WHEN the user selects the slot
- AND scanning phase is completed
- THEN the guidance screen is displayed and contains guidance instruction text
- AND parkassist pictogram
- AND maneuver gauge
- AND front/rear + bird view camera
- AND mute button
- AND camera selection button
- AND disclaimer text

### Scenario: Display AVM guidance (Portrait) [HMI-PARKASSIST-102]

- GIVEN AVM is present
- AND scanning phase park-in maneuver has detected slot
- WHEN the user selects the slot
- AND scanning phase is completed
- THEN the guidance screen is displayed and contains guidance instruction text
- AND parkassist pictogram
- AND maneuver gauge
- AND guidance instruction illustration
- AND front/rear + bird view camera
- AND mute button
- AND disclaimer text

### Scenario: Display RVC guidance (Landscape) [HMI-PARKASSIST-103]

- GIVEN RVC is present
- AND scanning phase park-in maneuver has detected slot
- WHEN the user selects the slot
- AND scanning phase is completed
- THEN the guidance screen is displayed and contains guidance instruction text
- AND parkassist pictogram
- AND maneuver gauge
- AND rear view camera
- AND sonar view
- AND mute button
- AND camera selection button
- AND disclaimer text

### Scenario: Display RVC guidance (Portrait) [HMI-PARKASSIST-104]

- GIVEN RVC is present
- AND scanning phase park-in maneuver has detected slot
- WHEN the user selects the slot
- AND scanning phase is completed
- THEN the guidance screen is displayed and contains guidance instruction text
- AND parkassist pictogram
- AND maneuver gauge
- AND guidance instruction illustration
- AND rear view camera
- AND sonar view
- AND mute button
- AND disclaimer text

### Scenario: Display instruction illustration (Landscape) [HMI-PARKASSIST-105]

- GIVEN landscape AVM/RVC guidance maneuver screen is displayed
- AND camera view is displayed
- WHEN the user selects the non-camera view (unselect camera button)
- THEN the guidance illustration is displayed instead of the camera view

### Scenario: Display camera view (Landscape) [HMI-PARKASSIST-106]

- GIVEN landscape AVM/RVC guidance maneuver screen is displayed
- AND guidance illustration is displayed
- WHEN the user selects the camera view (select camera button)
- THEN the camera view is displayed instead of the guidance illustration

### Scenario: Gauge update [HMI-PARKASSIST-107]

- GIVEN AVM/RVC guidance maneuver screen is displayed
- WHEN the gauge information is updated
- THEN the gauge display is updated

### Scenario: Gauge visibility update [HMI-PARKASSIST-108]

- GIVEN AVM/RVC guidance maneuver screen is displayed
- WHEN the extended instruction is updated
- THEN the gauge visibility is updated accordingly

### Scenario: Gauge color update [HMI-PARKASSIST-109]

- GIVEN AVM/RVC guidance maneuver screen is displayed
- WHEN the extended instruction is updated
- THEN the gauge visibility is updated accordingly


### Scenario: Guidance instruction update [HMI-PARKASSIST-110]

- GIVEN AVM/RVC guidance maneuver screen is displayed
- WHEN the maneuver state is changing
- THEN the instruction label is updated accordingly

## Feature: Display Easy Park Assist - Settings view [CCSEXT-3369](https://jira.dt.renault.com/browse/CCSEXT-3369)

> Epic: [CCSEXT-931](https://jira.dt.renault.com/browse/CCSEXT-931), [CCSEXT-932](https://jira.dt.renault.com/browse/CCSEXT-932), [CCSEXT-929](https://jira.dt.renault.com/browse/CCSEXT-929)

### Scenario: Access settings screen with settings button [HMI-PARKASSIST-111]

- GIVEN APA scanning screen are displayed
- WHEN the user press the settings button
- THEN the APA setting screen is displayed with the current default selected maneuver

### Scenario: Change default maneuver mode to perpendicular [HMI-PARKASSIST-112]

- GIVEN APA settings screen is displayed
- AND current default maneuver parallel
- WHEN the user selects the perpendicular maneuver
- THEN perpendicular maneuver button is highlighted
- AND the default maneuver is changed to perpendicular

### Scenario: Change default maneuver mode to parallel [HMI-PARKASSIST-113]

- GIVEN APA settings screen is displayed
- AND current default maneuver perpendicular
- WHEN the user selects the parallel maneuver
- THEN parallel maneuver button is highlighted
- AND the default maneuver is changed to parallel

### Scenario: Go back to APA from settings [HMI-PARKASSIST-114]

- GIVEN APA settings screen is displayed
- WHEN the user press the back button
- THEN initial screen (scanning) is displayed

## Feature: Extra Camera settings & configuration [CCSEXT-3372](https://jira.dt.renault.com/browse/CCSEXT-3372), [CCSEXT-5543](https://jira.dt.renault.com/browse/CCSEXT-5543)

> Epic: [CCSEXT-618](https://jira.dt.renault.com/browse/CCSEXT-918), [CCSEXT-917](https://jira.dt.renault.com/browse/CCSEXT-917), [CCSEXT-6942](https://jira.dt.renault.com/browse/CCSEXT-6942), [CCSEXT-6728](https://jira.dt.renault.com/browse/CCSEXT-6728), [CCSEXT-920](https://jira.dt.renault.com/browse/CCSEXT-920), [CCSEXT-921](https://jira.dt.renault.com/browse/CCSEXT-921)

### ~~Scenario: Display camera settings with trailer guidelines switch [HMI-PARKASSIST-115]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC or AVM is present
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed
- AND the user can activate/deactivate the trailer guidelines with the toggle switch

### ~~Scenario: Display camera settings with auto-zoom switch [HMI-PARKASSIST-116]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC or AVM is present
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed
- AND the user can activate/deactivate the auto-zoom with the toggle switch

### ~~Scenario: Dynamic guidelines feature unavailable [HMI-PARKASSIST-117]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC is present
- AND Dynamic Guidelines vehicule configuration is enabled
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed with no dynamic guidelines switch

### ~~Scenario: Static guidelines feature unavailable [HMI-PARKASSIST-118]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC is present
- AND Static Guidelines vehicule configuration is enabled
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed with no static guidelines switch

### ~~Scenario: Trailer guidelines feature unavailable [HMI-PARKASSIST-119]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC or AVM is present
- AND Trailer Guidelines vehicule configuration is enabled
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed with no trailer guidelines switch

### ~~Scenario: Auto-zoom feature unavailable [HMI-PARKASSIST-120]~~

> Deprecated: camera settings is no more accessible from main settings screen

- GIVEN RVC or AVM is present
- AND Auto-zoom vehicule configuration is enabled
- WHEN the user selects the "Camera" menu from the Parking Assistance settings screen
- THEN the Parking Assistance Camera Settings screen is displayed with no auto-zoom switch

## Feature: Display RAEB alerts [CCSEXT-8847](https://jira.dt.renault.com/browse/CCSEXT-8847)

> Epic: [CCSEXT-927](https://jira.dt.renault.com/browse/CCSEXT-927), [CCSEXT-928](https://jira.dt.renault.com/browse/CCSEXT-928)

### Scenario: RAEB in sonar view [HMI-PARKASSIST-121]

GIVEN RAEB is supported
AND RAEB is activated by the user
AND Rear Gear is engaged
AND AVM is not present
AND UPA/FKP is present
WHEN an obstacle is detected
THEN the 2 RAEB icons are displayed in sonar view

### Scenario: RAEB in RVC camera view [HMI-PARKASSIST-122]

GIVEN RAEB is supported
AND RAEB is activated by the user
AND Rear Gear is engaged
AND RVC is present
WHEN an obstacle is detected
THEN the 2 RAEB icons are displayed in RVC camera view

### Scenario: RAEB OFF in RVC camera view [HMI-PARKASSIST-182]

GIVEN RAEB is supported
AND RAEB is desactivated by the user
AND Rear Gear is engaged
AND RVC is present
WHEN an obstacle is detected
THEN the RAEB icon 'Off' is displayed in RVC camera view

### Scenario: RAEB in AVM standard & camera views [HMI-PARKASSIST-123]

GIVEN RAEB is supported
AND RAEB is activated by the user
AND Rear Gear is engaged
AND AVM is present
WHEN an obstacle is detected
THEN the 2 RAEB icons are displayed in AVM Camera view
AND the 2 RAEB icons are displayed in AVM Standard View

### Scenario: RAEB OFF in AVM standard & camera views [HMI-PARKASSIST-183]

GIVEN RAEB is supported
AND RAEB is desactivated by the user
AND Rear Gear is engaged
AND AVM is present
WHEN an obstacle is detected
THEN the RAEB icon 'Off' is displayed in AVM Camera view

### Scenario: RAEB in AVM standard & camera front views [HMI-PARKASSIST-180]

GIVEN RAEB is supported
AND RAEB is activated by the user
AND AVM Front View is displayed
WHEN an obstacle is detected
THEN the no RAEB icons are displayed

### Scenario: RAEB OFF in AVM standard & camera front views [HMI-PARKASSIST-184]

GIVEN RAEB is supported
AND RAEB is desactivated by the user
AND AVM Front View is displayed
WHEN an obstacle is detected
THEN the RAEB icon 'Off' is not displayed

### Scenario: RAEB not available and activated [HMI-PARKASSIST-124]

GIVEN RAEB is supported
AND RAEB is activated by the user
AND AVM Rear View is displayed
WHEN RAEB is not functional
THEN the RAEB icon 'Off' is displayed in camera View

### Scenario: RAEB not available and deactivated [HMI-PARKASSIST-179]

GIVEN RAEB is supported
AND AVM Rear View is displayed
AND RAEB is deactivated by the user
WHEN RAEB is not functional
THEN the RAEB icon 'Off' is displayed in camera View

### Scenario: RAEB not activated [HMI-PARKASSIST-125]

GIVEN RAEB is supported
AND AVM Rear View is displayed
WHEN RAEB is deactivated by the user
THEN the RAEB icon 'Off' is displayed in camera View

## Feature: Display camera settings [CCSEXT-10155](https://jira.dt.renault.com/browse/CCSEXT-10155)

> Epic: [CCSEXT-618](https://jira.dt.renault.com/browse/CCSEXT-618), [CCSEXT-917](https://jira.dt.renault.com/browse/CCSEXT-917), [CCSEXT-920](https://jira.dt.renault.com/browse/CCSEXT-920), [CCSEXT-921](https://jira.dt.renault.com/browse/CCSEXT-921)

### Scenario: Display Camera settings when user presses settings button - AVM [HMI-PARKASSIST-126]

- GIVEN AVM is present
- AND AVM application is shown
- WHEN the user selects the "settings" icon from the AVM non-regulatory screen
- THEN the AVM Camera settings screen is displayed

### Scenario: Display AVM screen when use presses back button - AVM [HMI-PARKASSIST-127]

- GIVEN AVM Camera settings screen is displayed
- WHEN the user press the back button
- THEN initial screen (AVM non-regulatory) is displayed

### Scenario: Display Camera settings when user presses settings button - AVM Regulatory [HMI-PARKASSIST-128]

- GIVEN AVM is present
- AND AVM regulatory view is displayed (rear gear engaged by the user)
- WHEN the user selects the "settings" icon from the AVM regulatory screen
- THEN the AVM Camera settings screen is displayed

### Scenario: Display AVM screen when use presses back button - AVM Regulatory [HMI-PARKASSIST-129]

- GIVEN AVM Camera settings screen is displayed
- WHEN the user press the back button
- THEN initial screen (AVM regulatory) is displayed

### Scenario: Display Camera settings when user presses settings button - RVC [HMI-PARKASSIST-130]

- GIVEN RVC is present
- AND RVC view is displayed (rear gear engaged by the user)
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the RVC Camera settings screen is displayed

### Scenario: Display RVC screen when use presses back button - RVC [HMI-PARKASSIST-131]

- GIVEN RVC Camera settings screen is displayed
- WHEN the user press the back button
- THEN initial RVC screen is displayed

## Feature: Display FAPK scanning view [CCSEXT-10678](https://jira.dt.renault.com/browse/CCSEXT-10678)

> Epic: [CCSEXT-926](https://jira.dt.renault.com/browse/CCSEXT-926)

### Scenario: Display FAPK screen - AVM [HMI-PARKASSIST-132]

- GIVEN FAPK is present
- AND car control main screen is displayed
- WHEN easy park assist is launched by the user
- THEN FAPK scanning screen is displayed with 3 maneuvers buttons
- AND a start button
- AND a camera view
- AND a settings button
- AND an instruction label
- AND a disclaimer label

### Scenario: Display Maneuver button only when feature available [HMI-PARKASSIST-187]

- GIVEN FAPK scanning screen is displayed
- AND Maneuver is present or not
- THEN the Maneuver buttons is displayed or removed accordingly

### Scenario: Activate FAPK maneuver button when user selects a maneuver [HMI-PARKASSIST-133]

- GIVEN FAPK scanning screen is displayed
- WHEN the user changes the maneuver mode
- THEN the corresponding selected maneuver button is highlighted

### Scenario: Enable/disable UPA/FKP sound when sound switch change [HMI-PARKASSIST-134]

- GIVEN FAPK scanning screen is displayed
- WHEN the user mutes or unmutes the sound
- THEN mute button is updated accordingly
- AND and audio is muted/unmuted accordingly

### Scenario: Display camera direction indication icon [HMI-PARKASSIST-135]

- GIVEN FAPK scanning screen is displayed
- WHEN the camera direction changes
- THEN the camera indication icon is updated accordingly

### Scenario: Display instruction label [HMI-PARKASSIST-136]

- GIVEN FAPK scanning screen is displayed
- WHEN the step of the scanning procedure changes
- THEN the instruction label is updated accordingly

### Scenario: Display RCTA indicator [HMI-PARKASSIST-137]

- GIVEN FAPK scanning screen is displayed
- AND RCTA is present
- WHEN an RCTA event occurs
- THEN the RCTA icon is displayed or removed accordingly

### Scenario: Display RAEB indicator [HMI-PARKASSIST-138]

- GIVEN FAPK scanning screen is displayed
- AND RAEB is present
- WHEN an RAEB event occurs
- THEN the RAEB indication is displayed or removed accordingly

### Scenario: Select parking slot on user touch [HMI-PARKASSIST-139]

- GIVEN FAPK scanning screen is displayed,
- WHEN the user selects a slot
- THEN parking slot is selected

## Feature: Display FAPK guidance view [CCSEXT-10679](https://jira.dt.renault.com/browse/CCSEXT-10679)

> Epic: [CCSEXT-926](https://jira.dt.renault.com/browse/CCSEXT-926)

### Scenario: start FAPK guidance - AVM [HMI-PARKASSIST-140]

- GIVEN FAPK scanning screen is displayed
- AND start button is enabled
- WHEN the user press the start button
- THEN FAPK guidance screen is displayed

### Scenario: Display FAPK guidance - AVM [HMI-PARKASSIST-141]

- GIVEN FAPK guidance screen is displayed
- AND stop button is enabled
- THEN 3 maneuvers button are displayed
- AND a stop button
- AND a camera view
- AND a settings button
- AND an instruction label
- AND a disclaimer label
- AND a camera direction icon
- AND an easy park icon
- AND a mute button

### Scenario: Enable/disable UPA/FKP sound when sound switch change [HMI-PARKASSIST-142]

- GIVEN FAPK guidance screen is displayed
- WHEN the user mutes or unmutes the sound
- THEN mute button is updated accordingly
- AND and audio is muted/unmuted accordingly

### Scenario: Display camera direction indication icon [HMI-PARKASSIST-143]

- GIVEN FAPK guidance screen is displayed
- WHEN the camera direction changes
- THEN the camera indication icon is updated accordingly

### Scenario: Display instruction label [HMI-PARKASSIST-144]

- GIVEN FAPK guidance screen is displayed
- WHEN the step of the scanning procedure changes
- THEN the instruction label is updated accordingly

### Scenario: Display RCTA indicator [HMI-PARKASSIST-145]

- GIVEN FAPK guidance screen is displayed
- AND RCTA is present
- WHEN an RCTA event occurs
- THEN the RCTA icon is displayed or removed accordingly

### Scenario: Display RAEB indicator [HMI-PARKASSIST-146]

- GIVEN FAPK guidance screen is displayed
- AND RAEB is present
- WHEN an RAEB event occurs
- THEN the RAEB indication is displayed or removed accordingly

### Scenario: Display FAPK Scanning when rear gear is engaged in FAPK Settings [HMI-PARKASSIST-189]

- GIVEN FAPK guidance screen is displayed
- AND current screen is FAPK settings
- WHEN user engage Rear Gear
- AND extended instruction is not "engage rear gear"
- THEN displayed screen should be FAPK scanning

## Feature: Display Trailer view [CCSEXT-12944](https://jira.dt.renault.com/browse/CCSEXT-12944)

> Epic: [CCSEXT-9036](https://jira.dt.renault.com/browse/CCSEXT-9036)

### Scenario: Display Trailer view when user launches AVM application [HMI-PARKASSIST-147]

- GIVEN Trailer is present
- WHEN the user selects AVM feature from Car control screen
- THEN display Trailer view: title, camera view area, camera direction indicator

## Feature: HFP / FAPK Warning Dialog Box [CCSEXT-4168](https://jira.dt.renault.com/browse/CCSEXT-4168)

> Epic: [CCSEXT-9585](https://jira.dt.renault.com/browse/CCSEXT-9585)

### Scenario: Display HFP warning [HMI-PARKASSIST-148]
- GIVEN HFP scanning or guidance screen active
- WHEN a warning is triggered (APA_WarningDisplayRequest2)
- THEN a popup "InteractiveCenterPopup (1 or 2 buttons)" is displayed with the corresponding text

### Scenario: Display FAPK warning [HMI-PARKASSIST-149]
- GIVEN FAPK scanning or guidance screen active
- WHEN a warning is triggered (APA_WarningDisplayRequest2)
- THEN a popup "InteractiveCenterPopup (1 or 2 buttons)" is displayed with the corresponding text

### Scenario: FAPK/HFP warning user acknowledgment [HMI-PARKASSIST-150]
- GIVEN a warning dialog box is displayed
- WHEN the user presses a button
- THEN the IVI sends corresponding acknowledgement (APA_UserAcknowledgement 1 or APA_UserAcknowledgement 2)

### Scenario: FAPK/HFP warning dialog box removal [HMI-PARKASSIST-151]
- GIVEN a warning dialog box is displayed
- WHEN a MESSAGE_NONE warning request is received
- THEN the warning dialog box should disappear

## Feature: Display AVM warnings and errors [CCSEXT-10903](https://jira.dt.renault.com/browse/CCSEXT-10903)

> Epic: [CCSEXT-9036](https://jira.dt.renault.com/browse/CCSEXT-9585)

### Scenario: Display AVM camera warning [HMI-PARKASSIST-152]

- GIVEN AVM present
- AND a warning is detected
- WHEN the user selects camera button in car control main menu
- THEN a 1-button "OK" Dialog box is displayed with explication text

### Scenario: Display trailer warning messages [HMI-PARKASSIST-153]

- GIVEN trailer is attached
- AND (RVC or AVM is present)
- AND warning is detected
- WHEN the user selects camera button in car control main menu
- THEN a 1-button "OK" Dialog box is displayed with explication text

### Scenario: Stop displaying warning  [HMI-PARKASSIST-154]

- GIVEN AVM or trailer warning is shown
- WHEN warning state change to none
- THEN the dialog box disappears

### Scenario: Display camera failure icon  [HMI-PARKASSIST-155]

- GIVEN AVM or RVC view is displayed
- WHEN an error is detected
- THEN a camera failure icon is displayed on top of the camera view

## Feature: Extra Camera settings & configuration [CCSEXT-3372](https://jira.dt.renault.com/browse/CCSEXT-3372), [CCSEXT-5543](https://jira.dt.renault.com/browse/CCSEXT-5543), [CCSEXT-11911](https://jira.dt.renault.com/browse/CCSEXT-11911)

> Epic: [CCSEXT-618](https://jira.dt.renault.com/browse/CCSEXT-918), [CCSEXT-917](https://jira.dt.renault.com/browse/CCSEXT-917), [CCSEXT-6942](https://jira.dt.renault.com/browse/CCSEXT-6942), [CCSEXT-6728](https://jira.dt.renault.com/browse/CCSEXT-6728), [CCSEXT-920](https://jira.dt.renault.com/browse/CCSEXT-920), [CCSEXT-921](https://jira.dt.renault.com/browse/CCSEXT-921),[CCSEXT-9056](https://jira.dt.renault.com/browse/CCSEXT-9056)

### Scenario: Display camera settings with luminosity selector [HMI-PARKASSIST-156]

- GIVEN RVC or AVM is present
- AND RVC or AVM view is displayed (rear gear engaged by the user)
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the Camera settings screen is displayed
- AND the user can set a luminosity value through seekbar

### Scenario: Display camera settings with contrast selector [HMI-PARKASSIST-157]

- GIVEN RVC or AVM is present
- AND RVC or AVM view is displayed (rear gear engaged by the user)
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the Camera settings screen is displayed
- AND the user can set a contrast value through seekbar

### Scenario: Display camera settings with color selector [HMI-PARKASSIST-158]

- GIVEN RVC or AVM is present
- AND RVC or AVM view is displayed (rear gear engaged by the user)
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the Camera settings screen is displayed
- AND the user can set a color value through seekbar

### Scenario: Display camera settings with fixed guidelines switch [HMI-PARKASSIST-159]

- GIVEN RVC or AVM is present
- AND RVC or AVM view is displayed (rear gear engaged by the user)
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the Camera settings screen is displayed
- AND the user can activate/deactivate the fixed guidelines with the toggle
  switch

### Scenario: Display camera settings with dynamic guidelines switch [HMI-PARKASSIST-161]

- GIVEN RVC or AVM is present
- AND RVC or AVM view is displayed (rear gear engaged by the user)
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the Camera settings screen is displayed
- AND the user can activate/deactivate the dynamic guidelines with the toggle
  switch

### Scenario: Display camera settings with trailer guidelines switch [HMI-PARKASSIST-162]

- GIVEN RVC or AVM is present
- AND RVC or AVM view is displayed (rear gear engaged by the user)
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the RVC Camera settings screen is displayed
- AND the user can activate/deactivate the trailer guidelines with the toggle switch

### Scenario: Display camera settings with auto-zoom switch [HMI-PARKASSIST-163]

- GIVEN AVM is present
- AND AVM view is displayed (rear gear engaged by the user)
- WHEN the user selects the "settings" icon from the AVM screen
- THEN the Camera settings screen is displayed
- AND the user can activate/deactivate the auto-zoom with the toggle switch

### Scenario: Dynamic guidelines feature unavailable [HMI-PARKASSIST-164]

- GIVEN RVC or AVM is present
- AND RVC or AVM view is displayed (rear gear engaged by the user)
- AND Dynamic Guidelines vehicle feature is not supported
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the Camera settings screen is displayed with no dynamic guidelines switch

### Scenario: Static guidelines feature unavailable [HMI-PARKASSIST-165]

- GIVEN RVC or AVM is present
- AND RVC or AVM view is displayed (rear gear engaged by the user)
- AND Static Guidelines vehicle feature is not supported
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the Camera settings screen is displayed with no static guidelines switch

### Scenario: Trailer guidelines feature unavailable [HMI-PARKASSIST-166]

- GIVEN RVC or AVM is present
- AND RVC or AVM view is displayed (rear gear engaged by the user)
- AND Trailer Guidelines vehicle feature is not supported
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the Camera settings screen is displayed with no trailer guidelines switch

### Scenario: Auto-zoom feature unavailable [HMI-PARKASSIST-167]

- GIVEN RVC or AVM is present
- AND RVC or AVM view is displayed (rear gear engaged by the user)
- AND Auto-zoom vehicle feature is not supported
- WHEN the user selects the "settings" icon from the RVC screen
- THEN the Camera settings screen is displayed with no auto-zoom switch

## Feature: Display Dealer view [CCSEXT-17467](https://jira.dt.renault.com/browse/CCSEXT-17467)

> Epic: [CCSEXT-7097](https://jira.dt.renault.com/browse/CCSEXT-7097)

### Scenario: Display Dealer screen - AVM  [HMI-PARKASSIST-168]

- GIVEN AVM is present
- WHEN dealer mode is triggered
- THEN display Dealer mode screen: camera view area

### Scenario: Display Dealer screen - RVC  [HMI-PARKASSIST-169]

- GIVEN RVC is present
- WHEN dealer mode is triggered
- THEN display Dealer mode screen: camera view area

## Feature: Camera on trunk [CCSEXT-11967](https://jira.dt.renault.com/browse/CCSEXT-11967)

> Epic: [CCSEXT-9056](https://jira.dt.renault.com/browse/CCSEXT-9056)

### Scenario: Display boot open warning with camera on trunk [HMI-PARKASSIST-170]

- GIVEN AVM or RVC is present
- AND rear camera is on trunk
- WHEN a rear camera view is displayed
- AND boot is open
- THEN 'boot open' notification is displayed on camera view

### Scenario: Hide boot open warning with camera on trunk [HMI-PARKASSIST-171]

- GIVEN AVM or RVC is present
- AND rear camera is on trunk
- WHEN a rear camera view is displayed
- AND boot is closed
- THEN 'boot open' notification is NOT displayed on camera view

### Scenario: Hide boot open warning with camera NOT on trunk [HMI-PARKASSIST-172]

- GIVEN AVM or RVC is present
- AND rear camera is NOT on trunk
- WHEN a rear camera view is displayed
- THEN 'boot open' notification is never displayed on camera view

## Feature: All settings on a single screen [CCSEXT-11967](https://jira.dt.renault.com/browse/CCSEXT-11967)

> Epic: [CCSEXT-11749](https://jira.dt.renault.com/browse/CCSEXT-11749), [CCSEXT-617](https://jira.dt.renault.com/browse/CCSEXT-617), [CCSEXT-942](https://jira.dt.renault.com/browse/CCSEXT-942)

### Scenario: Display main settings with sound switch [HMI-PARKASSIST-173]

- GIVEN UPA supported
- OR UPA/FKP is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the Parking Assistance Sound Settings screen is displayed
- AND the user can select the sound on/off switch

### Scenario: Display sound settings with sound type selector [HMI-PARKASSIST-174]

- GIVEN UPA supported
- OR UPA/FKP is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the Parking Assistance Sound Settings screen is displayed
- AND the user can select sound type

### Scenario: Display sound settings with sound volume [HMI-PARKASSIST-175]

- GIVEN UPA supported
- OR UPA/FKP is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the Parking Assistance Sound Settings screen is displayed
- AND the user can select the volume of the sound

### ~~Scenario: Display main settings with SOUND switch [HMI-PARKASSIST-176]~~

> Deprecated: Duplicated to HMI-PARKASSIST-173

- GIVEN sound activation control is supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND the user can switch On/Off the Sound Park Assist feature

### Scenario: Display main settings with SOUND expandable [HMI-PARKASSIST-177]

- GIVEN sound activation control is not supported
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND the user can not switch On/Off the Sound Park Assist feature
- AND the user can expand/collapse the sound option

### Scenario: Display main settings without REAR UPA options [HMI-PARKASSIST-178]

- GIVEN UPA rear config not supported or UPA visual settings feature not present
- WHEN the user selects the settings button of Parking Assistance
- THEN the main settings screen is displayed
- AND the user can not see rear UPA option

>
> /!\ To update when add new scenarios
> **Next ID to use** : HMI-PARKASSIST-192
>>>>>>> feat: All settings on a single screen
>
