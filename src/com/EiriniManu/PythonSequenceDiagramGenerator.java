package com.EiriniManu;

import org.python.util.PythonInterpreter; // import Jython inline Python interpreter.
import org.python.core.*;

public class PythonSequenceDiagramGenerator implements IPythonSequenceDiagramGenerator {

private String callingClassName;
private String methodName;


    public PythonSequenceDiagramGenerator(String callingClassName, String methodName){

        setCallingClassName(callingClassName);
        setMethodClassName(methodName);
        // TODO CONSTRUCTOR
    }

    @Override
    public void generateSequenceDiagramTextFile(String path) {
        try(PythonInterpreter pyIntp = new PythonInterpreter()) // instantiate python script interpreter
        {
            pyIntp.exec("tempdiag = open('" + path +  "', 'w+')");
            pyIntp.exec("tempdiag.write('@startuml \\n')");
            pyIntp.exec("tempdiag.write('"+ callingClassName + " -> Bob: "+ methodName + " \\n')");
            pyIntp.exec("tempdiag.write('@enduml \\n')");
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
