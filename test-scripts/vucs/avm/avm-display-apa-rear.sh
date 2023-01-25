#!/bin/bash

adb shell 'echo -e -n "\x83\x50\x10\xE1\x44\x60" > /dev/vucs_simulator_rx'
adb shell 'echo -e -n "\x83\x50\x10\xE1\x43\x60" > /dev/vucs_simulator_rx'
