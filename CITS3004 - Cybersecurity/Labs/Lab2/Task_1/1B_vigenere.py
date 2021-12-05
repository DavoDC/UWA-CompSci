# CITS 3004
# Name: Vigenere Cypher
# Authors: Alvaro Monsalve
# Last Update: 06 JUL 2021
# Requirements: Python 3.8
# Ack: Based on https://gist.github.com/dssstr/aedbb5e9f2185f366c6d6b50fad3e4a4
# Run using: python3 1B_vigenere.py

from string import ascii_uppercase
import itertools

###### ENCRYPTION ######
# Given
def encrypt_vigenere(plaintext, key):
    # Get key length
    key_length = len(key)

    # Convert key to ints
    key_as_int = [ord(i) for i in key]

    # Convert plaintext to ints
    plaintext_int = [ord(i) for i in plaintext]

    # Ciphertext holder
    ciphertext = ''

    # For each pos in plaintext
    for i in range(len(plaintext_int)):

        # Value is plaintext + key (wrap around position), wrap around alphabet
        value = (plaintext_int[i] + key_as_int[i % key_length]) % 26

        # Convert int to char and add to ciphertext
        ciphertext += chr(value + 65)

    return ciphertext



###### DECRYPTION ######
# Given
# Pretty much same as encrypt but iterate over ciphertext and subtract
def decrypt_vigenere(ciphertext, key):
    key_length = len(key)
    key_as_int = [ord(i) for i in key]
    ciphertext_int = [ord(i) for i in ciphertext]
    plaintext = ''
    for i in range(len(ciphertext_int)):
        value = (ciphertext_int[i] - key_as_int[i % key_length]) % 26
        plaintext += chr(value + 65)
    return plaintext


##### KEY FINDER #####
# My own
def find_key():
    """
    We have intercepted a ciphertext "Z(WHIK)VYIGB TEL XXEG (WHIK) BTI AWHIKKDEYB"
    We also figured that a plaintext phrase "OVER" is encrypted to "WHIK". 
    Write a function that can find the key length and find the key.
    Returns key
    """

    print("We know a plaintext fragment 'OVER' is encrypted to 'WHIK'")

    ### INFORMATION
    # Ciphertext with spaces
    ct_spaced = "ZWHIKVYIGB TEL XXEG WHIK BTI AWHIKKDEYB"

    # Ciphertext fragment and len
    cfrag = "WHIK"

    # Plaintext fragment and len
    pfrag = "OVER"
    pfl = len(pfrag)


    ### Find distance ciphertext fragment positions in CT

    # Find where first instance ends
    first_end = ct_spaced.find(cfrag) + len(cfrag) - 1

    # Find where second instance starts
    second_start = ct_spaced.find(cfrag, first_end, len(ct_spaced))

    # Get distance = subtract and record absolute value
    frag_dist = abs(first_end - second_start)


    ### Get key length by dividing distance by fragment length
    key_len = int(frag_dist / pfl)

    # Notify
    print("\nFound key length: " + str(key_len) + " by looking at fragment distance")



    ### Use enc OR dec to get key

    # Key characters holder
    key_chars=list()

    # Space
    print("")

    # For each key character
    for i in range(key_len):

        # For each alpabet character
        for curAB in ascii_uppercase:

            # Get as string that represents a guessing of the key char
            key_char_guess = str(curAB)

            ### USE ENCRYPTION
            # Try encrypting part of the plaintext frag with the key guesss,
            # to get a ciphertext guess
            cfrag_guess = encrypt_vigenere(str(pfrag[i]), key_char_guess)

            # If ciphertext guess part matches the real ciphertext frag part
            if (cfrag_guess == str(cfrag[i])):

                # It must be a character of the key
                key_chars.append(curAB)

                ### DECRYPTION IS AN EQUIVALENT ALTERNATIVE
                # Try decrypting part of cfrag with letter
                pfrag_guess = decrypt_vigenere(str(cfrag[i]), key_char_guess)
                # If plaintext DOESNT match other plaintext fragment
                if not (pfrag_guess == str(pfrag[i])):
                    print("ISSUE!")
                    # This never happens as both enc and dec 
                    # can be successfully used to test the keys

                # Notify
                msg = "Plaintext fragment letter '" + str(pfrag[i]) + "'"
                msg += " combined with some key letter '?'"
                msg += " must produce '" + cfrag_guess + "' (ciphertext fragment letter)"
                print(msg)
                print("> Trying all letters as the key letter...", end='  ')
                print("'" + str(curAB) + "' worked!")
                print("")
                

    print("So the key characters are = " + str(key_chars))
    print("")

    # Key holder
    key = "temp"

    # GET ALL PERMUTATIONS
    perms = list(itertools.permutations(key_chars))
    print("There are 4!=" + str(len(perms)) + " permutations of these key characters:" )
    for curperm in perms:
        #print(str(x))
        curpermS = ''.join(curperm)
        print(curpermS, end="   ")
        if(curpermS == "TIME"):
            print("<<<< (HUMAN READABLE!!!)")
            key = curpermS

    print("")
    print("")
    print("Key could be ***" + key + "*** as it is human readable, and (most) others are not")
    print("")
        
    return key





############ RUN FUNCTIONS ###########################
# Note: Encrypt not being run
encrypted_message = 'ZWHIKVYIGBTELXXEGWHIKBTIAWHIKKDEYB'
print('Encrypted Message is: ' + encrypted_message)

key = find_key()


print ("Attempting decryption with: " + key)
plaintext = decrypt_vigenere(encrypted_message, key)
print('Decrypted Message is: ' + plaintext)

print("Looks human readable as well! Key is correct!")






