#!/bin/bash

adb shell 'echo -e -n "\x83\x50\x10\xE1\x0c\x80" > /dev/vucs_simulator_rx'
adb shell 'echo -e -n "\x83\x50\x10\xE1\x0d\x80" > /dev/vucs_simulator_rx'
adb shell 'echo -e -n "\x83\x50\x10\xE1\x0e\x80" > /dev/vucs_simulator_rx'
adb shell 'echo -e -n "\x83\x50\x10\xE1\x0f\x80" > /dev/vucs_simulator_rx'
adb shell 'echo -e -n "\x83\x50\x10\xE1\x10\x80" > /dev/vucs_simulator_rx'
adb shell 'echo -e -n "\x83\x50\x10\xE1\x11\x80" > /dev/vucs_simulator_rx'
adb shell 'echo -e -n "\x83\x50\x10\xE1\x12\x80" > /dev/vucs_simulator_rx'
adb shell 'echo -e -n "\x83\x50\x10\xE1\x13\x80" > /dev/vucs_simulator_rx'