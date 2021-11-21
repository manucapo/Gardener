package com.EiriniManu;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import org.python.util.PythonInterpreter; // import Jython inline Python interpreter.
import org.python.core.*;

import java.io.File;
import java.util.List;

public class PythonSequenceDiagramGenerator implements IPythonSequenceDiagramGenerator {

private String callingClassName;
private String methodName;


    public PythonSequenceDiagramGenerator(String callingClassName, String methodName){

        setCallingClassName(callingClassName);
        setMethodClassName(methodName);
        // TODO CONSTRUCTOR
    }


    public void GenerateSequenceDiagramTextFile(String path) {
        try(PythonInterpreter pyIntp = new PythonInterpreter()) // instantiate python script interpreter
        {
            pyIntp.exec("tempdiag = open('" + path +  "', 'w+')");
            pyIntp.exec("tempdiag.write('@startuml \\n')");
            pyIntp.exec("tempdiag.write('"+ callingClassName + " -> Bob: "+ methodName + " \\n')");
            pyIntp.exec("tempdiag.write('@enduml \\n')");
        }

    }

    @Override
    public void GenerateSequenceDiagramImage(String path) {
        try {
            File seqDiag = new File(path);                        // Open file
            SourceFileReader seqFileReader = new SourceFileReader(seqDiag);     // instantiate plantUML file parser
            List<GeneratedImage> imgList = seqFileReader.getGeneratedImages();  // generate UML sequence diagram as png

        }catch (Exception e){                                               // handle exceptions
            System.out.println("ERROR READING SEQUENCE DIAGRAM FILE");
            System.out.println(e.toString());
        }
    }

    @Override
    public boolean setCallingClassName(String name) {

        if(name != null){
            callingClassName = name;
            return  true;
        }
        return false;
    }

    @Override
    public boolean setMethodClassName(String name) {
        if(name != null){
            methodName = name;
            return true;
        }
        return  false;
    }
}
