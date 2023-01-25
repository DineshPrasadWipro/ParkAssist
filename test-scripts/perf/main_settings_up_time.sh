#!/bin/bash

echo "Bash version ${BASH_VERSION}"

RUN_NUMBER=20
TOTAL_TIME=0

start() {
    adb shell log "ElapsedTime Sending Intent to start SettingsActivity"
    adb shell am start -S -W com.renault.parkassist/.ui.settings.SettingsActivity
}

for ((i = 1; i <= RUN_NUMBER; i++)); do
    TMP_TIME=$(start | grep TotalTime | cut -d ":" -f2 | tr -d '[:space:]')
    echo "Run ${i}: ${TMP_TIME}ms"
    TOTAL_TIME=$((TOTAL_TIME + TMP_TIME))
done

echo
echo "Mean Up Time:"
echo "-------------"
echo "$(echo "$TOTAL_TIME/$RUN_NUMBER" | bc)ms"
