package com.EiriniManu;

import org.python.util.PythonInterpreter; // import Jython inline Python interpreter.
import org.python.core.*;

public class PythonSequenceDiagramGenerator implements IPythonSequenceDiagramGenerator {


    public PythonSequenceDiagramGenerator(){
        // TODO CONSTRUCTOR
    }

    @Override
    public void generateSequenceDiagramTextFile(String path) {
        try(PythonInterpreter pyInt = new PythonInterpreter()) // instantiate python script interpreter
        {
            pyInt.exec("tempdiag = open('" + path +  "', 'w+')");   // create and write to file
            pyInt.exec("tempdiag.write('@startuml \\n')");
            pyInt.exec("tempdiag.write('Alice -> Bob: test 1 \\n')");
            pyInt.exec("tempdiag.write('Alice -> Bob: test 2 \\n')");
            pyInt.exec("tempdiag.write('@enduml \\n')");
        }

    }
}
