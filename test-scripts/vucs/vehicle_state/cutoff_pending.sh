#!/bin/bash

# Emulate VEHICLE_STATE +> CUTOFF_PENDING
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\x20" > /dev/vucs_simulator_rx'
