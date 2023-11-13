MARKETPLACE_PATH=$PWD
echo marketplace path : $MARKETPLACE_PATH

sh shutdown.sh

#start marketplace admin
cd $MARKETPLACE_PATH/marketplace_admin
nohup java -jar marketplace_Admin-0.0.1-SNAPSHOT.jar > $MARKETPLACE_PATH/log/marketplace-adminLogs/nohup.out 2>&1 &
echo "started marketPlace-admin"

#start marketplace api
cd $MARKETPLACE_PATH/marketplace_api
nohup java -jar com.marketplace.api-0.0.1-SNAPSHOT.jar > $MARKETPLACE_PATH/log/marketplace-apiLogs/nohup.out 2>&1 &
echo "started marketPlace-api" 

#start marketplace web
cd $MARKETPLACE_PATH/marketplace-web
nohup npm start > $MARKETPLACE_PATH/log/marketplace-webLogs/nohup.out 2>&1 &
echo "started marketPlace-web"

#start marketplace cms
cd $MARKETPLACE_PATH/marketplace-cms
nohup yarn start > $MARKETPLACE_PATH/log/marketplace-cmsLogs/nohup.out 2>&1 &
