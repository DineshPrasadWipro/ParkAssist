#!/bin/bash

#  adb shell 'echo -e -n "\x83\x50\x11\x03\x23\x02\x01\xaa\x02\x02\xyy\x02\x03\xzz"  > /dev/vucs_simulator_rx'
#
#  aa ==  CAN signal CTA_AlertStatus value
#     . 0 -> No alert / Not available
#     . 1 -> Rigth alert
#     . 2 -> Left alert
#     . 3 -> Right and Left alert
#​
#  yy == CAN signal CTA_CollisionRiskAlert value
#     . 0 -> Low collision risk / Not available
#     . 1 -> High collision risk
#​
#  zz == CAN signal CTA_StatusDisplay_v2 value
#    . 0 -> No display
#    . 1 -> Not operationnal
#    . 2 -> Operationnal
#    . 3 -> Temporary failure
#    . 4 -> Permanent failure

function help() {
    echo "Usage: $0 -a <CTA_AlertStatus> -c <CTA_CollisionRiskAlert> -d <CTA_StatusDisplay_v2>"
    echo "    See this script header for more details"
    exit
}

while getopts "a:c:d:h" option; do
    case $option in
    a)
        ALERT_STATUS=$OPTARG
        ;;
    c)
        COLLISION_RISK_ALERT=$OPTARG
        ;;
    d)
        STATUS_DISPLAY=$OPTARG
        ;;
    h)
        help
        ;;
    *)
        help
        ;;
    esac
done

if [[ -z $ALERT_STATUS || $ALERT_STATUS -gt 3 ]]; then
    echo "Invalid CTA_AlertStatus, should be 0, 1, 2 or 3"
    help
fi
ALERT_STATUS=$(printf '%02x' "$ALERT_STATUS" | awk '{ print toupper($0) }')

if [[ -z $COLLISION_RISK_ALERT || $COLLISION_RISK_ALERT -gt 1 ]]; then
    echo "Invalid CTA_CollisionRiskAlert, should be 0 or 1"
    help
fi
COLLISION_RISK_ALERT=$(printf '%02x' "$COLLISION_RISK_ALERT" | awk '{ print toupper($0) }')

if [[ -z $STATUS_DISPLAY || $STATUS_DISPLAY -gt 4 ]]; then
    echo "Invalid CTA_StatusDisplay_v2, should be 0, 1, 2, 3 or 4"
    help
fi
STATUS_DISPLAY=$(printf '%02x' "$STATUS_DISPLAY" | awk '{ print toupper($0) }')

COMMAND="adb shell 'echo -e -n \"\x83\x50\x11\x03\x23\x02\x01\x$ALERT_STATUS\x02\x02\x$COLLISION_RISK_ALERT\x02\x03\x$STATUS_DISPLAY\" > /dev/vucs_simulator_rx'"

eval "$COMMAND"
