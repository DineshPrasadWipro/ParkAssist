#!/bin/bash

# Simulate View Mask requested:
adb shell 'echo -e -n "\x83\x50\x10\xE1\x47\x80" > /dev/vucs_simulator_rx'
