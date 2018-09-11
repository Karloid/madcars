import json
import math
import myutils
from myutils import eprint

import platform

def parseNewMatch(parsedInput):
    pass


def doMove(cmd):
    cmdStr = 'left' if cmd == -1 else 'right' if cmd == 1 else 'stop'
    print(json.dumps({"command": cmdStr, "debug": myutils.debugCmd})) #debug from utils
    myutils.debugCmd = ""

def go():
    eprint("python version is " + platform.python_version())
    piHalf = math.pi / 2
    tick = 0
    while True:
        tick = tick + 1
        inp = input()
        parsedInput = json.loads(inp)
        if parsedInput['type'] == 'new_match':
            eprint("new match start =======")
            eprint(inp)
            parseNewMatch(parsedInput)
            tick = 1
            continue

        worldParams = parsedInput['params']

        doMove(1)
