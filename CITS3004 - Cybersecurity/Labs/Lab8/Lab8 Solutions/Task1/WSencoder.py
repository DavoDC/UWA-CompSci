# CITS 3004
# Name: White Space Encoder
# Authors: Other Lecturers and Alvaro Monsalve
# Last Update: 25 SEP 2021
# Requirements: Python 3.8

import binascii

def text_to_bits(text, encoding='utf-8', errors='surrogatepass'):
    bits = bin(int(binascii.hexlify(text.encode(encoding, errors)), 16))[2:]
    return bits.zfill(8 * ((len(bits) + 7) // 8))

def main():
    original = input("What is the original file name    : ")
    output = input("What is the output file name: ")
    message = input("What is the secret message?       : ")
    orgfile = open(original, 'r') #original file
    outfile = open(output, 'w')   #hidden message file
    lines = orgfile.readlines()
    h_bits = text_to_bits(message)
    
    #+++++++++++++++++++++++++++++++++++++
    #   YOUR CODE GOES HERE
    # Here, you should include a white space at the end of each sentence
    # given in the variable: lines
    # the secret message is already provided in a binary form in the variable:
    # h_bits
    # 0 - no white space, 1 - add a white space
    # These are written to the output file.
    #+++++++++++++++++++++++++++++++++++++
    
    orgfile.close()
    outfile.close()
    return 0


if __name__ == "__main__":
    main()