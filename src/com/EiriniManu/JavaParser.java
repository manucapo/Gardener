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
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;

import java.util.Optional;

public class JavaParser implements IJavaParser {

    public JavaParser() {
        //TODO CONSTRUC

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

                    for (Parameter param : method.getParameters()) {
                        String[] splitArray = param.toString().split(" ");   // split parameter node name by space

                        diagramStructure.addParameterType(splitArray[0]);         // first string should be type
                        diagramStructure.addParameterName(splitArray[1]);         // second string should be name
                    }

                    for (Node node: method.findAll(VariableDeclarationExpr.class, Node.TreeTraversal.PREORDER)){  // extract information on variables inside method
                            checkVariableNode(node, diagramStructure);
                    }

                    for (Node node : method.findAll(MethodCallExpr.class,Node.TreeTraversal.PREORDER )) { // extract information on method calls inside method
                        checkMethodCallNode(node, diagramStructure);}
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR PARSING JAVA FILE");
            System.out.println(e.toString());
        }
    }

    public void checkVariableNode(Node node, DiagramStructure structure){
        String[] splitArray = node.toString().split(" ");                    // split variable declaration by spaces
        structure.addVariableDeclarationTypes(splitArray[0]);                     // the first string should be the type
        structure.addVariableDeclarations(splitArray[1]);                         // the second string should be the variable name
    }

    public void checkMethodCallNode(Node node, DiagramStructure structure) {

            System.out.println("--------------------------------");
            System.out.println(node.toString());
            parseMethodCallString(node.toString(), structure);

            if (!node.findAncestor(IfStmt.class).equals(Optional.empty()))
            {
              // System.out.println( node.findAncestor(IfStmt.class).get().getElseStmt());
                System.out.println("THIS MEHTOD IS INSIDE AN IF BLOCK");
            }
            System.out.println("--------------------------------");

    }

    public void parseMethodCallString(String methodCall, DiagramStructure structure){

       methodCall = methodCall.replaceAll("\\([^()]*\\)", "");  // remove parameters  GOTTA CHECK THIS FOR NESTED BRACKETS
        System.out.println("Method call withouht params : " + methodCall);
        String[] splitArray2 =  methodCall.split("\\."); // split the call without parameters by . dot
        structure.addMethodCall(splitArray2[splitArray2.length -1]);        // the last string in the array should be the actual call

        boolean foundClassMethod = false;
        for (String classMethod: structure.getClassMethodNames()){
            if (classMethod.equals(splitArray2[0])){
            System.out.println("THIS IS A CLASS MEHTOD");
            foundClassMethod = true;
            }
        }
        boolean foundVariable = false;
        int variableIndex = 0;

        for (String classMethod: structure.getVariableDeclarations()){
            if (classMethod.equals(splitArray2[0])){
                System.out.println("THIS IS A VARIABLE");
                foundVariable = true;
                break;
            }
            variableIndex++;
        }

        boolean foundParameter = false;
        int parameterIndex = 0;
        for (String methodParameter : structure.getParameterNames()){
            if (methodParameter.equals(splitArray2[0])){
                System.out.println("THIS IS A PARAMETER");
                foundParameter = true;
                break;
            }
            parameterIndex++;
        }

        if(foundClassMethod){
            structure.addMethodCallTarget("this");
        } else if (foundVariable){
           structure.addMethodCallTarget(structure.getVariableDeclarationTypes().get(variableIndex));
        } else if (foundParameter){
            structure.addMethodCallTarget(structure.getParameterType().get(parameterIndex));
        } else {
            structure.addMethodCallTarget(splitArray2[0]);
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
