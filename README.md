# Gardener
A tool to automatically generate plantUML sequence diagrams from java methods.

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

Parse single method calls and attribute them to the correct call target

Resolve method call target using contextual scope information

Correctly identify the order of method calls inside functions

Resolve type for symbols declared inside of method blocks or catch blocks

SOMETIMES resolve generic method calls using method call scope context

Identify and parse deeply nested block structures ( if, for, while )

Parse and recognize the order of  calls in deeply recursively nested method calls

USE CASES:

Reverse engineer source code by visualizing the flow and order of method calls

Quickly create visual documentation for object oriented programming projects
