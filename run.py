import os
os.system("zip -r py.zip main.py myutils.py"
          "&& python3 ../miniaicups/madcars/Runners/localrunner.py -s \"python3 ./main.py 1\"")
