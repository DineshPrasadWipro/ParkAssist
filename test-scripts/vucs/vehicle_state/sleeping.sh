#!/bin/bash

# Emulate VEHICLE_STATE +> SLEEPING
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\x00" > /dev/vucs_simulator_rx'
