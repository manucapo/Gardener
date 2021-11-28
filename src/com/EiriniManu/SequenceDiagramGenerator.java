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


    public SequenceDiagramGenerator(){                // Default constructor.
         structure = new DiagramStructure();
         reflector = new Reflector();
         parser = new JavaParser();
         jCaller = new JythonCaller();
    }


    public void updateDiagramStructure(String methodName, Object cls, String className , String classFilePath, String packageName, Class<?>... params){            // Update structure with information gathered by reflector and parser
        structure.setMethodName(methodName);
        reflector.ReflectOnClass(cls, structure);
        reflector.ReflectOnMethod(cls,methodName,structure ,params);
        parser.ParseMethodFromClass(parser.ParseFile(className, parser.SetSourceRoot(classFilePath,packageName)), className, methodName, structure);

    }

    public void generateSequenceDiagram(String pathToSource, String pathToDiagram, String methodName, Object cls, String className, String packageName, Class<?>... params){                  // Wrapper function for the two-steps required for creating a plantUML diagram image.
        updateDiagramStructure(methodName, cls, className, pathToSource, packageName, params);
        generateSequenceDiagramTextFile(pathToDiagram);
        generateSequenceDiagramImage(pathToDiagram);
        resetStructure();
    }

    public void generateSequenceDiagramTextFile(String path) {     // Use JythonCaller class to generate Text file with plantUML code
        try {
            File file = new File( "src\\com\\EiriniManu\\Script.py");  // Relative path to python Script
            InputStream stream = new FileInputStream(file);                     // Read File as InputStream
            jCaller.createDiagramFile(stream, path + "\\" + structure.getMethodName() + ".txt", structure);                 // Pass input stream and script path to the Jython caller. It can then generate the file using the information in the diagram structure
        }catch (Exception e){
            System.out.println("ERROR READING PYTHON SCRIPT");
            System.out.println(e.toString());
        }

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
        parser.addPackageDependencies(dependency);
    }

    public void resetStructure()
    {
        structure = new DiagramStructure();
    }
}
