#!/bin/bash

# Check if the MARKETPLACE_PATH environment variable is set
if [ -z "$MARKETPLACE_PATH" ]; then
    # If not set, use "/marketplace" as the default value
    MARKETPLACE_PATH="/marketplace"
fi

# Now you can use the MARKETPLACE_PATH variable, which is guaranteed to have a value.
echo "MARKETPLACE_PATH is set to: $MARKETPLACE_PATH"

$MARKETPLACE_PATH/bin/find_and_kill_process.sh 1337
nohup yarn start > $MARKETPLACE_PATH/log/marketplace-cmsLogs/nohup.out 2>&1 &

