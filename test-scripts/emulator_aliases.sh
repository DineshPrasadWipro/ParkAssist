case $(basename $SHELL) in
    bash)   echo "have a bash, emulator aliases set"
            alias aivi_port='emulator -avd aivi2_portrait_avd_API_29 -gpu swiftshader_indirect -no-audio -no-boot-anim -writable-system -selinux permissive'
            alias aivi_land='emulator -avd aivi2_landscape_avd_API_29 -gpu swiftshader_indirect -no-audio -no-boot-anim -writable-system -selinux permissive'
            ;;
    fish)   echo "caught a fish, emulator aliases set"
            alias aivi_port 'emulator -avd aivi2_portrait_avd_API_29 -gpu swiftshader_indirect -no-audio -no-boot-anim -writable-system -selinux permissive'
            alias aivi_land 'emulator -avd aivi2_landscape_avd_API_29 -gpu swiftshader_indirect -no-audio -no-boot-anim -writable-system -selinux permissive'
            ;;
    * )     echo "Unknown shell: $SHELL, please add it to the script"
            exit 1
esac