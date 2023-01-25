#!/bin/bash

# Emulate VEHICLE_STATE +> AUTO_START
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\x80" > /dev/vucs_simulator_rx'
