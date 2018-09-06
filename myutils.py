import math

twoPi = math.pi * 2


def normalizeAngle(angle):
    # reduce the angle

    angle = angle % twoPi

    # force it to be the positive remainder, so that 0 <= angle < 360
    angle = (angle + twoPi) % twoPi

    # force into the minimum absolute value residue class, so that -180 < angle <= 180
    if angle > math.pi:
        angle -= twoPi
    return angle