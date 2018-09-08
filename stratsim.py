import json
import math
import myutils
from myutils import eprint


def parseNewMatch(parsedInput):
    pass


def doMove(cmd):
    print(json.dumps({"command": cmd, 'debug': cmd}))


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

        cmd = 'right'
        cmdOposite = 'left'
        delta = myCarAngle - desiredAngle
        if delta > 0:
            cmd = cmdOposite
            cmdOposite = 'right'

        isCloseToPerfectAngle = abs(delta) < piHalf / 2
        if tick < 50 and isCloseToPerfectAngle and tick % 5 != 0:
            cmd = cmdOposite

        if tick > 50 and tick % 5 != 0:
            myX = worldParams['my_car'][0][0]
            enemyX = worldParams['enemy_car'][0][0]

            leftCmd = 'left'
            rightCmd = 'right'
            if abs(myCarAngle) > 1:
                rightCmd = leftCmd
                leftCmd = 'right'

            cmd = leftCmd if myX > enemyX else rightCmd
            eprint(f"tick {tick}  {cmd} {myCarAngle} myX {myX} enemyX {enemyX}")

        doMove(cmd)