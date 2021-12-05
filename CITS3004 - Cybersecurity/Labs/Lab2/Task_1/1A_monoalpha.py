
# CITS 3004
# Name: Monoalphabetic Cypher
# Authors: Russell Ballestrini, Jin Hong, Alvaro Monsalve
# Last Update: 06 JUL 2021
# Requirements: Python 3.8
# Run using: python3 1A_monoalpha.py

from random import shuffle
from string import ascii_letters
from string import ascii_lowercase
from string import ascii_uppercase
from string import digits
import collections


##########################################
############## FUNCTIONS #################
##########################################




##### CIPHER FUNCTIONS #####

def random_monoalpha_cipher():
    """Generate a Monoalphabetic Cipher
        Returns a dictionary of substitution table randomly generated
    """

    # Pool letters and digits
    pool = ascii_letters + digits

    # Convert pool to list
    original_pool = list(pool)

    # Convert pool to list and shuffle
    shuffled_pool = list(pool)
    shuffle(shuffled_pool)

    # Create table via pairing them
    return dict(zip(original_pool, shuffled_pool))




def key_monoalpha_cipher(key):
    """
        Generate a Monoalphabetic Cipher using the key provided
        Returns a dictionary of substitution table using the key
        The order of the substitution table is described in the Week 2 lectures
        e.g. if key="pineapple", the table is: 
        'pinealmoqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789bcdfghjk'
        Note: Assumes key is lowercase
    """

    # Remove duplicates from key to get processed key
    proc_key = "".join(sorted(set(key), key=key.index))

    # List of mono cipher values, initialized with processed key
    mono_values = list(proc_key)


    ### Add values that come alphabetically after key but that are not in key

    # Extract lastchar of processed key
    last_char = proc_key[len(proc_key) - 1]

    # Get next character alphabetically (numerical)
    next_char_num = (ord(last_char) + 1) - 96

    # For each alphabet position, from the next_char to 26
    for ab_pos in range(next_char_num, 27):

        # Convert to char
        curchar = chr(ab_pos + 96)

        # If char is not in processed key
        if not str(curchar) in proc_key:

            # Add to cipher values
            mono_values.append(curchar)


    ### Add uppercase letters next
    mono_values.extend(list(ascii_uppercase))


    ############## DISABLE DIGITS
    ### Add digits next
    mono_values.extend(list(digits))
    

    ### Add remaining lowercase

    # Get current list as string
    mono_valS = ''.join(mono_values)

    # For each lowercase character
    for curchar in ascii_lowercase:

        # Get as string
        curcharS = str(curchar)

        # If char is NOT already in values
        if not curcharS in mono_valS:
            
            # Add to values
            mono_values.append(curchar)  

    # Compare values
    # print("Comparison of ciper values: ")
    # correctvalues = "pinealmoqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789bcdfghjk"
    myvalues = ''.join(mono_values)
    print(myvalues + " (MY VALUES)")
    # print(correctvalues + " (CORRECT ANSWER)")
    # for i,j in zip(myvalues, correctvalues):
    #         curResult = 'T' if i == j else 'F'
    #         print(curResult,end='')
    # fullresultBool = collections.Counter(myvalues)==collections.Counter(correctvalues)
    # print(' (DEFS CORRECT!)' if fullresultBool else ' (ISSUE!)')
    # print("")

    # Get  letters and digits
    normal_keys = list(ascii_letters + digits)

    # Create table via pairing keys and mono cipher values
    mono_dict = dict(zip(normal_keys, mono_values))

    # Return dictionary
    return mono_dict






def inverse_monoalpha_cipher(monoalpha_cipher):
    """Given a Monoalphabetic Cipher (dictionary) return the inverse."""
    inverse_monoalpha = {}
    for key, value in monoalpha_cipher.items():
        inverse_monoalpha[value] = key
    return inverse_monoalpha


def print_cipher(cipher, linebyline):
    """
    Print out a cipher dictionary
    Arg1: Cipher Dictionary
    Arg2: True if you want to print line by line, False otherwise
    """
    print("Cipher dict: ")

    # Note: Key is value in plaintext
    for key,value in cipher.items():
        line = "'" + key + "' becomes " + value
        if linebyline:
            print(line) 
        else:
            print(line,end='   ')

    # Space
    print("")
    print("")




##### ENCRYPTION/DECRYPTION FUNCTIONS #####

def encrypt_with_monoalpha(message, monoalpha_cipher):
    """Encrypts message using the substitution table monoalpha_cipher"""
    encrypted_message = []
    for letter in message:
        encrypted_message.append(monoalpha_cipher.get(letter, letter))
    return ''.join(encrypted_message)



def decrypt_with_monoalpha(encrypted_message, monoalpha_cipher):
    """Decrypts message using the substitution table monoalpha_cipher"""
    return encrypt_with_monoalpha(encrypted_message, 
                                inverse_monoalpha_cipher(monoalpha_cipher))







##########################################
########## CALLING OF FUNCTIONS ##########
##########################################


# Title
print("")
print("### CITS3004 Lab 2 - Task 1A ###")




### Cipher/dictionary
# Use shuffled
#cipher = random_monoalpha_cipher()


## Use key version
# Key
#key = "pineapple"
#key = "market"
key = "monopoly"
print('\nKey is: ' + key)
print("")

cipher = key_monoalpha_cipher(key)

# Print curent cipher dict
# print_cipher(cipher, False)
# print("")


### Enc/Dec

# Message
#message = "This is a Secret Message: HX343"
#message = "ATTACK AT DAWN" #1c
message = "secret message"
print('Original Message is:   ' + message)

# Encrypt message to get ciphertext
ciphertext = encrypt_with_monoalpha(message, cipher)
print('Encrypted Message is: ', ciphertext)


# Decrypt cipher text to get plaintext
plaintext = decrypt_with_monoalpha(ciphertext, cipher)
print('Decrypted Message is: ', plaintext)

