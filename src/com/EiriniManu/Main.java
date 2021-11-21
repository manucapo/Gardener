package com.EiriniManu;


public class Main {

    public static void main(String[] args)
    {





        String seqDiagPath = "Diagrams\\sequenceDiagram.txt";  // relative path to sequence diagram file

        PythonSequenceDiagramGenerator seqDiagGen = new PythonSequenceDiagramGenerator("callingClass", "methodName");

        seqDiagGen.GenerateSequenceDiagramTextFile(seqDiagPath);
        seqDiagGen.GenerateSequenceDiagramImage(seqDiagPath);



        TestMethod testM = new TestMethod();
        Reflector reflector = new Reflector();

        reflector.ReflectOnClass(testM);
        reflector.ReflectOnMethod(testM,"Test3", String.class, int.class, boolean.class);


       JavaParser parser = new JavaParser();
       String fileName = "TestMethod.java";
       String path = "C:\\Users\\manol\\Desktop\\Softwarentwicklung VF\\gardener\\src";
       String packageName = "com.EiriniManu";
       String className = "TestMethod";
       String methodName = "Test3";

       parser.ParseMethodFromClass(parser.ParseFile(fileName, parser.SetSourceRoot(path,packageName)), className, methodName);

    }


}


