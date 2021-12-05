"""
CITS 3004
Name: Python BlockChain
Authors: Other UWA Lecturers, Alvaro Monsalve
Last Update: 14 AUG 2021

Notes:  solution from https://hackernoon.com/learn-blockchains-by-building-one-117428612f46
"""



# Import libraries
import hashlib
from hashlib import sha256
import json
from time import time
from urllib.parse import urlparse
from uuid import uuid4



# Import flask libraries
import requests
from flask import Flask, jsonify, request



"""
This is a simple Proof of Work Algorithm:

1) Find a number p' (y) such that hash(pp') / hash(xy) contains leading 4 zeroes
2) Where p (x) is the previous proof, and p' (y) is the new proof
        
:param last_block: <dict> last Block
:return: <int> proof

Note: Add the hash of the previous block so PoW cannot be reused.
"""

#last_proof = last_block['proof']
#last_hash = self.hash(last_block)

#------------------------------------------------
# MY CODE
#------------------------------------------------


#         Question 2 
# 10 points Save Answ 
# Using the example shown in Task2 a3 (Section proof Of Work) , 
# what is the proof Of work (y) if we want 4 leading 
# Zeroes (0000)? 
# Keep x=5 and sha256. 
# The initial value of y=O.
#  Note that the while condition should take into account 4 leading 
# zeros now. 
# Provide your aswer as a number (i.e., 1000) 

# p
#x = last_proof 
x=5

# p', start from 0
y = 0 

# While hash value does not have four leading zeroes
#while sha256(f'{x}{y}'.encode()).hexdigest()[:4] != "0000":
while sha256(str(x*y).encode()).hexdigest()[:4] != "0000":
    # Keep searching
    y += 1



# When 4 zero number found, print info
print('solution value is {}'.format(y))


# CHECK
# Calculates if valid - Rule: First 4 hex are Zero
block_data = str(x*y).encode()
block_hash = sha256(block_data).hexdigest()

# Print
print("CHECK:")
print("INFO: " + str(f'{x}  {y}'))
print('Block Hash value is:  {}'.format(block_hash))
print('Is a valid Block?:   ', block_hash[:4] == "0000")