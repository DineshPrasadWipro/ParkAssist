#!/bin/bash

if [ -z $1 ]; then
  echo "Usage: upa-obstacle-front-center.sh <SENSOR_POSITION> <INTEGER_LEVEL_SENSOR> <BOOLEAN_HATCHED_BIT>"
  echo "    with 0 <= INTEGER_LEVEL_SENSOR <= 5 if UPA else 0 <= INTEGER_LEVEL_SENSOR <= 3 for FKP"
  echo "    with SENSOR_POSITION in (front-center,front-right,front-left,rear-center,rear-right,rear-left,left-front,left-front-center,left-rear-center,left-rear,right-rear-center,right-front-center,right-front)"
  exit 1
fi

HEXA_SENSOR_POS=""
FKP=false
case $1 in
"front-center")
  HEXA_SENSOR_POS="3B"
  ;;
"front-left")
  HEXA_SENSOR_POS="34"
  ;;
"front-right")
  HEXA_SENSOR_POS="33"
  ;;
"rear-center")
  HEXA_SENSOR_POS="3A"
  ;;
"rear-left")
  HEXA_SENSOR_POS="37"
  ;;
"rear-right")
  HEXA_SENSOR_POS="36"
  ;;
"left-front")
  HEXA_SENSOR_POS="11"
  FKP=true
  ;;
"left-front-center")
  HEXA_SENSOR_POS="10"
  FKP=true
  ;;
"left-rear-center")
  HEXA_SENSOR_POS="0F"
  FKP=true
  ;;
"left-rear")
  HEXA_SENSOR_POS="14"
  FKP=true
  ;;
"right-rear")
  HEXA_SENSOR_POS="13"
  FKP=true
  ;;
"right-rear-center")
  HEXA_SENSOR_POS="12"
  FKP=true
  ;;
"right-front-center")
  HEXA_SENSOR_POS="15"
  FKP=true
  ;;
"right-front")
  HEXA_SENSOR_POS="0E"
  FKP=true
  ;;
esac

HEXA=$2

# shellcheck disable=SC2089
#COMMAND="adb shell 'echo -e -n \"\x83\x50\x10\xE1\x${HEXA_SENSOR_POS}\x${HEXA}0\" > /dev/vucs_simulator_rx'"
COMMAND="adb shell 'echo -e -n \"\x83\x50\x11\x01\x21\x02\x${HEXA_SENSOR_POS}\x0${HEXA}\" > /dev/vucs_simulator_rx'"
eval "$COMMAND"