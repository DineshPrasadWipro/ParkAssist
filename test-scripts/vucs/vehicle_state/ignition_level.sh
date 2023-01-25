#!/bin/bash

# Emulate VEHICLE_STATE +> IGNITION_LEVEL
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\x50" > /dev/vucs_simulator_rx'
