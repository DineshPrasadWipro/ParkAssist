case $(basename $SHELL) in
    bash)   echo "have a bash, intent aliases set"
            alias start_service='adb shell "am start-foreground-service com.renault.parkassist/.service.DisplayService"'
            alias avm='adb shell "am start -n \"com.renault.parkassist/.ui.MainActivity\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"'
            alias hfp='adb shell "am start -n \"com.renault.parkassist/.ui.apa.ApaActivity\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"'
            alias settings='adb shell "am start -n \"com.renault.parkassist/.ui.settings.SettingsActivity\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"'
            ;;
    fish)   echo "caught a fish, intent aliases set"
            alias start_service 'adb shell "am start-foreground-service com.renault.parkassist/.service.DisplayService"'
            alias avm 'adb shell "am start -n \"com.renault.parkassist/.ui.MainActivity\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"'
            alias hfp 'adb shell "am start -n \"com.renault.parkassist/.ui.apa.ApaActivity\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"'
            alias settings 'adb shell "am start -n \"com.renault.parkassist/.ui.settings.SettingsActivity\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"'
            ;;
    * )     echo "Unknown shell: $SHELL, please add it to the script"
            exit 1
esac