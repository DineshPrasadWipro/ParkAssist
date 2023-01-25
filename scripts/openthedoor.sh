#!/bin/bash

SCRIPT_VERSION="0.7"
SCRIPT_DATE="Nov-24-2020"

SCRIPT_PATH=$(dirname $0)
ROOT_PATH="$SCRIPT_PATH/.."

# import common functions (i.e. spinner, warn, error)
. $SCRIPT_PATH/common.sh

OS="`uname`"
case $OS in
  'Linux')
    ANDROID_SDK_HOME="$HOME/Android/Sdk"
    ;;
  *)
    # Assuming this is MACOS
    ANDROID_SDK_HOME="$HOME/Library/Android/sdk"
    ;;
esac

BUILD_TOOLS_VERSION=$(ls -l $ANDROID_SDK_HOME/build-tools | tail -1 | awk '{print $NF}')
BUILD_TOOLS_PATH=$ANDROID_SDK_HOME/build-tools/$BUILD_TOOLS_VERSION
APKSIGNER="$BUILD_TOOLS_PATH/apksigner"
ZIPALIGN="$BUILD_TOOLS_PATH/zipalign"

SIGN_TOOLS="$SCRIPT_PATH/sign-tools"

APP_IML_FILE="$ROOT_PATH/ParkAssist/ParkAssist-ParkAssist.iml"
ASSEMBLE_TASK_NAME_ATTRS="ASSEMBLE_TASK_NAME"
ASSEMBLE_TASK_RELEASE="assembleRelease"
ASSEMBLE_TASK_DEBUG="assembleDebug"

PROJECT_NAME="ParkAssist"
PACKAGE_NAME="com.renault.parkassist"

MAIN_ACTIVITY="ui.MainActivity"

TARGET_PATH="/product/priv-app/$PROJECT_NAME"

CUB_RELEASE=$(adb wait-for-device shell getprop ro.build.version.incremental)
BACKUP_FOLDER=$PROJECT_NAME"_$CUB_RELEASE"_backup_"$(date +%Y%m%d.%H%M%S)"

CFG_ADAS_UPA_Visual="hmi_config/adas/upa_visual"
CFG_ADAS_FKP="hmi_config/adas/fkp"

CFG_ADAS_UPA_Audio="hmi_config/adas/upa_audio"
CFG_ADAS_UPA_Mute="hmi_config/adas/upa_mute"

CFG_ADAS_RCTA="hmi_config/adas/rcta"
CFG_ADAS_RAEB="hmi_config/adas/raeb"
CFG_ADAS_OSE="hmi_config/adas/ose"

CFG_ADAS_RVC="rvc_config/rvc_feature/rvcpresent"
CFG_ADAS_AVM="hmi_config/adas/avm_d"

CFG_ADAS_APA="hmi_config/adas/apa"


USE_EVS_CAMERA="persist.car.config.adas.use.evs.camera"

function usage
{
cat << EOF

usage: ./$(basename $0) [-b|--build] [-d|--demo <apk_filename>] [-h|--help]

version $SCRIPT_VERSION - $SCRIPT_DATE

Pre-requisites:
    AIVI2 board connected and Android Car system launched up to UI

Description:
    Replace $PROJECT_NAME release app in /product by project app (based on current variant)

    [Debug only] For Android Studio configuration, edit your 'Run configuration' as follow:
    - Go to Run->Edit Configurations
       1. Set [Deploy] box to 'Nothing'
       2. Keep [Launch Options] and [Deployment Target Options] to the default settings
       3. Under [Before Launch], create a new one with + -> Run External tool -> +
          a. Fill up Name and Description as you want
          b. Under [Program], select this script $(basename $0) located in ./scripts
          c. Under [Working directory], ensure it is well specified to ./scripts folder
          d. Confirm twice your changes with OK in each box
    - Then 'Apply' your changes

Arguments:
    -h, --help                                    display this help and exit

    -c, --config  [rvc_a|rvc_d]                   configure camera fov: RVC Analog/Digital
    -b, --build                                   build $PROJECT_NAME based on current build variant (outside Android Studio)
    -r, --removeonly                              remove only $PROJECT_NAME in /product
    -d, --demo <apk_filename>                     install local <apk_filename>
    -v, --variant [debug|release|...]             specify variant as parameters (else it takes selected variant in Android Studio)
    
Deprecated (to be fixed):
    -m, --mode    [real|simu]        select camera Mode: simulated|real camera

i.e:
    a) replace $PROJECT_NAME release app in /product by provided app
    ./$(basename $0) -d ParkAssist-release-unsigned.apk

    b) replace $PROJECT_NAME release app in /product by project app
    ./$(basename $0)

    c) build and install $PROJECT_NAME in /product partition
    ./$(basename $0) -b

    d) remove only $PROJECT_NAME in /product partition
    ./$(basename $0) -r



    Tips: think about triggering rear gear to display ParkAssist regulatory screens

