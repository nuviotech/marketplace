#!/bin/bash
sudo su
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"  # This loads nvm
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"  # This loads nvm bash_completion


systemctl restart mysql
systemctl status mysql

MARKETPLACE_PATH=/app/marketplace-main
echo marketplace path : $MARKETPLACE_PATH

# kill all process

pid=$(lsof -t -i :8080)

if [ -n "$pid" ]; then
  echo "Process with port 8080 is running... please kill the process"
  exit 1
fi
 
pid=$(lsof -t -i :8084)
if [ -n "$pid" ]; then
  echo "Process with port 8084 is running... please kill the process"
  exit 1
fi
  
pid=$(lsof -t -i :1337)
if [ -n "$pid" ]; then
  echo "Process with port 1337 is running... please kill the process"
  exit 1
fi
  
pid=$(lsof -t -i :3003)
if [ -n "$pid" ]; then
  echo "Process with port 3003 is running... please kill the process"
  exit 1
fi
 
#start mysql
sudo systemctl restart mysql

#make log dir
if [ ! -d $MARKETPLACE_PATH/log/marketplace-startupLogs ]; then
  mkdir $MARKETPLACE_PATH/log/marketplace-startupLogs
  echo "marketplace-startupLogs directory created."
fi

if [ ! -d $MARKETPLACE_PATH/log/marketplace-adminLogs ]; then
  mkdir $MARKETPLACE_PATH/log/marketplace-adminLogs
  echo "marketplace-adminLogs directory created."
fi

if [ ! -d $MARKETPLACE_PATH/log/marketplace-apiLogs ]; then
  mkdir $MARKETPLACE_PATH/log/marketplace-apiLogs
  echo "marketplace-apiLogs directory created."
fi

if [ ! -d $MARKETPLACE_PATH/log/marketplace-webLogs ]; then
  mkdir $MARKETPLACE_PATH/log/marketplace-webLogs
  echo "marketplace-webLogs directory created."
fi

if [ ! -d $MARKETPLACE_PATH/log/marketplace-cmsLogs ]; then
  mkdir $MARKETPLACE_PATH/log/marketplace-cmsLogs
  echo "marketplace-webLogs directory created."
fi

#set application.properties values run startup.jar
# Check if at least one argument is provided
if [ $# -eq 0 ]; then
    echo "Standalone marketplace."
	echo "started startup"
	nohup java -jar $MARKETPLACE_PATH/startup-0.0.1-SNAPSHOT.jar  > $MARKETPLACE_PATH/log/marketplace-startupLogs/nohup.out 2>&1 &
else
    echo "Cloud marketplace:" $1
	echo "started startup"
	nohup java -jar $MARKETPLACE_PATH/startup-0.0.1-SNAPSHOT.jar $1 > $MARKETPLACE_PATH/log/marketplace-startupLogs/nohup.out 2>&1 &
	sleep 10
	nohup java -jar $MARKETPLACE_PATH/startup-0.0.1-SNAPSHOT.jar $1 > $MARKETPLACE_PATH/log/marketplace-startupLogs/nohup.out 2>&1 &
fi

#start marketplace admin
cd $MARKETPLACE_PATH/marketplace-admin
nohup java -jar marketplace_Admin.jar > $MARKETPLACE_PATH/log/marketplace-adminLogs/nohup.out 2>&1 &
echo "started marketPlace-admin"

#start marketplace api
cd $MARKETPLACE_PATH/marketplace-api
nohup java -jar com.marketplace.api-0.0.1-SNAPSHOT.jar > $MARKETPLACE_PATH/log/marketplace-apiLogs/nohup.out 2>&1 &
echo "started marketPlace-api" 

#start marketplace web
cd $MARKETPLACE_PATH/marketplace-web
#install web node modules
nvm install v16.17.1
nvm use v16.17.1
node -v
yarn install
yarn build
nohup npm start > $MARKETPLACE_PATH/log/marketplace-webLogs/nohup.out 2>&1 &
echo "started marketPlace-web"


#start marketplace cms
cd $MARKETPLACE_PATH/marketplace-cms
nvm install v14.15.0
nvm use v14.15.0
node -v
yarn install
yarn build
nohup yarn start > $MARKETPLACE_PATH/log/marketplace-cmsLogs/nohup.out 2>&1 &
