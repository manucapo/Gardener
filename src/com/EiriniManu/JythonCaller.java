package com.EiriniManu;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.__builtin__;


import java.io.InputStream;

public class JythonCaller implements IJythonCaller{

    private PythonInterpreter pyIntp;

    public JythonCaller(){
        pyIntp = new PythonInterpreter();
    }

    public void createDiagramFile(InputStream stream, String path, DiagramStructure structure){


        String filePath ="C:\\Users\\manol\\Desktop\\Softwarentwicklung VF\\gardener\\src\\com\\EiriniManu\\Script.py";
        String scriptName = "Script";

        pyIntp.execfile (stream);

        String funcName = "generateFile";
        PyObject func = pyIntp.get(funcName);


        try {
            if (func == null){
                throw new Exception("Could not find Python function");
            } else {
                func.__call__(new PyString(path), new PyString(structure.getImplementingClassName()), new PyString(structure.getMethodName()));
            }

        }catch (Exception e){
            System.out.println(e.toString());
            System.out.println("ERROR CREATING DIAGRAM PLANTUML FILE");
        }

        pyIntp.close();

    }
}
