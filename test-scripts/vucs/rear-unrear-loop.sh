#!/bin/bash

for i in {1..100}
do
    echo "Engage rear gear n.$i"
    ./rear-gear-engaged.sh
    sleep 1
    echo "Remove rear gear n.$i"
    ./rear-gear-engaged-not-engaged.sh
    sleep 1
done
