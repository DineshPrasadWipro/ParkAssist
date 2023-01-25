#!/bin/bash

# Emulate VEHICLE_STATE +> POWER_TRAIN_RUNNING
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\x70" > /dev/vucs_simulator_rx'
