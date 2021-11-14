package com.EiriniManu;

import org.python.util.PythonInterpreter;
import org.python.core.*;

import net.sourceforge.plantuml.SourceStringReader;

public class Main {

    public static void main(String[] args)
    {
	// write your code here

       try(PythonInterpreter pyInt = new PythonInterpreter())
       {
           pyInt.exec("print('hello world')");
       }
    }
}
