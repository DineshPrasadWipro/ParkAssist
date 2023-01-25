#!/bin/bash

adb shell "su 0 procrank 2>/dev/null" 2>/dev/null | grep -i parkassist | tr -s ' ' | awk ' \
BEGIN {
    print "ParkAssist Memory usage"
    FS=" "
}
{
    print "PID " $1 ": " $5
    gsub(/K/,"")
    mem=$5+mem
}
END {
    print "          ---------"
    print "Total:    " mem "K"
}'
