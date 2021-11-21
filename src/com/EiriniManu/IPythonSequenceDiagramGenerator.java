package com.EiriniManu;

public interface IPythonSequenceDiagramGenerator {

    public void GenerateSequenceDiagramTextFile(String path);
    public void GenerateSequenceDiagramImage(String path);
    public boolean setCallingClassName(String name);
    public boolean setMethodClassName(String name);
}
