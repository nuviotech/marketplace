#!/bin/bash

# Check if the MARKETPLACE_PATH environment variable is set
if [ -z "$MARKETPLACE_PATH" ]; then
    # If not set, use "/marketplace" as the default value
    MARKETPLACE_PATH="/marketplace"
fi

# Now you can use the MARKETPLACE_PATH variable, which is guaranteed to have a value.
echo "MARKETPLACE_PATH is set to: $MARKETPLACE_PATH"

# Check if the JAVA_HOME environment variable is set
if [ -z "$JAVA_HOME" ]; then
    # If not set, set it to the default Java home directory
    JAVA_HOME="$MARKETPLACE_PATH/jdk-11.0.16.1"
fi

# Now you can use the JAVA_HOME variable, which is either set or contains the default value.
echo "JAVA_HOME is set to: $JAVA_HOME"


$MARKETPLACE_PATH/bin/find_and_kill_process.sh 8084
nohup  $JAVA_HOME/bin/java -jar $MARKETPLACE_PATH/com.marketplace.api-0.0.1-SNAPSHOT.jar  > $MARKETPLACE_PATH/log/marketplace-apiLogs/nohup.out 2>&1 &
echo "Restarted marketPlace-api"  
