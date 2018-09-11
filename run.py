import os
os.system("zip -r py.zip main.py myutils.py stratif.py stratsim.py steps.py"
          "&& python3 ../miniaicups/madcars/Runners/localrunner.py -f \"python3 ./main.py 1\"")
