#!/bin/bash

SCRIPT=$(realpath -s $0)
SCRIPT_DIR=`dirname $SCRIPT`/..
AVM_SCRIPT_DIR=${SCRIPT_DIR}/avm
FAPK_SCRIPT_DIR=${SCRIPT_DIR}/apa/fapk

echo "Display Sonar Popup"
$AVM_SCRIPT_DIR/avm-display-popup.sh
sleep 6

echo "Display FAPK scanning"

$AVM_SCRIPT_DIR/avm-display-apa-front.sh
$FAPK_SCRIPT_DIR/fapk-display-scanning.sh


# OFF
sleep 10
$FAPK_SCRIPT_DIR/fapk-display-none.sh
$AVM_SCRIPT_DIR/avm-display-apa-front.sh









