package com.EiriniManu.Parsing.Parser;

/*
    This class represents an object that can instantiate the adequate parser for the chosen parsing strategy
*/

import com.EiriniManu.IO.DiagramStructure;
import com.EiriniManu.ParserType;


public class JavaParserFactory {

    public static IJavaParser create(ParserType parserType){

        IJavaParser parser = null;
        switch (parserType){
            case SAFE:
                parser = new SafeJavaParser();
                parser.addObserver(DiagramStructure.getInstance());
                return parser;
            case REFLECTION:
                parser = new ReflectionJavaParser();
                parser.addObserver(DiagramStructure.getInstance());
                return parser;
            case DEEP:
                parser = new DeepJavaParser();
                parser.addObserver(DiagramStructure.getInstance());
                return parser;
            case BLOCK:
                parser = new BlockJavaParser();
                parser.addObserver(DiagramStructure.getInstance());
                return parser;
            default:
                break;
        }
        return  null;
    }

}
