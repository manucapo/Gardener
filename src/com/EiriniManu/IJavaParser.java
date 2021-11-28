package com.EiriniManu;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import org.python.antlr.ast.Str;

public interface IJavaParser {
    SourceRoot SetSourceRoot(String path, String packageName);
    CompilationUnit ParseFile(String fileName, SourceRoot sourceRoot);
    void ParseMethodFromClass(CompilationUnit cu, String className, String methodName, DiagramStructure diagramStructure);
}
