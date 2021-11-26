package com.EiriniManu;

import java.io.InputStream;

public class Main {
    
    public static void main(String[] args)
    {
        String pathToSequenceDiagram = "Diagrams\\sequenceDiagram.txt";                                            // Choose desired (relative) path to sequence diagram file
        TestMethod testMethod = new TestMethod();                                                                  // Instantiate a class that provides some simple methods to test the program with.
        JavaParser parser = new JavaParser();                                                                      // Instantiate a class that can parse java source code to generate an AST (Abstract Syntax Tree)
        parser.addPackageDependencies("com.EiriniManu.");                                        // Add packages to check
        parser.addPackageDependencies("java.lang.");
        SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator(parser);            // Instantiate a class that can create a plantUML sequence diagram




        // Some extra metadata entered by the user. (NEED TO FIND A BETTER WAY TO PASS THIS)
       String fileName = "TestMethod.java";
       String path = "src";                        // Relative path to TestMethod Class
       String packageName = "com.EiriniManu";
       String className = "TestMethod";
       String methodName = "Test3";


        // String methodName, Object cls, String className , String classfileName, String classFilePath, String packageName, Class<?>... params
       //OPERATION STEPS

       // 1) Update the information structure that contains metadata on the method the user wants to display as a diagram
        sequenceDiagramGenerator.updateDiagramStructure(methodName , testMethod,className ,fileName,path ,packageName , String.class, int.class, boolean.class);

        // 2) Generate plantUML sequence diagram using the updated information structure.
        sequenceDiagramGenerator.generateSequenceDiagram(pathToSequenceDiagram);

    }
}


