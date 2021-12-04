
import sys
import os

def generateFile(path, implementingClass, methodName, methodCalls, methodCallTargets, methodBlock):
 tempdiag = open(path, 'w+')
 tempdiag.write('@startuml \n')
 tempdiag.write('participant Actor \n')
 tempdiag.write('Actor -> ' + implementingClass + ': ' + methodName + '\n')
 tempdiag.write('activate ' + implementingClass + '\n')
 i = 0
 blockCount = 0
 countedblocknumber = [""]
 blockparent = 0
 blocknumber = 0
 runcount = 0
 nestedblocks = [""]
 blockname = ""
 for method in methodCalls:
  print("run :" + str(i) )
  if methodBlock[i].find("top") >= 0 :
    print("ok")
    blockCount = 0
    countedblocknumber = [""]
  elif methodBlock[i] != blockname and not countedblocknumber.count(methodBlock[i].split(" ")[1]) > 0:
   tempdiag.write("alt " + methodBlock[i] + '\n')
   blockCount += 1
   blockparent = methodBlock[i].split(" ")[2]
   blocknumber = methodBlock[i].split(" ")[1]
   blockname = methodBlock[i]
   countedblocknumber.append(blocknumber)
   print("PARENT : ")
   print(blockparent)
   print("BLOCK : ")
   print(blocknumber)
   print("COUNT : ")
   print(blockCount)
  if methodCallTargets[i] == "this":
   tempdiag.write(implementingClass + ' -> ' + implementingClass + ': ' + method + '\n')
   tempdiag.write('activate ' + implementingClass + '\n')
   tempdiag.write(implementingClass + '-->' + implementingClass + '\n')
   tempdiag.write('deactivate ' + implementingClass + '\n')
  elif methodCallTargets[i].find("[]") >= 0:
   tempdiag.write(implementingClass + ' -> ' + "__Array__" + ': ' + method + '\n')
   tempdiag.write('activate ' + "__Array__" + '\n')
   tempdiag.write("__Array__" + '-->' + implementingClass + '\n')
   tempdiag.write('deactivate ' + "__Array__" + '\n')
  elif methodCallTargets[i].find("LOSTMESSAGE") >= 0:
   tempdiag.write(implementingClass + ' ->x] ' + ': ' + method + '\n')
  else:
   tempdiag.write(implementingClass + ' -> ' + methodCallTargets[i] + ': ' + method + '\n')
   tempdiag.write('activate ' + methodCallTargets[i] + '\n')
   tempdiag.write(methodCallTargets[i] + '-->' + implementingClass + '\n')
   tempdiag.write('deactivate ' + methodCallTargets[i] + '\n')
  i = i+1
  if len(methodBlock) == i:
   for x in range(blockCount):
    tempdiag.write('end \n')
    blockCount -= 1
    if len(countedblocknumber) > 0:
          countedblocknumber.pop()
    print("ONE")
  elif methodBlock[i] == blockname:
   print("EQUAL")
  elif methodBlock[i].find("top") >= 0:
   for x in range(blockCount):
    tempdiag.write('end \n')
    blockCount -= 1
    if len(countedblocknumber) > 0:
          countedblocknumber.pop()
    print("TWO")
  elif  int(methodBlock[i].split(" ")[2]) == int(blockparent) and blocknumber > 1 :
   print("THREE")
   tempdiag.write('end \n')
   blockCount -= 1
   if len(countedblocknumber) > 0:
         countedblocknumber.pop()
  elif int(methodBlock[i].split(" ")[2]) < int(blockparent):
   print("FOUR")
   count = 0
   while count < int(blockparent):
    if (i - 2 - count) > 0:
     if methodBlock[i - 2 - count].split(" ")[1] == blockparent:
      break
     else:
      count += 1
   for x in range(count):
     tempdiag.write('end \n')
     blockCount -= 1
     if len(countedblocknumber) > 0:
      countedblocknumber.pop()
 tempdiag.write('return \n')
 tempdiag.write('@enduml \n')

print 'Python Script Done Running'


