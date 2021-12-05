#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# CITS3004
# Name: White Space Encoder and Decoder
# Authors: Other Lecturers and Alvaro Monsalve, David
# Last Update: 25 SEP 2021
# Requirements: Python 3.8

# RUN USING: 
# cls && python3 WScoder.py

# Import libs
import binascii
import os


########## HELPER METHODS ##########
def text_from_bits(bits, encoding='utf-8', errors='surrogatepass'):
    """Convert to text from bits"""
    n = int(bits, 2)
    return int2bytes(n).decode(encoding, errors)


def text_to_bits(text, encoding='utf-8', errors='surrogatepass'):
    """Convert text to bits"""
    bits = bin(int(binascii.hexlify(text.encode(encoding, errors)), 16))[2:]
    return bits.zfill(8 * ((len(bits) + 7) // 8))

def int2bytes(i):
    """Convert an int to bytes"""
    hex_string = '%x' % i
    n = len(hex_string)
    return binascii.unhexlify(hex_string.zfill(n + (n & 1)))



def decode():
    """Decode a message from a text file"""

    # Notify, heading
    print("######## DECODING MESSAGE")

    # Set output file name
    output = "output.txt"
    # output = "iloveshrek_OUTPUT.txt"

    # Setup
    outfile = open(output, 'r')
    lines = outfile.readlines()

    # Process (code pre-made, untouched!)
    bits = ''
    zcount = 0
    for line in lines:
        line = line[:-1]
        if len(line) == 0:
            bits += '0'
            zcount += 1
        elif line[-1] == " ":
            bits += '1'
            zcount = 0
        else:
            bits += '0'
            zcount += 1
        if zcount >= 8:
            diff = len(bits) % 8
            if diff == 0:
                diff = 8
            bits = bits[:-diff]
            break
    if len(bits) > 0:
        text = text_from_bits(bits)
        print("Your secret message is: " + text)
    else:
        print("No secret message in this file!")

    outfile.close()




def encode():
    """Encode a message into a text file"""

    # Notify, heading
    print("######## ENCODING MESSAGE")

    # VARIABLES
    # Set the original file name
    original = "chapter1.txt"

    # Set the output file name
    output = "output.txt"

    # Set the secret message
    message = "hello world"

    # SETUP
    # Open original file for reading, and read in lines
    orig_file = open(original, 'r', encoding='utf-8')
    orig_lines = orig_file.readlines()

    # Open output file for writing
    outfile = open(output, 'w')   

    # Convert message to bits
    h_bits = text_to_bits(message)
    
    # PROCESS (my code starts here)
    # White space added
    wsadded = 0

    ### Iterate over old lines
    # Get length of original lines
    orig_len = len(orig_lines)

    # For each line in original lines
    for index in range(orig_len):

        # New line holder, init to original line, stripped of new line chars
        new_line = orig_lines[index].rstrip()

        # If index is within number of bits
        if index < len(h_bits):
            
            # If current bit CHAR is '1':
            if h_bits[index] == '1':

                # Add white space to the line
                new_line = new_line + " "

                # Count
                wsadded += 1

        # Write new_line to output file
        outfile.write(new_line + "\n")
        

    # Notify
    print("White spaces added: ", wsadded)
    print("")

    # Close files
    orig_file.close()
    outfile.close()





def main(): 
    """Main function, entrypoint"""
    # Print heading
    print("\n############# Lab 8 - Task 1 - Whitespace Encode/Decoder ############# \n")
    # Encode then decode
    encode()
    decode()

if __name__ == "__main__": 
	main()