import json
import math
import myutils
from myutils import eprint


def parseNewMatch(parsedInput):
    pass


def doMove(cmd):
    cmdStr = 'left' if cmd == -1 else 'right' if cmd == 1 else 'stop'
    print(json.dumps({"command": cmdStr, 'debug': "debug"})) #debug from utils


def go():
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

        cmd = 1
        cmdOposite = -1
        delta = myCarAngle - desiredAngle
        if delta > 0:
            cmd = cmdOposite
            cmdOposite = 1

        isCloseToPerfectAngle = abs(delta) < piHalf / 2
        if tick < 50 and isCloseToPerfectAngle and tick % 5 != 0:
            cmd = cmdOposite

        if tick > 50 and tick % 5 != 0:
            myX = worldParams['my_car'][0][0]
            enemyX = worldParams['enemy_car'][0][0]

            leftCmd = -1
            rightCmd = 1
            if abs(myCarAngle) > 1:
                rightCmd = leftCmd
                leftCmd = 1

            cmd = leftCmd if myX > enemyX else rightCmd
            eprint(f"tick {tick}  {cmd} {myCarAngle} myX {myX} enemyX {enemyX}")

        doMove(cmd)
