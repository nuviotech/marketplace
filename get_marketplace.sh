sudo rm -rf main.zip
sudo rm -rm marketplace-main

wget https://github.com/nuviotech/marketplace/archive/refs/heads/main.zip

sudo unzip main.zip
if [ $# -eq 0 ]; then
	echo "argument not present.."
else
	sh marketplace-main/startup.sh $1
if
