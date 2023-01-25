#!/bin/bash

SCRIPT=$(readlink -f $0)
SCRIPT_DIR=`dirname $SCRIPT`/..
SENSOR=${SCRIPT_DIR}/upa/upa-sensor.sh

${SCRIPT_DIR}/rear-gear-engaged.sh
${SCRIPT_DIR}/upa/upa-display-all.sh
${SCRIPT_DIR}/upa/fkp-display-all.sh

### Scan fkp left
sleep 1
$SENSOR left-front 0 0
$SENSOR left-front-center 0 0
$SENSOR left-rear-center 0 0
$SENSOR left-rear 0 0

### Scan fkp right
sleep 1
$SENSOR right-front 0 0
$SENSOR right-front-center 0 0
$SENSOR right-rear-center 0 0
$SENSOR right-rear 0 0


### Drive forward
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

### Drive backward
sleep 1
$SENSOR front-center 3 0
$SENSOR front-left 3 1
$SENSOR rear-center 1 0

sleep 1
$SENSOR front-center 2 0
$SENSOR front-left 2 1
$SENSOR rear-center 2 0
$SENSOR rear-right 1 1
$SENSOR right-rear-center 1 1
$SENSOR rear-left 1 0
$SENSOR left-rear 1 0

sleep 1
$SENSOR front-center 1 0
$SENSOR rear-center 3 0
$SENSOR rear-right 2 1
$SENSOR right-rear-center 2 1
$SENSOR rear-left 3 0
$SENSOR left-rear 2 0

sleep 1
$SENSOR front-center 0 0
$SENSOR front-left 0 0
$SENSOR front-right 0 1
$SENSOR rear-center 4 0
$SENSOR right-rear-center 3 1
$SENSOR rear-right 1 1
$SENSOR rear-left 3 0
$SENSOR left-rear-center 2 0
$SENSOR left-rear 3 0

sleep 1
$SENSOR rear-right 2 1
$SENSOR rear-left 5 0
$SENSOR left-rear 3 0
$SENSOR left-rear-center 3 0
$SENSOR rear-center 5 0

### Exit
sleep 1
$SENSOR front-center 0 0
$SENSOR front-left 0 0
$SENSOR front-right 0 0
$SENSOR rear-center 0 0
$SENSOR rear-right 0 0
$SENSOR rear-left 0 0
$SENSOR left-rear 0 0
$SENSOR left-rear-center 0 0
$SENSOR right-rear-center 0 1

sleep 1
${SCRIPT_DIR}/upa/upa-display-off.sh
${SCRIPT_DIR}/upa/fkp-display-off.sh










