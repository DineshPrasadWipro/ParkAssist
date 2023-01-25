#!/bin/bash

# Simulate engine on:
adb shell 'echo -e -n "\x83\x50\x10\xE1\x43\x08" > /dev/vucs_simulator_rx'
adb shell 'echo -e -n "\x83\x50\x10\xE0\x03\x50" > /dev/vucs_simulator_rx'
