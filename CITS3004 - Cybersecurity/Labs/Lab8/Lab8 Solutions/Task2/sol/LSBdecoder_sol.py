# CITS 3004
# Name: Least Significant Bit Image Stegano - Decoder
# Authors: Other Lecturers and Alvaro Monsalve
# Last Update: 25 SEP 2021
# Requirements: Python 3.8

import os
import LsbSteg

def main():
    stegoImageName = input("What is the secret image file to unpack?: ")   
    if os.name == "nt":
        stegoImageWorkingDirectory = os.getcwd() + "\\" + stegoImageName 
    else:
        stegoImageWorkingDirectory = os.getcwd() + "/" + stegoImageName 
    stegoMessage = LsbSteg.extractMessage(stegoImageWorkingDirectory)
    print(stegoMessage)

if __name__ == "__main__":
    main()