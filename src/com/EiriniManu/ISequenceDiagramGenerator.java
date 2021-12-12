package com.EiriniManu;

/*
    This interface defines an object that acts as the public endpoint for the gardener library.
    Such an object should let the end user configure the parser and provide the necessary information for the creation of the plantUML sequence diagrams
 */

public interface ISequenceDiagramGenerator {
    void generateSequenceDiagram(String pathToSource, String pathToDiagram, String methodName, Object cls, String className, String packageName, Class<?>... params);
    void setParser(ParserType type);
}
