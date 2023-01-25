#!/bin/bash

# Emulate PERF_VEHICLE_SPEED +> 0.0 km/h
adb shell 'echo -e -n "\x83\x50\x10\xE0\x09\x00\x00" > /dev/vucs_simulator_rx'
# Emulate VEHICLE_STATE +> POWER_TRAIN_RUNNING
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\x70" > /dev/vucs_simulator_rx'
# Emulate GEAR_REVERSE_ON +> ENGAGED
adb shell 'echo -e -n "\x83\x50\x10\xE0\x0C\x80" > /dev/vucs_simulator_rx'
