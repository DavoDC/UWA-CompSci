
# Import crypto lib
# Had to do this to get it to run:
# pip uninstall crypto && pip uninstall pycryptodome && pip install pycryptodome
from Crypto.Util.strxor import strxor 

# The raw ciphertext
ct_raw = "303c2436415b554d081641153a58172d400d243a022751481d21431d2634510d0741332e2d5a0b3a4318390b354a444852540d"

# The bytes of the ciphertext
ct_bytes = bytes.fromhex(ct_raw) 

# Get the known plaintext fragment bytes
known = b"CITS3004{"

# Combining the known plaintext with the ciphertext will give the key
# i.e P XOR C = K
key = strxor(ct_bytes[:8], known[:8]) 

# Repeat key and combine to decrypt
pt = strxor(ct_bytes, (key * 7)[:51])

# Print plaintext
print(pt.decode("UTF-8"))