#!/bin/bash
cd /mnt/newhd/eTube/Autocomplete_dev
java -cp .:mongodb-driver-3.4.2.jar:mongodb-driver-core-3.4.2.jar:org.json.jar:json-simple-1.1.1.jar:bson-3.4.2.jar Insert_eTube_dev