EOF
}

# Set default values
removeonly=false
build=false
install=true
demo=false

# Check options
while [ "$1" != "" ]; do
    case $1 in
        -c | --config)              shift
                                    config=$1           # CFG_ADAS_RVC/CFG_ADAS_AVM
                                    ;;
        -m | --mode)                shift
                                    mode=$1             # real|simu
                                    ;;
        -b | --build)               build=true
                                    ;;
        -r | --removeonly)          removeonly=true
		                    install=false
                                    ;;
        -d | --demo)                shift
                                    APK_NAME=$1
                                    demo=true
                                    install=false
                                    ;;
        -v | --variant)             shift
                                    BUILD_VARIANT=$1
                                    ;;
        -h | --help )               usage
                                    exit 0
                                    ;;
        * )                         echo "Invalid argument: $1"                    
                                    usage
                                    exit 1
    esac
    shift
done

if $demo && [[ -z $APK_NAME ]]; then
    echo "Error: missing <apk_filename> parameter for 'demo' option"
    exit 1
fi

if [[ -z $BUILD_VARIANT ]]; then
    BUILD_VARIANT="release"
fi

if [[ $BUILD_VARIANT != 'debug' ]] && [[ $BUILD_VARIANT != 'release' ]]; then
    echo "Error: invalid parameter for -v option, can be either 'debug' or 'release'"
    exit 1
fi

read_iml () {
local IFS=\>
    read -d \< ENTITY CONTENT
    local ret=$?
    TAG_NAME=${ENTITY%% *}
    ATTRIBUTES=${ENTITY#* }
    return $ret
}

parse_iml () {
    if [[ $TAG_NAME == "option" ]] ; then
        eval local $ATTRIBUTES 2>/dev/null
        #echo "option name is: $name"
        if [[ $name = $ASSEMBLE_TASK_NAME_ATTRS ]] ; then
            task_name=$value
            return
        fi
    fi
    false
}

get_build_variant() {
    while read_iml; do
       if parse_iml ; then break; fi
    done < $APP_IML_FILE

    if [[ $task_name = $ASSEMBLE_TASK_RELEASE ]] ; then
        BUILD_VARIANT="release"
    elif [[ $task_name = $ASSEMBLE_TASK_DEBUG ]] ; then
        BUILD_VARIANT="debug"
    else
        echo "Unknown task name: $task_name"
        echo "Check configuration file: $APP_IML_FILE and its attribute $ASSEMBLE_TASK_NAME_ATTRS"
        exit
    fi
    echo "== Build Variant: $BUILD_VARIANT =="
    APK_NAME=$PROJECT_NAME"-"$BUILD_VARIANT
    if [[ $BUILD_VARIANT == 'release' ]] ; then
       APK_NAME=$APK_NAME"-unsigned"
    fi
    APK_NAME=$APK_NAME".apk"
    # Select generated project APK
    APK_NAME="$ROOT_PATH/$PROJECT_NAME/build/outputs/apk/$BUILD_VARIANT/$APK_NAME"
}

build_apk() {
    echo "== Build $PROJECT_NAME in $BUILD_VARIANT =="
    # Compile the APK: you can adapt this for production build, flavors, etc.
    pushd $ROOT_PATH/$PROJECT_NAME > /dev/null
    ../gradlew $task_name --configure-on-demand --daemon || exit -1 # exit on failure
    popd > /dev/null
}

push_apk() {
    make_system_writable
    adb wait-for-device shell mkdir -p $TARGET_PATH
    adb wait-for-device push $APK_NAME $TARGET_PATH
}

check_boot_completed() {
    BOOT_COMPLETED=$(adb wait-for-device shell 'getprop dev.bootcomplete' 2>/dev/null)
    if [[ $BOOT_COMPLETED != '1' ]] ; then
        echo "Waiting for device to be connected and booted up..."
        adb wait-for-device shell 'while ! getprop dev.bootcomplete|grep -q 1 ; do sleep 1; done'
    fi
}

check_camera_mode() {
    if [[ $cur_evs_camera == 'true' ]] ; then
        cur_mode='real'
    else 
        cur_mode='simu'
    fi
    check_selected_camera
}

check_selected_camera() {
    if [[ `adb shell ls $CAMERA_MODE_FILE 2> /dev/null` ]] ; then
        cur_camera_mode=$(adb shell cat $CAMERA_MODE_FILE)
    else
        echo "no $CAMERA_MODE_FILE file on board!"
	    cur_camera_mode=1 # w/o file, default case: RVC Digital
    fi
#    echo "current camera mode: $cur_camera_mode"
    get_camera_name $cur_camera_mode
    cur_camera_name=$camera_name
}

get_camera_name() {
case $1 in
    0)
        camera_name="RVC analog"
        ;;
    1)
        camera_name="RVC digital"
        ;;
    2)
        camera_name="AVM digital"
        ;;
    *)
        echo "$1 unknown mode, should be in [0:rvc analog | 1:rvc digital | 2:avm]"
        ;;
