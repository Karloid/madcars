import math
import sys

twoPi = math.pi * 2

debugCmd = " "


def normalizeAngle(angle):
    # reduce the angle

    angle = angle % twoPi

    # force it to be the positive remainder, so that 0 <= angle < 360
    angle = (angle + twoPi) % twoPi

    # force into the minimum absolute value residue class, so that -180 < angle <= 180
    if angle > math.pi:
        angle -= twoPi
    return angle


cerrEnabled = False

if len(sys.argv) > 1:
    cerrEnabled = sys.argv[1] == "1"


# hello


def eprint(*args, **kwargs):
    pass
    global debugCmd
    if cerrEnabled:
        print(*args, file=sys.stderr, **kwargs)
    else:
        debugCmd = debugCmd + "\n" + args[0]


def getDebugCmd():
    global debugCmd
    cmd = debugCmd
    debugCmd = " "
    return cmd