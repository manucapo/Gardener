package com.EiriniManu;

import com.github.javaparser.ast.Node;

import java.io.InputStream;

public class Main {
    
    public static void main(String[] args)
    {
        String pathToSequenceDiagram = "Diagrams";                                            // Choose desired (relative) path to sequence diagram file
        TestMethod testMethod = new TestMethod();                                                                  // Instantiate a class that provides some simple methods to test the program with.
        JavaParser parser = new JavaParser();                                                                      // Instantiate a class that can parse java source code to generate an AST (Abstract Syntax Tree)
       JythonCaller jCaller = new JythonCaller();
        parser.addPackageDependencies("com.EiriniManu.");                                        // Add packages to help resolve classes
        parser.addPackageDependencies("java.lang.");
        parser.addPackageDependencies("java.util.");
        parser.addPackageDependencies("java.util.Optional<T>.");
        parser.addPackageDependencies("java.util.Optional.");
        parser.addPackageDependencies("com.github.javaparser.ast.");
        parser.addPackageDependencies("com.github.javaparser.ast.expr");
        parser.addPackageDependencies("com.github.javaparser.ast.stmt");
        parser.addPackageDependencies("com.github.javaparser.");
        parser.addPackageDependencies("com.github.javaparser.HasParentNode<T>.");
        parser.addPackageDependencies("com.github.javaparser.HasParentNode.");


        SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator(parser);            // Instantiate a class that can create a plantUML sequence diagram



        // Some extra metadata entered by the user. (NEED TO FIND A BETTER WAY TO PASS THIS)
       String fileName = "TestMethod.java";
       String path = "src";                        // Relative path to TestMethod Class
       String packageName = "com.EiriniManu";
       String className = "TestMethod";
        String methodName = "parseMethodNode";

       for (int i = 1; i <= 19; i++){
         methodName = "test" + String.valueOf(i);



           // 1) Update the information structure that contains metadata on the method the user wants to display as a diagram
           sequenceDiagramGenerator.updateDiagramStructure(methodName ,testMethod,className ,fileName,path ,packageName , String.class, int.class, boolean.class);

           // 2) Generate plantUML sequence diagram using the updated information structure.
           sequenceDiagramGenerator.generateSequenceDiagram(pathToSequenceDiagram);

           sequenceDiagramGenerator = new SequenceDiagramGenerator(parser);
       }


    }
}
  //  public String parseMethodNode(Node methodcallNode, int subMethodCounter, DiagramStructure structure, String lastClassName) {
//createDiagramFile(InputStream stream, String path, DiagramStructure structure) {

