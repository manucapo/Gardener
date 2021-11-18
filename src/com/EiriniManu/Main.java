package com.EiriniManu;

import java.io.File;         // import Java File reader
import java.util.List;

import net.sourceforge.plantuml.GeneratedImage;       // import plantUML file Reader and image generator
import net.sourceforge.plantuml.SourceFileReader;





public class Main {

    public static void main(String[] args)
    {

        String seqDiagPath = "Diagrams\\sequenceDiagram.txt";  // relative path to sequence diagram file

        PythonSequenceDiagramGenerator seqDiagGen = new PythonSequenceDiagramGenerator("callingClass", "methodName");

        seqDiagGen.generateSequenceDiagramTextFile(seqDiagPath);

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


