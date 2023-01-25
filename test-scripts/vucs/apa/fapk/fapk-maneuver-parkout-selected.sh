#!/bin/bash

# Parkout selected, parallel not selected, perpendicular not available
adb shell 'echo -e -n "\x83\x50\x10\xE1\x9c\x31\x00" > /dev/vucs_simulator_rx'