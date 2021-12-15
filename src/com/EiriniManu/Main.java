package com.EiriniManu;


public class Main {
    
    public static void main(String[] args)
    {
        TestMethod testMethod = new TestMethod();                                                                  // Instantiate a class that provides some simple methods to test the program with.

        SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator(ParserType.SAFE);            // Instantiate a class that can create a plantUML sequence diagram


        // Some extra metadata entered by the user. (NEED TO FIND A BETTER WAY TO PASS THIS)
       String pathToSource = "src";                                                            // Relative path to TestMethod Class
        String pathToSequenceDiagram = "Diagrams/SAFE";                                            // Choose desired (relative) path to sequence diagram file
       String packageName = "com.EiriniManu";                                                 // Package name
       String className = "TestMethod";                                                       // Class name

       for (int i = 1; i <= 8; i++){
         String methodName = "test"  + i;

           // Generate plantUML sequence diagram
           sequenceDiagramGenerator.generateSequenceDiagram(pathToSource ,pathToSequenceDiagram, methodName ,testMethod,className,packageName , String.class, int.class, boolean.class);
       }

       sequenceDiagramGenerator = new SequenceDiagramGenerator(ParserType.REFLECTION);
        pathToSequenceDiagram = "Diagrams/REFLECTION";

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

        for (int i = 9; i <= 13; i++){
            String methodName = "test"  + i;

            // Generate plantUML sequence diagram
            sequenceDiagramGenerator.generateSequenceDiagram(pathToSource ,pathToSequenceDiagram, methodName ,testMethod,className,packageName , String.class, int.class, boolean.class);
        }

        sequenceDiagramGenerator = new SequenceDiagramGenerator(ParserType.BLOCK);
        pathToSequenceDiagram = "Diagrams/BLOCK";

        for (int i = 19; i <= 23; i++){
            String methodName = "test"  + i;

            // Generate plantUML sequence diagram
            sequenceDiagramGenerator.generateSequenceDiagram(pathToSource ,pathToSequenceDiagram, methodName ,testMethod,className,packageName , String.class, int.class, boolean.class);
        }

        sequenceDiagramGenerator = new SequenceDiagramGenerator(ParserType.DEEP);
        pathToSequenceDiagram = "Diagrams/DEEP";

        for (int i = 24; i <=27; i++){
            String methodName = "test"  + i;

            // Generate plantUML sequence diagram
            sequenceDiagramGenerator.generateSequenceDiagram(pathToSource ,pathToSequenceDiagram, methodName ,testMethod,className,packageName , String.class, int.class, boolean.class);
        }

    }

}

