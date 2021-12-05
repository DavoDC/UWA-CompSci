# CITS 3004
# Name: Least Significant Bit Image Stegano - Encoder
# Authors: Other Lecturers and Alvaro Monsalve
# Last Update: 25 SEP 2021
# Requirements: Python 3.8

import os
import LsbSteg

def main():
    imageName = input("What is the image file to be used?: ")
    stegoImageName = input("What is the secret image file name? (extension must be png): ")
    message = input("What is the secret message?: ")
    if os.name == "nt":
        imageWorkingDirectory = os.getcwd() + "\\" + imageName
        stegoImageWorkingDirectory = os.getcwd() + "\\" + stegoImageName
    else:
        imageWorkingDirectory = os.getcwd() + "/" + imageName
        stegoImageWorkingDirectory = os.getcwd() + "/" + stegoImageName        
    
    LsbSteg.embedMessage(imageWorkingDirectory, stegoImageWorkingDirectory, message)    


if __name__ == "__main__":
    main()
