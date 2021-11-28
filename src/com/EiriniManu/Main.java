package com.EiriniManu;

import java.io.InputStream;

public class Main {
    
    public static void main(String[] args)
    {
        String pathToSequenceDiagram = "Diagrams";                                            // Choose desired (relative) path to sequence diagram file
        TestMethod testMethod = new TestMethod();                                                                  // Instantiate a class that provides some simple methods to test the program with.
        JavaParser parser = new JavaParser();                                                                      // Instantiate a class that can parse java source code to generate an AST (Abstract Syntax Tree)
        parser.addPackageDependencies("com.EiriniManu.");                                        // Add packages to help resolve classes
        parser.addPackageDependencies("java.lang.");
        SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator(parser);            // Instantiate a class that can create a plantUML sequence diagram



        // Some extra metadata entered by the user. (NEED TO FIND A BETTER WAY TO PASS THIS)
       String fileName = "TestMethod.java";
       String path = "src";                        // Relative path to TestMethod Class
       String packageName = "com.EiriniManu";
       String className = "TestMethod";
       String methodName = "test";

       for (int i = 1; i <= 13; i++){
          methodName = "test" + String.valueOf(i);

          //methodName = "test12";


           // 1) Update the information structure that contains metadata on the method the user wants to display as a diagram
           sequenceDiagramGenerator.updateDiagramStructure(methodName , testMethod,className ,fileName,path ,packageName , String.class, int.class, boolean.class);

           // 2) Generate plantUML sequence diagram using the updated information structure.
           sequenceDiagramGenerator.generateSequenceDiagram(pathToSequenceDiagram);

           parser = new JavaParser();
           parser.addPackageDependencies("com.EiriniManu.");                                        // Add packages to help resolve classes
           parser.addPackageDependencies("java.lang.");
           sequenceDiagramGenerator = new SequenceDiagramGenerator(parser);
       }


    }
}


