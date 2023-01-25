#!/bin/bash

SCRIPT=$(readlink -f $0)
SCRIPT_DIR=`dirname $SCRIPT`/..
SENSOR=${SCRIPT_DIR}/upa/upa-sensor.sh

${SCRIPT_DIR}/upa/upa-display-front.sh

### Drive frontward
sleep 1
$SENSOR front-center 1 0
sleep 1
$SENSOR front-center 2 0
$SENSOR front-left 2 1
sleep 1
$SENSOR front-center 3 0
$SENSOR front-left 3 1
sleep 1
$SENSOR front-center 4 0
$SENSOR front-right 2 0
sleep 1
$SENSOR front-center 5 0
$SENSOR front-right 3 0

### Exit
sleep 1
$SENSOR front-center 0 0
$SENSOR front-left 0 0
$SENSOR front-right 0 0
${SCRIPT_DIR}/upa/upa-display-off.sh










