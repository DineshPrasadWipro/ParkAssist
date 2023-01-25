To change the display state using the command line :

* Build using the allianceStandaloneXxx flavor
* Install the app
* Reboot the device
* Issue the corresponding commands:

```sh
adb shell am startservice -a OVERLAY --es type FULLSCREEN
```

```sh
adb shell am startservice -a OVERLAY --es type WIDGET
```

```sh
adb shell am startservice -a OVERLAY --es type NONE
```
