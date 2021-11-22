
import sys
import os

def generateFile(path, implementingClass, methodName):
 tempdiag = open(path, 'w+')
 tempdiag.write('@startuml \n')
 tempdiag.write(' [o-> '+implementingClass+': ' + methodName + '\n')
 tempdiag.write('@enduml \n')

print 'Python Script Done Running'


