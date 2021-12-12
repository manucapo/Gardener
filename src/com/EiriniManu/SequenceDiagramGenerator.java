package com.EiriniManu;

/*
    This class represents an object that acts as the public endpoint for the gardener library.
    This class lets the end user configure the parser by providing the desired parser strategy.
    It also lets the user provide some extra metadata necessary for the creation of the plantUML diagrams
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
    private Reflector reflector;
    private ParserContext parserContext;

    public SequenceDiagramGenerator(ParserType parserType){
         reflector = new Reflector();
        reflector.addObserver(DiagramStructure.getInstance());
        setParser(parserType);
    }


    public void updateDiagramStructure(String methodName, Object cls, String className , String classFilePath, String packageName, Class<?>... params){            // Update structure with information gathered by reflector and parser
        reflector.ReflectOnClass(cls);
        reflector.ReflectOnMethod(cls,methodName,params);
        parserContext.executeParsing(methodName, className, classFilePath, packageName,DiagramStructure.getInstance());
    }

    public void generateSequenceDiagramTextFile(String path, String methodName) {                        // generate Text file with plantUML syntax
        DiagramStructure.getInstance().serialize(path + "\\" + methodName + ".txt");
    }

    public void generateSequenceDiagramImage(String path, String methodName) {  // Use the plantUML library to generate a sequence diagram image from the plantUML syntax text file
        try {
            File file = new File(path + "\\" + methodName + ".txt");
            SourceFileReader fileReader = new SourceFileReader(file);     // instantiate plantUML file reader
            List<GeneratedImage> imgList = fileReader.getGeneratedImages();  // generate UML sequence diagram from file as png

        }catch (Exception e){
            System.out.println("ERROR READING SEQUENCE DIAGRAM FILE");
            System.out.println(e.toString());
        }
    }

    public void generateSequenceDiagram(String pathToSource, String pathToDiagram, String methodName, Object cls, String className, String packageName, Class<?>... params){        // Wrapper function for the steps required for creating a plantUML diagram image.
        updateDiagramStructure(methodName, cls, className, pathToSource, packageName, params);
        generateSequenceDiagramTextFile(pathToDiagram, methodName);
        generateSequenceDiagramImage(pathToDiagram, methodName);
        DiagramStructure.getInstance().reset();
    }

    public void addDependency(String dependency){        // Add an external package to the list of dependencies used by the REFLECTION parser type to resolve method targets
        parserContext.addDependency(dependency);
    }

    public void setParser(ParserType type){            // set desired parser strategy

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
