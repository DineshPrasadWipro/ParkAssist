#!/bin/bash

adb shell 'echo -e -n "\x83\x50\x10\xE1\x44\x10" > /dev/vucs_simulator_rx'
adb shell 'echo -e -n "\x83\x50\x10\xE1\x43\x10" > /dev/vucs_simulator_rx'
