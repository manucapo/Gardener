package com.EiriniManu.IO;

import java.io.InputStream;

public interface IJythonCaller {
    void createDiagramFile(InputStream stream, String path, DiagramStructure structure);
}
