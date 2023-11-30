# Marketplace

Our "Marketplace Software" is the #1 API based Interconnectable marketplace software , we overcome the hectic practices made by big marketplaces,and guarantee never before experience for owners to start their own marketplace under their own brand name and become pioneer in Industry, with an option to sell other sellers and products readily available and onboarded..

## Install Nodejs

#### STEP 1 :
Enable the NodeSource repository by running the following curl command as a user with sudo privileges :
```bash
curl -sL https://deb.nodesource.com/setup_16.x | sudo -E bash -
```
The command will add the NodeSource signing key to your system, create an apt sources repository file, install all necessary packages and refresh the apt cache.
#### STEP 2 :
Once the NodeSource repository is enabled, install Node.js and npm by typing
```bash
sudo apt install nodejs
```
The nodejs package contains both the node and npm binaries.

#### STEP 3 :
Verify that the Node.js and npm were successfully installed by printing their versions:

```bash
node --version
#output : v16.17.1
npm --version
#output : 8.15.0
```

Install nvm by running the following command :
```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.5/install.sh | bash
```
Install yarn by running the following command :
````bash
sudo curl --compressed -o- -L https://yarnpkg.com/install.sh | bash
````

## Install Java
Install java 11 by running the following command :
```bash
sudo apt-get install openjdk-11-jdk
```
Verify that the java successfully installed by printing their versions:
```bash
java --version
# output : v11.17.1
```

## Installing Mysql
#### STEP 1 :
Update the system Packages:
```bash
Add mysql apt 
sudo apt update
```
We need to Install wget using below command:
```bash
sudo apt install wget -y
```
Download the repository using the below command:
```bash
wget https://dev.mysql.com/get/mysql-apt-config_0.8.12-1_all.deb
```
Once downloaded install repository using below command:
```bash
sudo dpkg -i mysql-apt-config_0.8.12-1_all.deb
```

###### IN PROMPT
- a)In the prompt, choose Ubuntu Bionic and click Ok
- b)The next prompt shows MySQL 8.0 chosen by default. Choose the first option and click OK
- c)In the next prompt, select MySQL 5.7 server and click OK.
- d)The next prompt selects MySQL5.7 by default. Choose the last option Ok and click OK

STEP 2 : 
Let’s search for MySQL 5,7 using below command:
sudo apt-get update
sudo apt-cache policy mysql-server

#### STEP 3 :
Before Installing MySQL 5.7 you need to run this command:
```bash
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 467B942D3A79BD29
sudo apt update
```

we are going to install MySQL 5.7 client, MySQL 5.7 server with the below command:
```bash
sudo apt install -f mysql-client=5.7* mysql-community-server=5.7* mysql-server=5.7*
```
Enter and re-enter root password.
#### STEP 4 : 
Run the following commands
```bash
sudo mysql_secure_installation
```
Provide the root password set above and you will have output like this:
```bash
ubuntu@ip-172-31-44-141:~$ sudo mysql_secure_installation

Securing the MySQL server deployment.

Enter password for user root:

VALIDATE PASSWORD PLUGIN can be used to test passwords
and improve security. It checks the strength of password
and allows the users to set only those passwords which are
secure enough. Would you like to setup VALIDATE PASSWORD plugin?

Press y|Y for Yes, any other key for No: y

There are three levels of password validation policy:

LOW    Length >= 8
MEDIUM Length >= 8, numeric, mixed case, and special characters
STRONG Length >= 8, numeric, mixed case, special characters and dictionary file

Please enter 0 = LOW, 1 = MEDIUM and 2 = STRONG: 1
Using existing password for root.

Estimated strength of the password: 25
Change the password for root ? ((Press y|Y for Yes, any other key for No) : no

 ... skipping.
By default, a MySQL installation has an anonymous user,
allowing anyone to log into MySQL without having to have
a user account created for them. This is intended only for
testing, and to make the installation go a bit smoother.
You should remove them before moving into a production
environment.

Remove anonymous users? (Press y|Y for Yes, any other key for No) : y
Success.


Normally, root should only be allowed to connect from
'localhost'. This ensures that someone cannot guess at
the root password from the network.

Disallow root login remotely? (Press y|Y for Yes, any other key for No) : y
Success.

By default, MySQL comes with a database named 'test' that
anyone can access. This is also intended only for testing,
and should be removed before moving into a production
environment.


Remove test database and access to it? (Press y|Y for Yes, any other key for No) : y
 - Dropping test database...
Success.

 - Removing privileges on test database...
Success.

Reloading the privilege tables will ensure that all changes
made so far will take effect immediately.

Reload privilege tables now? (Press y|Y for Yes, any other key for No) : y
Success.

All done!

```
#### STEP 5 : 
```bash
mysql -u root -p 
```
To Restart, Stop and check the Status run the below commands:
```bash
sudo systemctl restart mysql
sudo systemctl stop mysql
sudo systemctl status mysql
```
## setup startup.properties file
Add the values in startup.properties file according to you.

## run startup.sh
Finally run the startup.sh file.


### Support

For support, email suport@nuvio.in or join our Slack channel.

### License

[![MIT License](https://img.shields.io/badge/License-BETA-green.svg)](https://choosealicense.com/licenses/mit/)

### Authors

- [@nuviotech](https://github.com/nuviotech)
