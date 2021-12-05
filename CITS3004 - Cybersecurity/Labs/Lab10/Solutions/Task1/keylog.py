# CITS 3004
# Name: Logger
# Authors: Other Lecturers and Alvaro Monsalve
# Last Update: 13 OCT 2021
# Requirements: Python 3.8
 
from pynput import keyboard
 
def get_key_name(key):
    if isinstance(key, keyboard.KeyCode):
        return key.char
    else:
        return str(key)

def on_release(key):
    key_name = get_key_name(key)
    print('Pressed: {}'.format(key_name))
 
    if key_name == 'Key.esc':
        print('Exiting...')
        return False
 
with keyboard.Listener(on_release = on_release) as listener:
    listener.join()