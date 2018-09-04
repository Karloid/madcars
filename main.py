import json
import random
import sys
import json
import math

#hello

def eprint(*args, **kwargs):
    pass
    print(*args, file=sys.stderr, **kwargs)


def parseNewMatch(parsedInput):
    pass


def doMove(cmd):
    print(json.dumps({"command": cmd, 'debug': cmd}))


piHalf = math.pi / 2

while True:
    inp = input()
    parsedInput = json.loads(inp)
    if parsedInput['type'] == 'new_match':
        eprint("new match start =======")
        parseNewMatch(parsedInput)
        continue

    # eprint(parsedInput['my_car'][1])
    # eprint(parsedInput['params']['enemy_car'][1])
    myCarAngle = parsedInput['params']['my_car'][1]
    eprint("m " + str(myCarAngle))

    desiredAngle = piHalf *  parsedInput['params']['my_car'][2]

    cmd = 'right'
    if myCarAngle > desiredAngle:
        cmd = 'left'

    eprint("m " + cmd + " " + str(myCarAngle))
    doMove(cmd)
