"""
CITS 3004
Name: Verify Proof of Work
Authors: Alvaro Monsalve
Last Update: 14 AUG 2021
"""
from hashlib import sha256
import json


"""
INSERT YOUR PARAMETERS HERE:
From Previous Block Index (Previous JSON File)
	previous_proof (Also known as last_proof)
	
From Current Block Index (Current JSON File)
	proof
	previous_hash (Also known as last_hash)
"""
with open('mine.json') as f:
  data = json.load(f)

last_proof = "100" 	# For Block "Index":1, this proof is hardcoded to 100
proof = data['proof']	# proof value
previous_hash = data['previous_hash'] 	# "previous_hash" string



# Calculates if valid - Rule: First 4 hex are Zero
block_data = f'{last_proof}{proof}{previous_hash}'.encode()
block_hash = sha256(block_data).hexdigest()
print('Block Hash value is:  {}'.format(block_hash))
print('Is a valid Block?:   ', block_hash[:4] == "0000")

