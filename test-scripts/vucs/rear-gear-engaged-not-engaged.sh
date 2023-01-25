#!/bin/bash

# Emulate GEAR_REVERSE_ON +> NOT_ENGAGED
adb shell 'echo -e -n "\x83\x50\x10\xE0\x0C\x40" > /dev/vucs_simulator_rx'
# Emulate VEHICLE_STATE +> ENGINE_STOP
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\xf0" > /dev/vucs_simulator_rx'
