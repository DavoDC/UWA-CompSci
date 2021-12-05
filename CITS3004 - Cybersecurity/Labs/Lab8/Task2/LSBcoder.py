# CITS 3004
# Name: Least Significant Bit Image Stegano - Encoder and Decoder
# Authors: Other Lecturers and Alvaro Monsalve
# Last Update: 25 SEP 2021
# Requirements: Python 3.8

# RUN with: cls && python3 LSBcoder.py

# Import libs
import os
import LsbSteg


# Set the file name with the message
# message_file = "Hidden.txt"

# Set the image file to be used
imageName = "Plane.png"
# imageName = "Horse.png"

# Set the secret image (output) file name (extension must be PNG)
# stegoImageName = "Plane+Hidden.png"
# stegoImageName = "Horse+Hidden.png"
stegoImageName = "HiddenQuiz.png"


# ENCODE (PRE MADE)
def encode():

    # Notify, heading
    print("######## ENCODING MSG + IMG")

    # Read in message (Added by DC)
    # m_file_obj = open(message_file, 'r', encoding='utf-8')
    # message =  m_file_obj.read().replace('\n', '')
    message = "hello world"

    # Get paths
    if os.name == "nt":
        imageWorkingDirectory = os.getcwd() + "\\" + imageName
        stegoImageWorkingDirectory = os.getcwd() + "\\" + stegoImageName
    else:
        imageWorkingDirectory = os.getcwd() + "/" + imageName
        stegoImageWorkingDirectory = os.getcwd() + "/" + stegoImageName

    # Embed message        
    LsbSteg.embedMessage(imageWorkingDirectory, stegoImageWorkingDirectory, message) 
    
    # Notify
    # print("Encoded " +  message_file + " into " + imageName + " to produce: " + stegoImageName)
    print("Encoded " +  message + " into " + imageName + " to produce: " + stegoImageName)

    # Space
    print("")   


# DECODE
def decode():

    # Notify, heading
    print("######## DECODING IMAGE")

    #+++++++++++++++++++++++++++++++++++++++
    # YOUR CODE GOES HERE
    # given the steganography image and using the LsbSteg module
    # retrieve the hidden message
    #+++++++++++++++++++++++++++++++++++++++
    print("Decoding " + stegoImageName + "...")
    message = LsbSteg.extractMessage(stegoImageName)
    print("Output:")
    print(message)
    


# RUN CODE
print("\n############# Lab 8 - Task 2 - LSB Encoder/Decoder ############# \n")
encode()

# decode()



