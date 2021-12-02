package com.EiriniManu;

import java.io.File;

public interface ISequenceDiagramGenerator {
    void generateSequenceDiagramTextFile(String path);
    void generateSequenceDiagramImage(String path);
    void generateSequenceDiagram(String pathToSource, String pathToDiagram, String methodName, Object cls, String className, String packageName, Class<?>... params);
    void updateDiagramStructure(String methodName, Object cls, String className , String classFilePath, String packageName, Class<?>... params);
}
