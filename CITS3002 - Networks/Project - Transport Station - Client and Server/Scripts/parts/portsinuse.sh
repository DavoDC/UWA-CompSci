#!/bin/bash

PATH="/bin:/usr/bin"

echo ports in use:

case `uname` in
    Linux*)
        /bin/netstat -tulpn 2>&1 | cut -c21-40 | sed 's/.*\:\(.*\)/\1/'
        ;;
    Darwin*)
        /usr/sbin/netstat -an | grep -E '^tcp|udp' | cut -c22-40 | sed 's/.*\.\(.*\)/\1/'
        ;;
esac | grep '^[0-9]' | sort -nu
