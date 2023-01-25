# Setup Test Environment

## Use the following helper scripts

### 1. Setup a Vehicle Configuration

All the vehicle configurations are handled by a separate project:
https://gitlabee.dt.renault.com/swlabs/cc/ivi/android/product/loire/renault/vehicle_configs.git

- Clone it locally in your preferred workspace folder

```code
git clone https://gitlabee.dt.renault.com/swlabs/cc/ivi/android/product/loire/renault/vehicle_configs.git
```

- Select and push a vehicle configuration:
(script will guide you to choose the right configuration!)
```code
cd vehicle_configs
./push_vehicle_config.sh
```

### 2. Select camera mode on needs

|camera mode|             script               |
|   :---:   |----------------------------------|
|  Digital  |```./scripts/set_digital_cam.sh```|
|  Analog   |```./scripts/set_analog_cam.sh``` |

- It enables to get a full video stream displayed by setting proper fov parameters (get rid of black bars)

### 3. How to install ParkAssist debug app

Ensure you select `debug` variant under Android Studio, then run:

```code
./scripts/openthedoor.sh
```

- It enabled to get rid of persistent flag restrictions coming with the ParkAssist release version
- It also backups locally the delivered ParkAssist version embedded in the CUB release
- Once executed on the board, you can go back to Android Studio for usual debugging usage

Note: you can use `-h` parameter to see all fine scripts options
