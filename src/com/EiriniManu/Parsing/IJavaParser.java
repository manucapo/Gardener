package com.EiriniManu.Parsing;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;

public interface IJavaParser {
    SourceRoot SetSourceRoot(String path, String packageName);
    CompilationUnit ParseFile(String fileName, SourceRoot sourceRoot);
    void ParseMethod(CompilationUnit cu, String className, String methodName, DiagramStructure diagramStructure);
}
