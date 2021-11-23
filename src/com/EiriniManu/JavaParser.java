package com.EiriniManu;

/*
    This class represents an object that can create an AST (abstract symbol tree) from java source code and pass the resulting tree for information.
    This is the second layer of information extraction in our process.
*/

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;

import java.util.Optional;

public class JavaParser implements IJavaParser {

    public JavaParser() {
        //TODO CONSTRUCTOR
    }

    public SourceRoot SetSourceRoot(String path, String packageName) {                           // Set a Root path for the source code. Needed by the parser.
        return new SourceRoot(CodeGenerationUtils.packageAbsolutePath(path, packageName));
    }

    public CompilationUnit ParseFile(String fileName, SourceRoot sourceRoot) {                    // Parse the code and return the AST
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());                     // Set the parser to log errors to standard out.
        CompilationUnit cu = sourceRoot.parse("", fileName);               // parse the file with a corresponding name in the root path.
        Log.info("DONE PARSING");
        return cu;
    }

    public void ParseMethodFromClass(CompilationUnit cu, String className, String methodName, DiagramStructure diagramStructure) {
        try {
            ClassOrInterfaceDeclaration clsX = new ClassOrInterfaceDeclaration();                 // Holder variable for the class declaration node.

            if (cu.getClassByName(className).isPresent()) {                                      // Check if class with corresponding name exists. :
                clsX = cu.getClassByName(className).get();                                       // if yes hold the class declaration node
            }

            for (MethodDeclaration method : clsX.getMethods()) {                                 // for every method declaration node in the class :
                if (method.getName().toString().equals(methodName)) {                            // Find method with given name
                    System.out.println(method.getName().toString());


                    for (Node node : method.findAll(MethodCallExpr.class,Node.TreeTraversal.PREORDER )) {
                        checkMethodCallNode(node, diagramStructure);}
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR PARSING JAVA FILE");
            System.out.println(e.toString());
        }
    }

    public void checkMethodCallNode(Node node, DiagramStructure structure) {                 //  Check if node is a Method Call.
        if (node instanceof MethodCallExpr) {
            System.out.println("--------------------------------");
            System.out.println(node.toString());
            structure.addMethodCall(node.toString());

            if (!node.findAncestor(IfStmt.class).equals(Optional.empty()))
            {
              // System.out.println( node.findAncestor(IfStmt.class).get().getElseStmt());
                System.out.println("THIS MEHTOD IS INSIDE AN IF BLOCK");
            }
            System.out.println("--------------------------------");
        }
    }

    // The Classes below are part of the JavaParser API and can be used to "visit" and operate on the nodes of the AST.  https://www.tutorialspoint.com/design_pattern/visitor_pattern.htm

    static class MethodVisitor extends VoidVisitorAdapter<Void> {    // Visitor class that checks MethodCall nodes.
        @Override
        public void visit(MethodCallExpr n, Void arg) {
            System.out.println(n.getName());
            super.visit(n, arg);
        }
    }

    static class ParamVisitor extends VoidVisitorAdapter<Void> {       // Visitor class that checks Parameters
        @Override
        public void visit(Parameter n, Void arg) {
             System.out.println(n.getName());
             super.visit(n, arg);
        }
    }

}
