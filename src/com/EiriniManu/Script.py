
import sys
import os

def generateFile(path, implementingClass, methodName):
 tempdiag = open(path, 'w+')
 tempdiag.write('@startuml \n')
 tempdiag.write(implementingClass + ' -> Bob: ' + methodName + '\n')
 tempdiag.write('@enduml \n')

print 'test'


