#!/bin/bash

echo > memory.txt

for i in {1..200}
do
    ./rear-gear-engaged.sh
    sleep 1

    MEM_USAGE_KB=$(adb shell "su 0 procrank 2>/dev/null" | grep -r parkassist | tr -s ' '  | cut -d " " -f 6 | cut -d "K" -f1)

    echo "Mem usage for rear gear n.$i: $MEM_USAGE_KB "
    echo "$MEM_USAGE_KB" >> memory.txt

    ./rear-gear-engaged-not-engaged.sh
    sleep 1
done
