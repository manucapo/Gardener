package com.EiriniManu.Parsing.Parser;

/*
    This class represents an object that holds context of the strategy pattern used to implement different parsing algorithms
*/


import com.EiriniManu.IO.DiagramStructure;
import com.EiriniManu.ParserType;

public class ParserContext {

    private IJavaParser parser;

    public ParserContext(ParserType type){
        this.parser = JavaParserFactory.create(type);
        DiagramStructure.getInstance().addObserver(this.parser);
    }

    public void executeParsing(String methodName, String className, String classFilePath, String packageName, DiagramStructure structure){
        parser.execute(methodName, className, classFilePath, packageName, structure);
    }
    public void addDependency(String dependency){
        if((parser instanceof ReflectionJavaParser)) {
            parser.addDependency(dependency);
        }
    }

}