esac
}

select_camera_mode() {
    case $1 in
        none)
            echo "Removing camera selection file... default case: RVC Digital"
            adb shell "rm $CAMERA_MODE_FILE"
            return
            ;;
        rvc_a)
            CAMERA_ID=0
            ;;
        rvc_d)
            CAMERA_ID=1
            ;;
        avm)
            CAMERA_ID=2
            ;;
        *)
            echo "$1 unknown, choose between analog/digital/avm"
            ;;
    esac

    get_camera_name $CAMERA_ID
    echo "Selecting $camera_name camera..."
    adb root
    adb shell "mkdir -p $CAMERA_MODE_PATH"
    adb shell "echo $CAMERA_ID > $CAMERA_MODE_FILE"
    adb shell "chmod 755 $CAMERA_MODE_FILE"
}

# check which one of none/rvc_a/rvc_d/avm is selected
check_current_config() {

    cur_upa_front=$(adb shell getprop $CFG_ADAS_UPA_Visual_Front |sed "s/\r//")
    cur_upa_rear=$(adb shell getprop $CFG_ADAS_UPA_Visual_Rear |sed "s/\r//")
    cur_fkp=$(adb shell getprop $CFG_ADAS_FKP |sed "s/\r//")
    cur_rvc=$(adb shell getprop $CFG_ADAS_RVC |sed "s/\r//")
    cur_avm=$(adb shell getprop $CFG_ADAS_AVM |sed "s/\r//")
    cur_rcta=$(adb shell getprop $CFG_ADAS_RCTA |sed "s/\r//")
    cur_ose=$(adb shell getprop $CFG_ADAS_OSE |sed "s/\r//")
    cur_raeb=$(adb shell getprop $CFG_ADAS_RAEB |sed "s/\r//")
    cur_upa_audio=$(adb shell getprop $CFG_ADAS_UPA_Audio |sed "s/\r//")
    cur_apa=$(adb shell getprop $CFG_ADAS_APA |sed "s/\r//")
    cur_evs_camera=$(adb shell getprop $USE_EVS_CAMERA |sed "s/\r//")

    check_camera_mode

    if [[ -z "$cur_rvc" && -z "$cur_avm" ]] ; then
        cur_config="avm" # Default config is AVM
    elif [[ $cur_rvc == '0' && $cur_avm == '0' ]] ; then
        cur_config="none"
    elif [[ $cur_avm == '1' ]] ; then
        cur_config="avm"
    elif [[ $cur_rvc == '1' ]] ; then
        cur_config="rvc_d"
    fi
 
#    if [[ $cur_mode == 'real' ]] ; then
        if [[ $cur_rvc == '1' ]] ; then
#            echo "RVC selected"
            if [[ $cur_camera_mode == '0' ]] ; then
                cur_config="rvc_a"
            elif [[ $cur_camera_mode == '1' ]] ; then
                cur_config="rvc_d"
            else
                warn "Inconsistent configuration, RVC prop not matching camera selection, update with '--config' !"
            fi
        elif [[ $cur_avm == '1' && $cur_camera_mode == '2' ]] ; then
#            echo "AVM selected"
            cur_config="avm"
        else
            warn "Inconsistent configuration, update with '--config' !"
        fi
#    fi
}

select_config() {
    if [[ $config == 'none' ]] ; then
        rvc='0'
        avm='0'
    elif [[ $config == 'rvc_a' || $config == 'rvc_d' ]] ; then
        rvc='1'
        avm='0'
    elif [[ $config == 'avm' ]] ; then
        rvc='0'
        avm='1'
    fi
    adb shell setprop ${CFG_ADAS_RVC} $rvc
    adb shell setprop ${CFG_ADAS_AVM} $avm
    
    select_camera_mode $config
    reboot=true
}

