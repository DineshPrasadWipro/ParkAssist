adb shell "echo -e -n '\x83\x50\x11\x01\x07\x02\x3F\x01' > /dev/vucs_simulator_rx"
sleep 5 # long-press
adb shell "echo -e -n '\x83\x50\x11\x01\x07\x02\x3F\x00' > /dev/vucs_simulator_rx"
