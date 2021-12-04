package com.EiriniManu;

/*
    This class represents an object that can handle the creation of a plantUML file from the information stored in the Diagram Structure data type.
    Currently, the Actual File generation is handled by a python Script through the JyhtonCaller helper class.
    This class feeds the python script the necessary information
 */

import com.EiriniManu.IO.DiagramFileWriter;
import com.EiriniManu.IO.DiagramStructure;
import com.EiriniManu.Parsing.Parser.*;
import com.EiriniManu.Parsing.Reflector;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;

import java.io.File;
import java.util.List;

public class SequenceDiagramGenerator implements ISequenceDiagramGenerator {
    private DiagramStructure structure;
    private Reflector reflector;
    private ParserContext parserContext;
    private DiagramFileWriter diagramFileWriter;


    public SequenceDiagramGenerator(ParserType parserType){                // Default constructor.
         structure = DiagramStructure.getInstance();
         reflector = new Reflector();
        reflector.addObserver(structure);
         diagramFileWriter = new DiagramFileWriter();
        setParser(parserType);
    }


    public void updateDiagramStructure(String methodName, Object cls, String className , String classFilePath, String packageName, Class<?>... params){            // Update structure with information gathered by reflector and parser
        reflector.ReflectOnClass(cls);
        reflector.ReflectOnMethod(cls,methodName,params);
        parserContext.executeParsing(methodName, className, classFilePath, packageName,structure);
    }

    public void generateSequenceDiagram(String pathToSource, String pathToDiagram, String methodName, Object cls, String className, String packageName, Class<?>... params){                  // Wrapper function for the two-steps required for creating a plantUML diagram image.
        updateDiagramStructure(methodName, cls, className, pathToSource, packageName, params);
        generateSequenceDiagramTextFile(pathToDiagram);
        generateSequenceDiagramImage(pathToDiagram);
        structure.reset();
    }

    public void generateSequenceDiagramTextFile(String path) {     // Use JythonCaller class to generate Text file with plantUML code
        diagramFileWriter.createDiagramFile(path + "\\" + structure.getMethodName() + ".txt");
    }

    public void generateSequenceDiagramImage(String path) {  // Use the plantUML library to generate a sequence diagram image from the plantUML text file
        try {
            File file = new File(path + "\\" + structure.getMethodName() + ".txt");
            SourceFileReader fileReader = new SourceFileReader(file);     // instantiate plantUML file reader
            List<GeneratedImage> imgList = fileReader.getGeneratedImages();  // generate UML sequence diagram from file as png

        }catch (Exception e){
            System.out.println("ERROR READING SEQUENCE DIAGRAM FILE");
            System.out.println(e.toString());
        }
    }

    public void addDependency(String dependency){
        parserContext.addDependency(dependency);
    }

    public void setParser(ParserType type){

        switch (type){
            case SAFE:
                parserContext = new ParserContext(ParserType.SAFE);
                break;
            case REFLECTION:
                parserContext = new ParserContext(ParserType.REFLECTION);
                break;
            case DEEP:
                parserContext = new ParserContext(ParserType.DEEP);
                break;
            case BLOCK:
                parserContext = new ParserContext(ParserType.BLOCK);
                break;
            default:
                break;
        }



    }

}
