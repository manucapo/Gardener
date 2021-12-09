package com.EiriniManu;

import com.EiriniManu.Parsing.Parser.ReflectionJavaParser;

public class Main {
    
    public static void main(String[] args)
    {
        TestMethod testMethod = new TestMethod();                                                                  // Instantiate a class that provides some simple methods to test the program with.
        SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator(ParserType.BLOCK);            // Instantiate a class that can create a plantUML sequence diagram
        sequenceDiagramGenerator.addDependency("com.EiriniManu");                                        // Add packages to help resolve classes
        sequenceDiagramGenerator.addDependency("java.lang");
        sequenceDiagramGenerator.addDependency("java.util");
        sequenceDiagramGenerator.addDependency("java.util.Optional<T>");
        sequenceDiagramGenerator.addDependency("java.util.Optional");
        sequenceDiagramGenerator.addDependency("com.github.javaparser.ast");
        sequenceDiagramGenerator.addDependency("com.github.javaparser.ast.expr");
        sequenceDiagramGenerator.addDependency("com.github.javaparser.ast.stmt");
        sequenceDiagramGenerator.addDependency("com.github.javaparser");
        sequenceDiagramGenerator.addDependency("com.github.javaparser.HasParentNode<T>");
        sequenceDiagramGenerator.addDependency("com.github.javaparser.HasParentNode");

        // Some extra metadata entered by the user. (NEED TO FIND A BETTER WAY TO PASS THIS)
       String pathToSource = "src";                        // Relative path to TestMethod Class
        String pathToSequenceDiagram = "Diagrams";                                            // Choose desired (relative) path to sequence diagram file
       String packageName = "com.EiriniManu";
       String className = "TestMethod";

       for (int i = 1; i <= 27; i++){
         String methodName = "test"  + String.valueOf(i);

           // Generate plantUML sequence diagram
           sequenceDiagramGenerator.generateSequenceDiagram(pathToSource ,pathToSequenceDiagram, methodName ,testMethod,className,packageName , String.class, int.class, boolean.class);
       }
    }
}

