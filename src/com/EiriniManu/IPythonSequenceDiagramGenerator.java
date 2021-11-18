package com.EiriniManu;

public interface IPythonSequenceDiagramGenerator {

    public void generateSequenceDiagramTextFile(String path);

    public boolean setCallingClassName(String name);
    public boolean setMethodClassName(String name);
}
