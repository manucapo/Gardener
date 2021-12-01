package com.EiriniManu.Parsing.Parser;

import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Messaging.IMessageSender;
import com.EiriniManu.IO.DiagramStructure;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.SourceRoot;

public interface IJavaParser extends  IMessageSender, IMessageObserver {
    SourceRoot SetSourceRoot(String path, String packageName);
    void execute(String methodName, String className, String classFilePath, String packageName, DiagramStructure structure);
    CompilationUnit ParseFile(String fileName, SourceRoot sourceRoot);
    void ParseMethod(CompilationUnit cu, String className, String methodName, DiagramStructure diagramStructure);
    void parseMethodNode(Node methodcallNode);
    void addDependency(String dependency);
}
