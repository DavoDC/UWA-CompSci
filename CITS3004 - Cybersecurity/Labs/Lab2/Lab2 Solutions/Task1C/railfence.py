# CITS 3004
# Name: Transposition Cypher
# Authors: Other UWA Lecturers, Alvaro Monsalve
# Last Update: 06 JUL 2021
# Requirements: Python 3.8


def fence(lst, numrails):
    fence = [[None] * len(lst) for n in range(numrails)]
    #print('fence: ', fence)
    rails = list(range(numrails - 1)) + list(range(numrails - 1, 0, -1))
    #print('rails: ', rails)
    for n, x in enumerate(lst):
        #print(n, ' ', x)
        fence[rails[n % len(rails)]][n] = x

    #print('fence: ', fence)
    if 0: # debug
        for rail in fence:
            print(''.join('.' if c is None else str(c) for c in rail))

    return [c for rail in fence
                for c in rail
                    if c is not None]


def encode(text, n):
    return ''.join(fence(text, n))


def decode(text, n):
    rng = range(len(text))
    pos = fence(rng, n)
    return ''.join(text[pos.index(n)] for n in rng)


message = 'ATTACK AT DAWN'
print('Message is: ', message)
dimension = 3
#dimension = 13
print('Dimension: ', dimension)
encrypted_message = encode(message, dimension)
print('The Encrypted message is: ', encrypted_message) # ACTWTAKA.ANT.D
decrypted_message = decode(encrypted_message, dimension)
print('The Decrypted message is: ', decrypted_message) # ATTACK.AT.DAWN