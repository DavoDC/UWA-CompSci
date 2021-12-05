sudo apt-get install software-properties-common
sudo add-apt-repository ppa:deadsnakes/ppa
pip install stegcracker
export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/home/seed/.local/bin:/home/seed/.local/bin

curl https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/Leaked-Databases/rockyou.txt.tar.gz -o rockyou.txt.tar.gz
tar xvzf rockyou.txt.tar.gz
