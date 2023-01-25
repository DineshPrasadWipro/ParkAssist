#!/bin/bash

# Emulate VEHICLE_STATE +> AUTO_ACC
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\x30" > /dev/vucs_simulator_rx'
