
import sys
import os

def generateFile(path, implementingClass, methodName, methodCalls, methodCallTargets):
 tempdiag = open(path, 'w+')
 tempdiag.write('@startuml \n')
 tempdiag.write(' [o-> ' + implementingClass + ': ' + methodName + '\n')
 i = 0
 for method in methodCalls:
  if methodCallTargets[i] == "this":
   tempdiag.write(implementingClass + ' -> ' + implementingClass + ': ' + method + '\n')
  else:
   tempdiag.write(implementingClass + ' -> ' + methodCallTargets[i] + ': ' + method + '\n')
  i = i+1
 tempdiag.write('@enduml \n')

print 'Python Script Done Running'


