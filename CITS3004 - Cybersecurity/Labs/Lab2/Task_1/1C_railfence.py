# CITS 3004
# Name: Transposition Cypher
# Authors: Other UWA Lecturers, Alvaro Monsalve
# Last Update: 06 JUL 2021
# Requirements: Python 3.8
# Run: cls && python3 1C_railfence.py

import os

## FENCE 
def fence(lst, numrails):
    fence = [[None] * len(lst) for n in range(numrails)]
    rails = list(range(numrails - 1)) + list(range(numrails - 1, 0, -1))
    for n, x in enumerate(lst):
        #print(n, ' ', x)
        fence[rails[n % len(rails)]][n] = x

    #print(fence)
    if 0: # debug
        for rail in fence:
            print(''.join('.' if c is None else str(c) for c in rail))

    return [c for rail in fence
                for c in rail
                    if c is not None]


## ENCODE ##
def encode(text, n):
    # Convert fence list to string
    return ''.join(fence(text, n))

## DECODE ##
def decode(text, n):
    # Get length of text
    rng = range(len(text))
    # Get fence
    pos = fence(rng, n)
    return ''.join(text[pos.index(n)] for n in rng)





##################### CALLING OF FUNCTIONS #############

message = 'ATTACK AT DAWN'
print('Message is: ', message)

for dimension in range(2, 15):
    print('Dimension: ' + str(dimension))
    encrypted_message = encode(message, dimension)
    print('The Encrypted message is: ', encrypted_message) # ACTWTAKA.ANT.D
    decrypted_message = decode(encrypted_message, dimension)
    print('The Decrypted message is: ', decrypted_message) # ATTACK.AT.DAWN
    print("")

print("\n\n")
os.system("python3 1A_monoalpha.py")