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
    output = input("What would the output file name be: ")
    message = input("What is the secret message?       : ")
    orgfile = open(original, 'r') #original file
    outfile = open(output, 'w')   #hidden message file
    lines = orgfile.readlines()
    h_bits = text_to_bits(message)
    if len(h_bits) < len(lines):
        i = 0
        for line in lines:
            if i < len(h_bits):
                outfile.write(line[:-1] + ' ' * int(h_bits[i]) + '\n')
            else:
                outfile.write(line)
            i += 1

        print("encoding hidden message complete!")
    else:
        print("The message is too long to be hidden in the original file")
    orgfile.close()
    outfile.close()
    return 0


if __name__ == "__main__":
    main()