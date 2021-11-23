package com.EiriniManu;

/*
    This class represents a helper object that can execute python code from a .py file.
    It can be used by other classes to handle any task in python
 */

import org.python.core.Py;
import org.python.core.PyArray;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.python.core.PyString;


import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class JythonCaller implements IJythonCaller {

    private PythonInterpreter pyInterpreter;

    public JythonCaller() {
        pyInterpreter = new PythonInterpreter();             // Instantiate the python interpreter class from Jython library
    }

    public void createDiagramFile(InputStream stream, String path, DiagramStructure structure) {  // Wrapper function for the python script function call

        pyInterpreter.execfile(stream);          // Execute the file with the python interpreter

        String functionName = "generateFile";              // Used to search for a function with this name in the Python script
        PyObject pyFunction = pyInterpreter.get(functionName);   // get the function as a jython object

        try {
            if (pyFunction == null) {
                throw new Exception("Could not find Python function");
            } else {
                PyObject[] args = {new PyString(path), new PyString(structure.getImplementingClassName()), new PyString(structure.getMethodName()), new PyArray(Array.class, structure.getMethodCalls().toArray()), new PyArray(Array.class, structure.getMethodCallTargets().toArray())  };
                pyFunction.__call__(args);  // Call the function in the python string with argument array.
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("ERROR CREATING DIAGRAM PLANTUML FILE");
        }
        pyInterpreter.cleanup();       // Cleanup any open python interpreter threads/files to make sure file is written to and closed before next step
    }
}
