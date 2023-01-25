# Display Policy

## Discussions

### Constraints

#### Multi-user constraints

[Automotive documentation](https://source.android.com/devices/tech/admin/multi-user#android-automotive-multi-user)

##### Display Service constraints

Application has been declared with `persistent = true` This way, the service is never shut down even
by the Android Out Of Memory Manager. Therefore, there is no need to use a Foreground service.

The service is also started as singleUser.

A first `BOOT_COMPLETED` intent is received by system user (u0) quite quickly with a successful
attempt to start the service

-   call of `onCreate()` that start the service
-   call of `onStartCommand()` doing nothing

A second `BOOT_COMPLETED` intent is received by current user (u10) few seconds later, still
attempting to start the service:

-   only `onStartCommand()` is called, **service is not started twice**

`onStartCommand` could be removed.

##### Broadcast Receiver constraints

As documented [here](https://source.android.com/devices/tech/admin/multiuser-apps), `singleUser`
option also applies to receivers. Still, `BootBroadCastReceiver` receives two `BOOT_COMPLETED`
intents for system and current users.

##### Repositories

Tests done with `SurroundViewRepository`

One instance of repository is created for System User at start-up. This instance is used for
regulatory use cases.

When an activity is launched by user, a new process is created with new repository instance. **In
this case, when user kill the activity, process is endend without proper unregister from services
(Surround, Sonar...)**

#### User switch

As stated in the
[documentation](https://source.android.com/devices/tech/admin/multi-user#android-automotive-multi-user),
`persistent` property allows the service not to be killed during a user switch.

User creation and user switch has been successfully tested while an overlay is displayed. User
switch is done with the overlay always on top. When switch is completed, overlay can be removed as
usual (e.g. engage neutral gear)

#### Regulatory vs user

### Architecture proposals

#### Full AWOS

Display service would use AWOS to display every screens: both regulatories and non regulatories.

##### Pros

-   No more usage of window manager
    -   Every UI Components can be written as Activity/Fragments
-   AWOS support 3 layers with 15 inner priorities. This should ensure compliancy with
    [Renault priority matrix](https://teams.microsoft.com/l/file/6EEC46E3-8366-46E4-B6B9-68CDC5F40341?tenantId=d6b0bbee-7cd9-4d60-bce6-4a67b543e2ae&fileType=xlsx&objectUrl=https%3A%2F%2Fgrouperenault.sharepoint.com%2Fteams%2FRNSLTeamProjectHMIDomain%2FShared%20Documents%2FPark%20Assist%2FF-M01-06_A-IVI2_Renault_PriorityScreen_v3_00.xlsx&baseUrl=https%3A%2F%2Fgrouperenault.sharepoint.com%2Fteams%2FRNSLTeamProjectHMIDomain&serviceName=teams&threadId=19:ce2833d1b66b46f0b12d38e91bb92632@thread.skype&groupId=ac40ddd7-3da1-4b69-965d-7fe0aa98ba91)
    at least for regulatory use case (see Cons)
-   Arbitration DisplayPolicy easy to centralize at the same place (DisplayService)

##### Cons

-   AWOS cannot display an Activity in same layer than a regular Activity. This is an issue for non
    regulatory use cases (activities launced by user such as APA. settings...)

    -   A solution could be to launch a blank activity that handle AWOS within its lifecycle.
    -   Drawback of this solution is that arbitration might be a bit scattered.

#### Display service aware of activities

This would be a mix: DisplayService/AWOS responsible of regulatory use cases and non regulatory
launched as regular activities (user intents)

##### Pros

-   No more issue with non regulatory use case

##### Cons

-   Need to find a way to make DisplayService aware of a priority activity ongoing Some possible
    solutions:
    -   Register DisplayService to activities lifecycle callbacks --> need to check if there is a
        risk that an activity is killed without the service being warned (memory killer?)
    -   Info might be available from already connected services (SurroundView, Sonar ...)

#### Use window manager

Same solution than [Display service aware of activities](Display-service-aware-of-activities) but
using WindowManager instead of AWOS

##### Pros

-   No real benefits

##### Cons

-   No more benefits from AWOS functionnalities
-   Regulatory screen manually built, cannot uses activites/fragment and navigation facilities.

## Proof of concepts prio list

-   Multiuser management
-   Blank activity for a full AWOS solution
-   Service aware of priority activity for mixed AWOS solution
