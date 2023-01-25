# Environment setup to run the app on the board with full hardware support

## Build

### repo sync

Use the AIVI2 version with the link provided [here](https://confluence.dt.renault.com/pages/viewpage.action?spaceKey=RSWL&title=AIVI2+-+Software)

This is the mirrored version delivered by LG.

```bash
repo init -u https://gitlabee.dt.renault.com/swlabs/mirrors/android/loire/manifest-translated.git -b alliance_p.car_release -m aivi2_manifest.xml
repo sync -j4
```

### Build, flash, troubleshooting

[Here is the complete documentation.](https://confluence.dt.renault.com/pages/viewpage.action?pageId=40755782)

## Current limitations

The provided code does not seem to work fine with the oldest libais version delivered by Qualcom.

Both the libais_max9288 and libais_max9296 are present on the system and the first one seems to be picked up by default.

Current workaround is to remove the old version from the system and simply use the latest one.

### Remove old libais version

First we need to remove one of the video encoder library /vendor/lib64/libais_max9288 delivered by Qualcom to avoid the following Fatal error.

> 08-06 14:57:00.608 7289 16104 E ais_server: max9288_set_init_sequence:747 ERR [max9288 max9288_set_init_sequence ERROR] max9288 wait lock fail: 0x04:0x0
> 08-06 14:57:00.609 7289 16104 E ais_server: max9288_power_up_init_set:448 ERR [max9288 max9288_power_up_init_set ERROR] Unable to set max9288 Init Value. Fatal error!

```bash
adb root
adb shell mount -o remount,rw /dev/block/dm-1                               (remount /vendor partition in read/write)
adb shell rm /vendor/lib64/libais_max9288.so
```

You can also remove the library from your build if you prefer.

/vendor/lib64/libais_max9296 will work fine instead

Once libais_max9288 removed and EvsHelperSample app added in our build, we can launch the following process (keep such ordering):

```bash
adb shell ais_server &
adb shell android.hardware.automotive.evs@1.0-ais &
adb shell android.automotive.evs.manager@1.0 &
```

### Sample App

The sample app just displays the camera stream. It is especially useful if you want to test your hardware layer.

Copy the app inside `/vendor/lge/system/common/avc`

```bash
croot
echo "-include vendor/lge/system/common/avc/build/avc.mk" >> device/alliance/aasp/aasp.mk
```

remake

Launch Android EvsHelperSampleApp via menu (previously compiled in your AIVI2 build), see Alex L. / Vincent F. for further details
