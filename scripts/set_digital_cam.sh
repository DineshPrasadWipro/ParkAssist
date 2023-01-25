#!/bin/bash

adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/rvcpresent:RVC_GEN2

adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/distortioncorrectionratio:100

adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/lensdistortioncoeff3:0.001
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/lensdistortioncoeff5:-2e-07

adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/steeringwheeltofrontwhell_f0:220
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/steeringwheeltofrontwheel_f0:220
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/steeringwheeltofrontwheel_f1:62
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/steeringwheeltofrontwheel_f2:-0.003
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/steeringwheeltofrontwheel_f3:2.9e-05
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/steeringwheeltofrontwheel_f4:-1.6e-08

adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/lensdistortioncoeff0:-1.6
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/lensdistortioncoeff1:16
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/lensdistortioncoeff2:-2.17e-2
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/lensdistortioncoeff3:1.12e-3
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/lensdistortioncoeff4:-3.88e-6
adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/lensdistortioncoeff5:-3.8e-8

adb shell cmd DiagAdb Configuration set rvc_config/rvc_feature/fov_rvc_h:130
