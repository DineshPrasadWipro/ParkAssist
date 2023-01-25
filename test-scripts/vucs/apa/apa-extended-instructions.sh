#!/bin/bash

if [ -z $1 ]
then
  echo "Usage: $0 <VALUE>"
  echo "    with value { 08, 10, 18, 28, 38, 48, 58, 68, 78, 88 } "
  exit 1
fi

COMMAND="adb shell 'echo -e -n \"\x83\x50\x10\xE1\x8E\x$1\" > /dev/vucs_simulator_rx'"
eval "$COMMAND"