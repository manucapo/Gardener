package com.EiriniManu;

import java.io.File;         // import Java File reader
import java.util.List;

import net.sourceforge.plantuml.GeneratedImage;       // import plantUML file Reader and image generator
import net.sourceforge.plantuml.SourceFileReader;


import org.python.util.PythonInterpreter; // import Jython inline Python interpreter.
import org.python.core.*;


public class Main {

    public static void main(String[] args)
    {

        String seqDiagPath = "Diagrams\\sequenceDiagram.txt";  // relative path to sequence diagram file

       try(PythonInterpreter pyInt = new PythonInterpreter()) // instantiate python script interpreter
       {
           pyInt.exec("tempdiag = open('" + seqDiagPath +  "', 'w+')");   // create and write to file
           pyInt.exec("tempdiag.write('@startuml \\n')");
           pyInt.exec("tempdiag.write('Alice -> Bob: test 1 \\n')");
           pyInt.exec("tempdiag.write('Alice -> Bob: test 2 \\n')");
           pyInt.exec("tempdiag.write('@enduml \\n')");
       }

       try {
           File seqDiag = new File(seqDiagPath);                        // Open file
           SourceFileReader seqFileReader = new SourceFileReader(seqDiag);     // instantiate plantUML file parser
           List<GeneratedImage> imgList = seqFileReader.getGeneratedImages();  // generate UML sequence diagram as png

       }catch (Exception e){                                               // handle exceptions
           System.out.println("ERROR READING SEQUENCE DIAGRAM FILE");
           System.out.println(e.toString());
       }

    }
}


