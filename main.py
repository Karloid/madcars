import json
import random
import sys

def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)

while True:
    z = input()
    eprint(z)
    commands = ['left', 'right', 'stop']
    cmd = random.choice(commands)
    print(json.dumps({"command": cmd, 'debug': cmd}))