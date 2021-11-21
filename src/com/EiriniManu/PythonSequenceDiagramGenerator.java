package com.EiriniManu;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import org.python.util.PythonInterpreter; // import Jython inline Python interpreter.

import java.io.File;
import java.util.List;

public class PythonSequenceDiagramGenerator implements IPythonSequenceDiagramGenerator {

private DiagramStructure structure;

private  Reflector reflector;

private JavaParser parser;


    public PythonSequenceDiagramGenerator(){

         structure = new DiagramStructure();
         reflector = new Reflector();
         parser = new JavaParser();
        // TODO CONSTRUCTOR
    }

    public PythonSequenceDiagramGenerator(DiagramStructure structure){

        this.structure = structure;
        // TODO CONSTRUCTOR
    }

    public void updateDiagramStructure(String methodName, Object cls, Class<?>... params){
        structure.setMethodName(methodName);

        reflector.ReflectOnClass(cls, structure);
        reflector.ReflectOnMethod(cls,methodName,structure ,params);
    }


    public void generateSequenceDiagramTextFile(String path) {
        try(PythonInterpreter pyIntp = new PythonInterpreter()) // instantiate python script interpreter
        {
            pyIntp.exec("tempdiag = open('" + path +  "', 'w+')");
            pyIntp.exec("tempdiag.write('@startuml \\n')");      // INIT


            pyIntp.exec("tempdiag.write('"+ structure.getImplementingClassName() + " -> Bob: "+ structure.getMethodName()  + " \\n')");
            pyIntp.exec("tempdiag.write('@enduml \\n')");
        }

    }

    @Override
    public void generateSequenceDiagramImage(String path) {
        try {
            File seqDiag = new File(path);                        // Open file
            SourceFileReader seqFileReader = new SourceFileReader(seqDiag);     // instantiate plantUML file parser
            List<GeneratedImage> imgList = seqFileReader.getGeneratedImages();  // generate UML sequence diagram as png

        }catch (Exception e){                                               // handle exceptions
            System.out.println("ERROR READING SEQUENCE DIAGRAM FILE");
            System.out.println(e.toString());
        }
    }


}
