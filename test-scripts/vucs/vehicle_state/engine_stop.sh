#!/bin/bash

# Emulate VEHICLE_STATE +> ENGINE_STOP
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\x90" > /dev/vucs_simulator_rx'
