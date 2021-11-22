package com.EiriniManu;

import java.io.File;

public interface ISequenceDiagramGenerator {
    void generateSequenceDiagramTextFile(String path);
    void generateSequenceDiagramImage(String path);
    void generateSequenceDiagram(String path);
}