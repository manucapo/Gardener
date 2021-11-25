package com.EiriniManu;

public class Main {
    
    public static void main(String[] args)
    {
        String pathToSequenceDiagram = "Diagrams\\sequenceDiagram.txt";                                            // Choose desired (relative) path to sequence diagram file
        TestMethod testMethod = new TestMethod();                                                                  // Instantiate a class that provides some simple methods to test the program with.
        SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator();            // Instantiate a class that can create a plantUML sequence diagram
        JavaParser parser = new JavaParser();                                                                      // Instantiate a class that can parse java source code to generate an AST (Abstract Syntax Tree)

        // Some extra metadata entered by the user. (NEED TO FIND A BETTER WAY TO PASS THIS)
       String fileName = "TestMethod.java";
       String path = "src";                        // Relative path to TestMethod Class
       String packageName = "com.EiriniManu";
       String className = "TestMethod";
       String methodName = "Test3";



       //OPERATION STEPS

       // 1) Update the information structure that contains metadata on the method the user wants to display as a diagram
        sequenceDiagramGenerator.updateDiagramStructure(methodName , testMethod,className ,fileName,path ,packageName , String.class, int.class, boolean.class);

        // 2) Generate plantUML sequence diagram using the updated information structure.
        sequenceDiagramGenerator.generateSequenceDiagram(pathToSequenceDiagram);

    }
}


