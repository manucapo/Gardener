package com.EiriniManu;

import java.io.File;

public interface ISequenceDiagramGenerator {
    void generateSequenceDiagramTextFile(String path);
    void generateSequenceDiagramImage(String path);
    void generateSequenceDiagram(String path);
    void updateDiagramStructure(String methodName, Object cls, String className , String classfileName, String classFilePath, String packageName, Class<?>... params);
}
