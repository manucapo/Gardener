
import sys
import os

def generateDiagFile(path):
 tempdiag = open(path, 'w+')
 tempdiag.write('@startuml \n')
 tempdiag.write('structure.getImplementingClassName() -> Bob:  structure.getMethodName() \n')
 tempdiag.write('@enduml \n')

print 'test'


