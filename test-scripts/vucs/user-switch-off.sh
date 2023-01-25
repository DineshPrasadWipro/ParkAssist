#!/bin/bash

# Simulate user switch off:
adb shell 'echo -e -n "\x83\x50\x10\xE1\xCD\x00" > /dev/vucs_simulator_rx'
