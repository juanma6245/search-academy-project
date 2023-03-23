import sys
import os
import uuid
import math


def createFile(destinyFolder):
    filename = str(uuid.uuid4())
    return open(os.path.join(destinyFolder, filename), 'w') 

def createFolder(destinyFolder):
    if(not os.path.exists(destinyFolder)):
        os.mkdir(destinyFolder)
    


origin = sys.argv[1]
destiny = sys.argv[2]
linesFileMax = int(sys.argv[3])

with open(origin, 'r') as tsv:
    lines = tsv.read().splitlines()

numFiles = math.ceil(len(lines) / linesFileMax)

createFolder(destiny)

for i in range(0,numFiles):
    file = createFile(destiny)
    remainingLines = linesFileMax
    while(remainingLines > 0 and len(lines) > 0):
        file.write(lines.pop())
        remainingLines -= 1
        if(remainingLines > 0 and len(lines) != 0):
            file.write("\n")

print("File divided")
