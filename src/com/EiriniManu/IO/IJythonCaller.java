package com.EiriniManu.IO;

import com.EiriniManu.Parsing.DiagramStructure;

import java.io.InputStream;

public interface IJythonCaller {
    void createDiagramFile(InputStream stream, String path, DiagramStructure structure);
}
