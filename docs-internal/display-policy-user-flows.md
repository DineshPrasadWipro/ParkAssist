# User's flows

## Reference docs

- [User flow - AVM intersystem](https://teams.microsoft.com/l/file/29E203AB-DD5C-4616-85C8-BE445A016CD7?tenantId=d6b0bbee-7cd9-4d60-bce6-4a67b543e2ae&fileType=pdf&objectUrl=https%3A%2F%2Fgrouperenault.sharepoint.com%2Fteams%2FRNSLTeamProjectHMIDomain%2FShared%20Documents%2FPark%20Assist%2FLatest%20Specifications%2FAVM%2FAVMDigital_intersystem_V32_3.pdf&baseUrl=https%3A%2F%2Fgrouperenault.sharepoint.com%2Fteams%2FRNSLTeamProjectHMIDomain&serviceName=teams&threadId=19:ce2833d1b66b46f0b12d38e91bb92632@thread.skype&groupId=ac40ddd7-3da1-4b69-965d-7fe0aa98ba91)
- [User flow - RVC intersystem](https://teams.microsoft.com/l/file/1D4C87C6-52D8-448A-B073-2F51991618F0?tenantId=d6b0bbee-7cd9-4d60-bce6-4a67b543e2ae&fileType=pptx&objectUrl=https%3A%2F%2Fgrouperenault.sharepoint.com%2Fteams%2FRNSLTeamProjectHMIDomain%2FShared%20Documents%2FPark%20Assist%2FLatest%20Specifications%2FRVC%2FRVC_intersystem_V2.pptx&baseUrl=https%3A%2F%2Fgrouperenault.sharepoint.com%2Fteams%2FRNSLTeamProjectHMIDomain&serviceName=teams&threadId=19:ce2833d1b66b46f0b12d38e91bb92632@thread.skype&groupId=ac40ddd7-3da1-4b69-965d-7fe0aa98ba91)
- User flow - Sonar intersystem. _Same than RVC?_
- [APA specifications](https://teams.microsoft.com/_#/files/Park%20Assist?threadId=19:ce2833d1b66b46f0b12d38e91bb92632@thread.skype&ctx=channel&context=Latest%2520Specifications%252FAPA%2520HFP).
  _Flows included in AVM user flow doc_
- [FAPK specifications](https://teams.microsoft.com/_#/files/Park%20Assist?threadId=19:ce2833d1b66b46f0b12d38e91bb92632@thread.skype&ctx=channel&context=Latest%2520Specifications%252FFAPK).
  _Flows included in AVM user flow doc_
- [Screen Priority Matrix](https://teams.microsoft.com/l/file/6EEC46E3-8366-46E4-B6B9-68CDC5F40341?tenantId=d6b0bbee-7cd9-4d60-bce6-4a67b543e2ae&fileType=xlsx&objectUrl=https%3A%2F%2Fgrouperenault.sharepoint.com%2Fteams%2FRNSLTeamProjectHMIDomain%2FShared%20Documents%2FPark%20Assist%2FF-M01-06_A-IVI2_Renault_PriorityScreen_v3_00.xlsx&baseUrl=https%3A%2F%2Fgrouperenault.sharepoint.com%2Fteams%2FRNSLTeamProjectHMIDomain&serviceName=teams&threadId=19:ce2833d1b66b46f0b12d38e91bb92632@thread.skype&groupId=ac40ddd7-3da1-4b69-965d-7fe0aa98ba91)

- [illustration interactions écrans ADAS Parking](https://grouperenault.sharepoint.com/:p:/r/teams/RNSLTeamProjectHMIDomain/Shared%20Documents/Park%20Assist/Latest%20Specifications/illustration%20interactions%20%C3%A9crans%20ADAS%20Parking.pptx?d=w0b6bd00ec75e429f9b32b2a13a5b1221&csf=1&e=wS81xU)
- [Interactions_Ecran_ADAS_Parking_2_Draft](https://grouperenault.sharepoint.com/:u:/r/teams/RNSLTeamProjectHMIDomain/Shared%20Documents/Park%20Assist/Latest%20Specifications/Interactions_Ecran_ADAS_Parking_2_Draft.vsdx?d=w708a4f0821b14387b602f5aaec1c4db5&csf=1&e=wwzjbV)

## User flows

### RVC

Reference Documentation:
[RVC_intersystem_V2.pptx](https://teams.microsoft.com/_#/pptx/viewer/teams/https:~2F~2Fgrouperenault.sharepoint.com~2Fteams~2FRNSLTeamProjectHMIDomain~2FShared%20Documents~2FPark%20Assist~2FLatest%20Specifications~2FRVC~2FRVC_intersystem_V2.pptx?threadId=19:ce2833d1b66b46f0b12d38e91bb92632@thread.skype&baseUrl=https:~2F~2Fgrouperenault.sharepoint.com~2Fteams~2FRNSLTeamProjectHMIDomain&fileId=1d4c87c6-52d8-448a-b073-2f51991618f0&ctx=files&rootContext=items_view&viewerAction=view)

#### Quick RVC flows recap

This paragraph only cover gen1 use cases

- RVC opening due to Rear Gear Engaged
- RVC closing due to RVC conditions NOK (eg: speed...)
- RVC settings access, without RVC already displayed: user clicks on control
  center icon.
- RVC settings access, with RVC already displayed: user clicks on RVC overlay
  setting icon

### AVM

Reference document:
[AVMDigital_intersystem_V32_3.pdf](https://teams.microsoft.com/_#/pdf/viewer/teams/https:~2F~2Fgrouperenault.sharepoint.com~2Fteams~2FRNSLTeamProjectHMIDomain~2FShared%20Documents~2FPark%20Assist~2FLatest%20Specifications~2FAVM~2FAVMDigital_intersystem_V32_3.pdf?threadId=19:ce2833d1b66b46f0b12d38e91bb92632@thread.skype&baseUrl=https:~2F~2Fgrouperenault.sharepoint.com~2Fteams~2FRNSLTeamProjectHMIDomain&fileId=29e203ab-dd5c-4616-85c8-be445a016cd7&ctx=files&rootContext=items_view&viewerAction=view)

#### Quick AVM flows recap

**In Bold** are the not correctly handled use cases or questions/open points.

##### AVM digital popup

- AVM Pop-up opening due to UPA/FKP obstacle

  - Sonar send a display request to AVM ECU
  - AVM send Pop-up Layout request to IVI

- AVM Pop-up closing due to Sonar Request
- AVM Pop-up closing due to user request by pushing Close Popup button

##### AVM Digital Views

- AVM Opening due to user request by pushing AVM button
- AVM User Activation when AVM PopUp already displayed
  - **Pop-up windows shall be closed**
- AVM User Activation after user closed AVM popup
  - **AVM should be opened with no UPA sound (no visible Button - no overlay is
    mentionned in the doc - or just muted by default?**
  - **if another pop-up request is then sent, AVM screen should remain but with
    sound activated + overlays(?)**
- AVM User Activation but AVM conditions speed NOK - usecase 1
  - **There is notion of user push and user release sw button not clearly
    defined.**
  - **Interpretation:**
    - **AVM application should not be launched at all if conditions are not
      met.** _May feels a little buggy to user_
    - **AVM app is launched only if conditions are met and there is more than 3s
      between successive user push on the button**
  - **What is the "AVM msg: Speed NOK"? A pop display for user or is it just
    internal to the doc?**
- AVM User Activation but AVM conditions speed NOK but with new push <3s -
  usecase 2a
  - **Interpretation: intervall between push button two short, app still not
    launched**
  - **warning popup message not displayed again**
  - **Warning pop-up duration should not exceed 3s?**
- AVM User Activation but AVM conditions speed NOK but with new push <3s -
  usecase 2b
  - **???**
- AVM User Activation but AVM conditions speed NOK but with new push >3s -
  usecase 3
  - **Interpretation: warning pop-up displayed again if another press done after
    3s**
- AMV Opening due to user request
- View Change due to user request on screen
  - **Transition screen to be handled**
- View change due to AVM request. **means application already launched by
  user?**
  - **Looks like AVM screen still displayed. Does this mean there is no switch
    to overlay mode?**
- Layout change due to user command on screen, _with AVM priority_
  - **Difference seems to be that display status is sent before change request**
- AVM closing due to user request by pushing AVM button
- AVM closing due to AVM ECU Request
  - Ok in regulatory case
  - **in case of AVM beeing opened by the user, no warning messsage?**
- AVM closing but Sonar obstacles still present (AVM popup)
  - **Back to home screen with AVM pop-up**

##### AVM digital settings

- AVM Settings activation in settings menu without RearView displayed before
- AVM settings activation in settings menu, with AVM popup without RearView
  displayed before
  - **pop-up windows shall be closed (even if obstacle still detected?)**
- AVM settings activation in AVM screen without RearView displayed before
- AVM settings activation in AVM screen WITH RearView displayed before.
  **regulatory use case?**
  - **Settings still to be displayed but no distinctions compared with previous
    case regarding overlay mode**
- AVM settings FV deactivation, after settings activation in settings menu due
  to Back button
  - Go back to Home screen
  - **"Launched from setings menu": guess this is is from car control center
    since going back to home screen**
  - **why is FV important?**
- AVM settings FV deactivation, after settings activation in AVM screen due to
  Back button
  - Going back to AVM screen
- AVM settings RV deactivation due to Back button
  - **Assuming that settings launched the same way than previous use case**
  - Back to AVM screen
- AVM settings deactivation & AVM deactivation due to vehicle speed not valid
  - **transition to home page if RESPONSE_TIME_FAILURE**
  - **AVM view with black screen otherwise**
- AVM settings FV deactivation, UPA/FKP obstacles due to Back button
  - Pop-up should be displayed when settings are closed
  - From settings use case 2, we know that settings screen takes priority over
    sonar pop-up

##### APA AVM (FAPK)

- FAPk activation without AVM/Popup before - _Car Control isonly accessible if
  Neutral/Drive, sowhenAPA activatedFrontViewisdisplayedby default_
- FAPk activation WITH AVM already displayed - _Only possible if APA button is
  available/displayed on AVM screen_
  - **Comment suggest this is with apa bubble capability.**
- FAPk activation when Popup layout already displayed
  - Remove pop-up and display FAPK
- FAPk scan with Front View Masked @High speed
  - APA_ViewMaskRequest true if >12km/h, false otherwise
  - **when true, "FAPk screen With Front View NOT displayedIn Scan mode": does
    this mean FAPK with no camera view or no FAPK screen at all?**
- FAPk Transition Scan/Manoeuvre/ParkOut
  - Depends on APA_MapContentDisplay field
- FAPk deactivation due to USER REQUEST without UPA/FKP obstacles & without
  RearGear Engaged
  - Back to home page
- FAPk deactivation due to USER REQUEST WITH RearGear Engaged
  - **Back to regulatory AVM screen**
- FAPk deactivation due to USER REQUEST WITH UPA/FKP obstacles & without
  RearGear Engaged
  - Back to home with Pop-up
- FAPk deactivation due to AVM REQUEST withoutUPA/FKP obstacles &
  withoutRearGear Engaged
  - Back to home page
- FAPk deactivation due to AVM REQUEST WITH RearGear Engaged
  - Back to AVM regulatory screen
- FAPk deactivation due to AVM REQUEST WITH UPA/FKP obstacles & without RearGear
  Engaged

  - Back to home with Pop-up

##### APA Sonar with AVM Digital (HFP/HFPB/APK)

Many slides mentions some CAN Signal response time conditions. Assumption is
made that this is hanled/filtered at lower layers (SVS) than HMI which then only
respond to SVS requests.

- APA.SONAR activation With vehicle speed < 12kph, and without UPA/FKP obstacles
  - HFP screen displayed but with or without AVM view depending
- APA.SONAR activation With vehicle speed > 12kph, and withoutUPA/FKP obstacles
  - HFP screen without AVM views
- APA.SONAR activation With vehicle speed < 12kph, and WITH UPA/FKP obstacles
  - HFP screen displayed with or without AVM views
- APA.SONAR activation WITH AVM already displayed before but Layout != BV+RV or
  BV+FV
  - HFP Screen displayed with or without AVM views
- APA.SONAR activation WITH AVM already displayed before WITH Layout = BV+RV or
  BV+FV
  - HFP Screen displayed with AVM views
- APA.SONAR activation But AVM transitions : display requested/not requested
  - HFP screen on, then AVM view toggle on off depending on speed condition
- APA.SONAR deactivation WITH AVM activated (V<12kph) Without rear gear engaged,
  and withoutUPA/FKP obstacles
  - Back to home
- APA.SONAR deactivation WITH AVM activated (V<12kph) Without rear gear engaged,
  and WITH UPA/FKP obstacles
  - Back to home + pop-up
- APA.SONAR deactivation WITH AVM activated (V<12kph) WITH rear gear engaged
  - Back to AVM regulatory screen
- APA.SONAR deactivation without AVM activated (V>12kph) Without rear gear
  engaged, and withoutUPA/FKP obstacles

  - Back to Home

##### Trailer view

- Trailer View opening due to user request, by pushing the Trailer button
  - Display Trailer view
- Trailer View opening due to user request, by pushing the Trailer button but
  condition 1 NOK
  - **Message alert displayed during 6s max**
  - **Condition 1 = Trailer not connected**
- Trailer View opening due to user request, by pushing the Trailer button but
  condition 2 NOK
  - Same than previous
  - **Condition 2 = Trailer no access**
- Trailer View opening due to user request, by pushing the Trailer button but
  condition NOK then 2nd push OK
  - Open view at second push (**even if second push <6s?**)
- Trailer View closing due to user request by pushing the Trailer button
  - Back to home page
- Trailer View closing due to UPA obstacles
  - Home + pop-up
- Trailer View closing due to AVM request (RearGear Engaged)
  - to regulatory AVM Screen
- Trailer View closing due to TimeOut or Trailer unplugged (AVM ECU)
  - Trailer view with black camera view
