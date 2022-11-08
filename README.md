# Gardener
A project to automatically generate plantUML sequence diagrams from java methods.

DEPENDENCIES:

Java AST generation:

JavaParser ( http://javaparser.org ) 

UML diagram image generation:

PlantUML ( https://plantuml.com )

PROJECT GOAL:

To simplify the process of analyzing source code by providing a java software library to automatically generate UML sequence diagrams from the source code of valid java methods. 

CURRENT SCOPE:

Java 1.8 extension valid methods

CURRENT FEATURES:

Parse single methods and attribute them to the call target

Resolve call targets using scope information

Correctly identify the order of method calls

Resolve symbols declared inside of catch blocks

SOMETIMES resolve generic methods from scope context

Identify deeply nested blocks (if, for, while)

Parse and regognize the order of calls in deeply recursively nested method calls

USE CASES:

Reverse engineer source code by visualizing the flow and order of method calls

Quickly create visual documentation for object oriented programming projects
