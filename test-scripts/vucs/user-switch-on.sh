#!/bin/bash

# Simulate user switch on:
adb shell 'echo -e -n "\x83\x50\x10\xE1\xCD\x40" > /dev/vucs_simulator_rx'
