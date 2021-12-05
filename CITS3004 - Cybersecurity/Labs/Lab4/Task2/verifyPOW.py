"""
CITS 3004
Name: Verify Proof of Work
Authors: Alvaro Monsalve
Last Update: 14 AUG 2021
"""
from hashlib import sha256
import json


# EXAMPLE
# seed ../Task2: curl http://127.0.0.1:5000/mine
# {"index":4,"message":"New Block Forged",
# "previous_hash":"cb2d0465f99b65133b06d6fac82d857255eca1a236a640d93381561f7ae45dfe",
# "proof":18627,"transactions":[{"amount":1,"recepient":"d80d0bda55e5438890f058e0ed82077d","sender":"0"}]}
# last_proof = "18627"

# seed ../Task2: curl http://127.0.0.1:5000/mine
# {"index":5,"message":"New Block Forged","
# previous_hash":"d302dc1f5e2652e88135bf3e8e1e4ab1fda8caf7ff3779efeba33820dd8c2ef3",
# "proof":12176,"transactions":[{"amount":1,"recepient":"d80d0bda55e5438890f058e0ed82077d","sender":"0"}]}
# seed ../Task2:
# proof = "12176"
# previous_hash = "d302dc1f5e2652e88135bf3e8e1e4ab1fda8caf7ff3779efeba33820dd8c2ef3"

"""
INSERT YOUR PARAMETERS HERE:
From Previous Block Index (Previous JSON File)
	last_proof (Also known as previous_proof)
"""
# For Block "Index":1, this proof is hardcoded to 100
last_proof = "100" 	
#last_proof = "44004"

""""
From Current Block Index (Current JSON File)
	proof
	previous_hash (Also known as last_hash)
"""
#with open('mine.json') as f:

with open('my_mine.json') as f:
	data = json.load(f)
proof = data['proof']	# proof value
previous_hash = data['previous_hash'] 	# "previous_hash" string


# Calculates if valid - Rule: First 4 hex are Zero
block_data = f'{last_proof}{proof}{previous_hash}'.encode()
block_hash = sha256(block_data).hexdigest()

# Print
print("CHECK:")
print("INFO: " + str(f'{last_proof}  {proof}  {previous_hash}'))
print('Block Hash value is:  {}'.format(block_hash))
print('Is a valid Block?:   ', block_hash[:4] == "0000")

