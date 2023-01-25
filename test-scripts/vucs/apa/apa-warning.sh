#!/bin/bash

if [ -z $1 ]
then
  echo "Usage: apa-warning.sh <DECIMAL_MESSAGE_ID>"
  echo "    with 0 <= DECIMAL_MESSAGE_ID <= 40"
  exit 1
fi

DEC_MSG=$(( $1 * 4 ))
HEX_MSG=$(printf '%02x' $DEC_MSG | awk '{ print toupper($0) }')

COMMAND="adb shell 'echo -e -n \"\x83\x50\x10\xE1\x90\x$HEX_MSG\" > /dev/vucs_simulator_rx'"

eval "$COMMAND"