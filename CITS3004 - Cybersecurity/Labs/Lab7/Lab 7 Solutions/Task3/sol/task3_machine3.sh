sysctl -q net.ipv4.tcp_max_syn_backlog
sudo sysctl -a | grep cookie
sudo sysctl -w net.ipv4.tcp_syncookies=0
netstat -na
