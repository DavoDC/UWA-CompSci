# CITS 3004
# Name: Vigenere Cypher
# Authors: Alvaro Monsalve
# Last Update: 06 JUL 2021
# Requirements: Python 3.8
# Ack: Based on https://gist.github.com/dssstr/aedbb5e9f2185f366c6d6b50fad3e4a4


def encrypt_vigenere(plaintext, key):
    key_length = len(key)
    key_as_int = [ord(i) for i in key]
    plaintext_int = [ord(i) for i in plaintext]
    ciphertext = ''
    for i in range(len(plaintext_int)):
        value = (plaintext_int[i] + key_as_int[i % key_length]) % 26
        ciphertext += chr(value + 65)
    return ciphertext


def decrypt_vigenere(ciphertext, key):
    key_length = len(key)
    key_as_int = [ord(i) for i in key]
    ciphertext_int = [ord(i) for i in ciphertext]
    plaintext = ''
    for i in range(len(ciphertext_int)):
        value = (ciphertext_int[i] - key_as_int[i % key_length]) % 26
        plaintext += chr(value + 65)
    return plaintext

#Example usage
encrypted_message = 'ZWHIKVYIGBTELXXEGWHIKBTIAWHIKKDEYB'
key = 'WRITE KEY HERE'
print('Encrypted Message is: ', encrypted_message)
print('key is: ', key)
#ciphertext = encrypt_vigenere(message, key)
#print('Encrypted Message is: ', ciphertext)
plaintext = decrypt_vigenere(encrypted_message, key)
print('Decrypted Message is: ', plaintext)