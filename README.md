# ParkAssist application repository

## Known Issues
- commit sha1 ID : 452b24e2a9fa8019dc942e4b8a193cfcac533f3b references a bad ticket number, it should be CCSEXT-21628 instead of CCSEXT-21268

## Features

The ADAS ParkAssist system application covers following features:

| Feature | Description                               |
| ------- | ----------------------------------------- |
| UPA-FKP | Ultrasonic Park Assist & Flank Protection |
| RCTA    | Rear Cross-Traffic Alert                  |
| RVC     | Rear View Camera                          |
| AVM     | Around View Monitoring                    |
| HFP     | Hands-free Parking                        |
| FAPK    | Full Auto-ParK                            |

## Development Guide

### Emulator launch parameters

Take care to use a '-writable-system' parameter for launching your emulator, like this:

> emulator -avd <avd_name> -gpu swiftshader_indirect -no-audio -no-boot-anim -writable-system

<avd_name> is the one used by Android system once created by the AVD Manager, ie. aivi2_portrait_avd_API_29.

Alternatively to the AVD Manager, you can retrieve the list of the created avd names using this command:

> emulator -list-avds

### Build and push on device (emulator/board)

Build and Install Debug app:

> gradlew clean replaceDebug

Note: it replaces installed apk in product/priv-app by debug app, then start the DisplayService once device is booted up

Build and Install Release app (signed apk):

> gradlew clean replaceRelease

Note: it makes your unsigned release apk signed with a Google development certificate, and replaces installed apk in product/priv-app by your signed release apk

Get rid of the Release app (for basic needs):

> gradlew clean removeRelease

Note: it simply removes the installed apk in product/priv-app

Note: all these Run configurations can be retrieved under Android Studio. For all of them, a backup of the previously installed version located in product/priv-app is done on your local machine

### Build only (elementary needs)

Build the content of the repository using these commands:

Build in debug mode:

> gradlew clean assembleDebug

Build in release mode (unsigned apk):

> gradlew clean assembleRelease

## Integration Guide

- Add the artifact ParkAssist-release-unsigned.apk in a dedicated folder like
vendor/renault/prebuilts/ParkAssist
- Add its makefile "Android.mk" in the same folder
- Add following lines to `android/device/alliance/aasp/aasp.mk` :

```makefile
# Add ALLIANCE Park Assist application
PRODUCT_PACKAGES += \
    ParkAssist
```

## Runtime environment

Use following Launcher Intents to start relevant Activities

| Component                                            | Comments              | Bash alias |
| ---------------------------------------------------- | --------------------- | ---------- |
| com.renault.parkassist/.ui.MainActivity              | AVM screen launcher   |     avm    |
| com.renault.parkassist/.ui.apa.ApaActivity           | HFP screen launcher   |     hfp    |
| com.renault.parkassist/.ui.settings.SettingsActivity | ParkAssist settings   |  settings  |

You will obtain these useful aliases by sourcing these files:

```aliases
source testscripts/intent_aliases.sh
source testscripts/broadcast_aliases.sh
```

For simulated vucs commands, you can rely on test-scripts/vucs subfolders

AVM use cases: ./avm/avm-display-<view>.sh
HFP uses cases: ./apa/hfp/hfp-display-<mode>.sh

## Features Activation

See [Test guidelines](docs/test-setup.md#hardware_configuration) for features
activation/deactivation procedures.
