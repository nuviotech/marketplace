MARKETPLACE_PATH=$PWD
echo marketplace path : $MARKETPLACE_PATH

#kill marketplace admin
$MARKETPLACE_PATH/bin/find_and_kill_process.sh 8080

#kill marketplace api
$MARKETPLACE_PATH/bin/find_and_kill_process.sh 8084

#kill marketplace cms
$MARKETPLACE_PATH/bin/find_and_kill_process.sh 1337

#kill marketplace web
$MARKETPLACE_PATH/bin/find_and_kill_process.sh 3003
