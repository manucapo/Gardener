package com.EiriniManu;

/*
    This class represents an object that can handle the creation of a plantUML file from the information stored in the Diagram Structure data type.
    Currently, the Actual File generation is handled by a python Script through the JyhtonCaller helper class.
    This class feeds the python script the necessary information
 */

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class SequenceDiagramGenerator implements ISequenceDiagramGenerator {

private DiagramStructure structure;

private  Reflector reflector;

private JavaParser parser;

private JythonCaller jCaller;


    public SequenceDiagramGenerator(){

         structure = new DiagramStructure();
         reflector = new Reflector();
         parser = new JavaParser();
         jCaller = new JythonCaller();
        // TODO CONSTRUCTOR
    }

    public SequenceDiagramGenerator(DiagramStructure structure){

        this.structure = structure;
        // TODO CONSTRUCTOR
    }

    public void updateDiagramStructure(String methodName, Object cls, Class<?>... params){
        structure.setMethodName(methodName);

        reflector.ReflectOnClass(cls, structure);
        reflector.ReflectOnMethod(cls,methodName,structure ,params);
    }

    public void generateSequenceDiagram(String path){
        generateSequenceDiagramTextFile(path);
        generateSequenceDiagramImage(path);

    }

    public void generateSequenceDiagramTextFile(String path) {

        try {
            File file = new File( "C:\\Users\\manol\\Desktop\\Softwarentwicklung VF\\gardener\\src\\com\\EiriniManu\\Script.py"); // HOW TO GET THIS PATH PROPERLY !!!
            InputStream stream = new FileInputStream(file);

            jCaller.createDiagramFile(stream, path, structure);

        }catch (Exception e){
            System.out.println("ERROR READING PYTHON SCRIPT");
            System.out.println(e.toString());
        }

    }

    public void generateSequenceDiagramImage(String path) {

        try {
            File file = new File("Diagrams\\sequenceDiagram.txt");
            SourceFileReader seqFileReader = new SourceFileReader(file);     // instantiate plantUML file parser
            List<GeneratedImage> imgList = seqFileReader.getGeneratedImages();  // generate UML sequence diagram as png

        }catch (Exception e){                                               // handle exceptions
            System.out.println("ERROR READING SEQUENCE DIAGRAM FILE");
            System.out.println(e.toString());
        }
    }


}
