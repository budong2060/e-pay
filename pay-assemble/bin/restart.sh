#!/bin/bash

PWD=$(dirname $(readlink -f "$0"))

cd ${PWD}

/bin/bash ${PWD}/stop.sh
/bin/bash ${PWD}/start.sh