display_config() {
    echo "ParkAssist settings:"
    printTable ',' \
    "CONFIG,MODE,CAMERA,UPA Front,UPA Rear,RCTA,OSE,RAEB,UPA Audio,APA\n\
    $cur_config,$cur_mode,$cur_camera_name ($cur_camera_mode),\
    $cur_upa_front,$cur_upa_rear,$cur_rcta,$cur_ose,$cur_raeb,\
    $cur_upa_audio,$cur_apa"
}

check_boot_completed

#check_current_config

if [[ -n $mode && $mode != $cur_mode ]] ; then
    echo "Switching to mode=$mode ..."
    if [[ $mode == 'real' ]] ; then
        use_evs_camera=true
    else
        use_evs_camera=false
    fi
    adb shell setprop ${USE_EVS_CAMERA} $use_evs_camera
    cur_mode=$mode
    # force configuration change even if not requested
    # it enables to align camera mode to avoid any misalignment
    if [[ -z $config ]] ; then
        config=$cur_config
    fi
fi

check_apk_on_product() {
    is_on_product=$(adb wait-for-device shell ls $TARGET_PATH 2> /dev/null)
    echo "ParkAssist ($TARGET_PATH): $is_on_product"
}

zip_align() {
    echo "== Zip align $PROJECT_NAME =="
    UNSIGNED_APK=$APK_NAME
    APK_NAME=${APK_NAME%-unsigned.*}-signed.apk
    $ZIPALIGN -f 4 $UNSIGNED_APK $APK_NAME
}

sign_apk() {
    echo "== Sign $PROJECT_NAME =="
    echo my_password | $APKSIGNER sign --ks $SIGN_TOOLS/device.keystore --ks-key-alias platform $APK_NAME
}

make_system_writable() {
    adb wait-for-device root 1>/dev/null
    IS_DISABLED=$(adb wait-for-device disable-verity | grep "verity is already disabled")
    if [[ -z $IS_DISABLED ]] ; then
        # one more disable line requires for emulator only
        adb wait-for-device shell avbctl disable-verification
        echo "Disable-verity done, let's reboot to make it effective"
        adb wait-for-device reboot
        check_boot_completed
    fi

    PRODUCT_RW=$(adb shell [ -w "/product" ] && echo "true" || echo "false")
    if ! $PRODUCT_RW ; then
       echo "Disable selinux and remount file system R/W"
       adb wait-for-device root 1>/dev/null
       adb wait-for-device shell setenforce 0
       # escape error output for emulator
       adb wait-for-device shell umount -l -t overlay /product 2> /dev/null
       adb wait-for-device remount
    fi
}

check_and_sign_apk() {
    # check whether apk is signed or not
    IS_SIGNED=$($APKSIGNER verify --print-certs $APK_NAME 2>/dev/null | grep "Signer")
    if [[ -z $IS_SIGNED ]] ; then
        echo "$APK_NAME is not signed"
        zip_align
        sign_apk
    else
        echo "$APK_NAME is well signed"
    fi
}

check_apk_on_product

if [[ -n $is_on_product ]] ; then
    echo $PROJECT_NAME" found"
    make_system_writable
    echo "Backup release app in $BACKUP_FOLDER"
    adb wait-for-device pull $TARGET_PATH $BACKUP_FOLDER
    adb wait-for-device shell rm -rf $TARGET_PATH
    if $removeonly; then
       adb wait-for-device reboot
    fi
else
    echo $PROJECT_NAME" already backup in "$BACKUP_FOLDER
fi

if $build || $install ; then
    get_build_variant
fi

if $build
then
    build_apk
fi

if $install || $demo
then
    check_and_sign_apk
    push_apk
    echo "Restart board to get $PROJECT_NAME parsed and installed by Package Manager"
    adb wait-for-device reboot
    check_boot_completed

    if [[ $BUILD_VARIANT == 'debug' ]] ; then
        echo "Start Display service in debug"
        adb wait-for-device root 2> /dev/null
        adb wait-for-device shell am start-foreground-service com.renault.parkassist/.service.DisplayService 2> /dev/null
    fi
    echo "== $PROJECT_NAME $BUILD_VARIANT version installed =="
    adb wait-for-device shell dumpsys package $PACKAGE_NAME | grep -E "versionName|codePath"
fi
