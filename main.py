import sys
import json
import math
import myutils

cerrEnabled = False

if len(sys.argv) > 1:
    cerrEnabled = sys.argv[1] == "1"


# hello

def eprint(*args, **kwargs):
    pass
    if cerrEnabled:
        print(*args, file=sys.stderr, **kwargs)


def parseNewMatch(parsedInput):
    pass


def doMove(cmd):
    print(json.dumps({"command": cmd, 'debug': cmd}))


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
    myCarAngle = myutils.normalizeAngle(worldParams['my_car'][1])  # my angle
    # eprint("m " + str(myCarAngle))

    desiredAngle = (piHalf * 0.7) * worldParams['my_car'][2]  # left or right

    cmd = 'right'
    cmdOposite = 'left'
    delta = myCarAngle - desiredAngle
    if delta > 0:
        cmd = cmdOposite
        cmdOposite = 'right'

    isCloseToPerfectAngle = abs(delta) < piHalf / 2
    if tick < 50 and isCloseToPerfectAngle and tick % 5 != 0:
        cmd = cmdOposite

    if tick > 50 and tick % 5 != 0 and abs(myCarAngle) < 1:
        myX = worldParams['my_car'][0]
        enemyX = worldParams['enemy_car'][0]

        cmd = 'left' if myX > enemyX else 'right'
        eprint("tick " + str(tick) + " m " + cmd + " " + str(myCarAngle))


    doMove(cmd)
