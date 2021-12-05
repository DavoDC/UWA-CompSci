Ensure you follow the instructions carefully, otherwise you may damage your machine!
Always create backup for your files.
Runs on Python3

You must install pycryptodome first to use the Crypto library.
    sudo pip install pycryptodome

1. DO NOT RUN THE CODE STRAIGHT AWAY
2. Open the main.py in an editor
3. check the password in line 16. Ensure that this is of type byte (e.g., b'some string')
4. check the folder location to be applied at line 66
5. Open discover.py and define which files would be encrypted
6. by running main.py, you would have now encrypted all files with the specified extension
   in the folder you have specified
7. To decrypt, run main.py -d for decryption. It will ask for password.
8. If you do not enter the correct password, it will be decrypted using the wrong password.
   Then, you must decrypt using the real password, and encrypt using the wrong password.


The key length must be 128/192/256 bits long (other words, 16/24/32 characters long)
Please, do not ruin your own, and especially other people's, computer.
