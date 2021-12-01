package com.EiriniManu.Parsing.Parser;

import com.EiriniManu.IO.DiagramStructure;

public class ParserContext {

    private IJavaParser parser;

    public ParserContext(IJavaParser parser, DiagramStructure structure){
        this.parser = parser;
        this.parser.addObserver(structure);
        structure.addObserver(this.parser);
    }

    public void executeParsing(String methodName, String className, String classFilePath, String packageName, DiagramStructure structure){
        parser.execute(methodName, className, classFilePath, packageName, structure);
    }
    public void addDependency(String dependency){
        parser.addDependency(dependency);
    }

}
