package com.EiriniManu.Parsing.Parser;

import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Messaging.IMessageSender;
import com.EiriniManu.IO.DiagramStructure;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.SourceRoot;

/*
    This interface defines an object that can create an AST (abstract symbol tree) from java source code and parse the resulting tree for information.
*/


public interface IJavaParser extends  IMessageSender, IMessageObserver {

    void execute(String methodName, String className, String classFilePath, String packageName, DiagramStructure structure);
    CompilationUnit ParseFile(String fileName, SourceRoot sourceRoot);
    void parseMethod(CompilationUnit cu, String className, String methodName, DiagramStructure diagramStructure);
    void parseMethodNode(Node methodcallNode);
    void addDependency(String dependency);
}
