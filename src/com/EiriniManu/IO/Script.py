
import sys
import os

def generateFile(path, implementingClass, methodName, methodCalls, methodCallTargets, methodBlock):
 tempdiag = open(path, 'w+')
 tempdiag.write('@startuml \n')
 tempdiag.write('participant Actor \n')
 tempdiag.write('Actor -> ' + implementingClass + ': ' + methodName + '\n')
 tempdiag.write('activate ' + implementingClass + '\n')
 i = 0
 blockList = [""]
 for method in methodCalls:
  if methodCallTargets[i] == "this":
   if methodBlock[i] != "top":
    tempdiag.write("alt " + methodBlock[i] + '\n')
   tempdiag.write(implementingClass + ' -> ' + implementingClass + ': ' + method + '\n')
   tempdiag.write('activate ' + implementingClass + '\n')
   tempdiag.write(implementingClass + '-->' + implementingClass + '\n')
   tempdiag.write('deactivate ' + implementingClass + '\n')
  elif methodCallTargets[i].find("[]") >= 0:
   if methodBlock[i] != "top":
    tempdiag.write("alt " + methodBlock[i] + '\n')
   tempdiag.write(implementingClass + ' -> ' + "__Array__" + ': ' + method + '\n')
   tempdiag.write('activate ' + "__Array__" + '\n')
   tempdiag.write("__Array__" + '-->' + implementingClass + '\n')
   tempdiag.write('deactivate ' + "__Array__" + '\n')
  elif methodCallTargets[i].find("LOSTMESSAGE") >= 0:
   if methodBlock[i] != "top":
    tempdiag.write("alt " + methodBlock[i] + '\n')
   tempdiag.write(implementingClass + ' ->x] ' + ': ' + method + '\n')
  else:
   if methodBlock[i] != "top":
    tempdiag.write("alt " + methodBlock[i] + '\n')
    blockList.append(methodBlock[i])
   tempdiag.write(implementingClass + ' -> ' + methodCallTargets[i] + ': ' + method + '\n')
   tempdiag.write('activate ' + methodCallTargets[i] + '\n')
   tempdiag.write(methodCallTargets[i] + '-->' + implementingClass + '\n')
   tempdiag.write('deactivate ' + methodCallTargets[i] + '\n')
   if methodBlock[i] != methodBlock[i-1]:
    tempdiag.write("end " + '\n')
  i = i+1
 tempdiag.write('return \n')
 tempdiag.write('@enduml \n')

print 'Python Script Done Running'


