./app/marketplace-main/install.sh >> /app/install_startup.log 
if [ $# -eq 0 ]; then
	./app/marketplace-main/startup.sh  >> /app/install_startup.log 
else
	./app/marketplace-main/startup.sh $1 >> /app/install_startup.log
fi