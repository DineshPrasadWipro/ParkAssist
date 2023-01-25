#!/bin/bash

# Emulate VEHICLE_STATE +> STARTING_IN_PROGESS
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\x60" > /dev/vucs_simulator_rx'
