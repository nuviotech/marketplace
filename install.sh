# Install node js
echo "Installing nodejs..."
curl -sL https://deb.nodesource.com/setup_16.x | sudo -E bash -
sudo apt install nodejs

echo "Install nvm..."
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.5/install.sh | bash
echo "Install yarn..."
sudo curl --compressed -o- -L https://yarnpkg.com/install.sh | bash

# Check the exit status and proceed accordingly
if [ $? -eq 0 ]; then
	node --version
    echo "Node js install successfully."
else
    echo "node js installation encountered an error."
    # Handle the error or exit the script
    exit 1
fi


# Install java
echo "Installing java..."
sudo apt-get install openjdk-11-jdk
# Check the exit status and proceed accordingly
if [ $? -eq 0 ]; then
	java --version
    echo "Java 11 install successfully."
else
    echo "Java 11 installation encountered an error."
    # Handle the error or exit the script
    exit 1
fi


# Install nginx
echo "Installing nginx..."
sudo apt update
sudo apt install nginx

# Check the exit status and proceed accordingly
if [ $? -eq 0 ]; then
	sudo ufw app list
	sudo ufw allow 'Nginx HTTP'
	systemctl status nginx
    echo "Nginx install successfully."
else
    echo "Nginx encountered an error."
    # Handle the error or exit the script
    exit 1
fi


#Installing Mysql
#echo "Installing Mysql..."
#sudo apt update
#sudo apt install wget -y
#wget https://dev.mysql.com/get/mysql-apt-config_0.8.12-1_all.deb
#echo "Let’s search for MySQL 5,7"
#sudo dpkg -i mysql-apt-config_0.8.12-1_all.deb

#sudo apt-get update
#sudo apt-cache policy mysql-server

#sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 467B942D3A79BD29
#sudo apt update

#sudo apt install -f mysql-client=5.7* mysql-community-server=5.7* mysql-server=5.7*

#sudo mysql_secure_installation

# Check the exit status and proceed accordingly
#if [ $? -eq 0 ]; then
	
#	mysql -u root -p 
#	sudo systemctl restart mysql
 #   echo "Mysql install successfully."
#else
#    echo "Mysql encountered an error."
    # Handle the error or exit the script
#    exit 1
#fi


