package com.EiriniManu;

public class Main {
    
    public static void main(String[] args)
    {
        String pathToSequenceDiagram = "Diagrams\\sequenceDiagram.txt";                                            // Choose desired (relative) path to sequence diagram file
        TestMethod testMethod = new TestMethod();                                                                  // Instantiate a class that provides some simple methods to test the program with.
        PythonSequenceDiagramGenerator sequenceDiagramGenerator = new PythonSequenceDiagramGenerator();            // Instantiate a class that can create a plantUML sequence diagram
        JavaParser parser = new JavaParser();                                                                      // Instantiate a class that can parse java source code to generate an AST (Abstract Syntax Tree)

        // Some extra metadata entered by the user. (NEED TO FIND A BETTER WAY TO PASS THIS)
       String fileName = "TestMethod.java";
       String path = "C:\\Users\\manol\\Desktop\\Softwarentwicklung VF\\gardener\\src";
       String packageName = "com.EiriniManu";
       String className = "TestMethod";
       String methodName = "Test3";

       //OPERATION STEPS

       // 1) Parse the given java class for information.
       parser.ParseMethodFromClass(parser.ParseFile(fileName, parser.SetSourceRoot(path,packageName)), className, methodName);

       // 2) Update the information structure that contains metadata on the method the user wants to display as a diagram
        sequenceDiagramGenerator.updateDiagramStructure("Test3", testMethod, String.class, int.class, boolean.class);

        // 3) Generate plantUML sequence diagram using the updated information structure.
        sequenceDiagramGenerator.generateSequenceDiagram(pathToSequenceDiagram);

    }
}


