case $(basename $SHELL) in
    bash)   echo "have a bash, broadcast aliases set"
            alias avm_bd='adb shell "am broadcast -n com.renault.parkassist/.service.AvmBroadcastReceiver -a com.alliance.intent.action.ACTION_START_AVM"'
            alias easypark_bd='adb shell "am broadcast -n com.renault.parkassist/.service.EasyParkBroadcastReceiver -a com.alliance.intent.action.ACTION_START_EASY_PARK"'
            ;;
    fish)   echo "caught a fish, broadcast aliases set"
            alias avm_bd 'adb shell "am broadcast -n com.renault.parkassist/.service.AvmBroadcastReceiver -a com.alliance.intent.action.ACTION_START_AVM"'
            alias easypark_bd 'adb shell "am broadcast -n com.renault.parkassist/.service.EasyParkBroadcastReceiver -a com.alliance.intent.action.ACTION_START_EASY_PARK"'
            ;;
    * )     echo "Unknown shell: $SHELL, please add it to the script"
            exit 1
esac
