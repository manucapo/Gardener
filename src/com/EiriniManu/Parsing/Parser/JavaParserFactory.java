package com.EiriniManu.Parsing.Parser;

import com.EiriniManu.IO.DiagramStructure;
import com.EiriniManu.Messaging.IMessageObserver;
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
            default:
                break;
        }
        return  null;
    }

}
