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
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JavaParser implements IJavaParser {

    private int subMethodCounter;
    private List<String> packageDependencies;

    public JavaParser() {
        subMethodCounter = 0;
        packageDependencies = new ArrayList<>();
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

        ClassOrInterfaceDeclaration clsX = new ClassOrInterfaceDeclaration();                 // Holder variable for the class declaration node.

        if (cu.getClassByName(className).isPresent()) {                                      // Check if class with corresponding name exists. :
            clsX = cu.getClassByName(className).get();                                       // if yes hold the class declaration node
        }

        for (MethodDeclaration method : clsX.getMethods()) {                                 // for every method declaration node in the class :
            if (method.getName().toString().equals(methodName)) {                            // Find method with given name

                for (Parameter node : method.getParameters()) {
                    checkParameterNode(node, diagramStructure);
                }

                for (Node node : method.findAll(VariableDeclarationExpr.class, Node.TreeTraversal.PREORDER)) {  // extract information on variables inside method
                    checkVariableNode(node, diagramStructure);
                }
                for (Node node : method.findAll(MethodCallExpr.class, Node.TreeTraversal.PREORDER)) { // extract information on method calls inside method
                    checkMethodCallNode(node, diagramStructure, cu);
                }
            }
        }
    }

    public void checkParameterNode(Node node, DiagramStructure structure) {
        String[] splitArray = node.toString().split(" ");   // split parameter node name by space
        structure.addParameterType(splitArray[0]);         // first string should be type
        structure.addParameterName(splitArray[1]);         // second string should be name
    }

    public void checkVariableNode(Node node, DiagramStructure structure) {
        String[] splitArray = node.toString().split(" ");                    // split variable declaration by spaces
        structure.addVariableDeclarationTypes(splitArray[0]);                     // the first string should be the type
        structure.addVariableDeclarations(splitArray[1]);                         // the second string should be the variable name
    }

    public void checkMethodCallNode(Node node, DiagramStructure structure, CompilationUnit cu) {

        System.out.println("--------------------------------");
        System.out.println(node.toString());

        if (subMethodCounter == 0) {           // Skip the process once for each sub method found
            boolean foundNestedMethodCall = false;

            for (Node subNode : node.findAll(MethodCallExpr.class, Node.TreeTraversal.PREORDER)) {  // check for nested method calls
                if (subNode != node) {                                                               // ignore original node
                    System.out.println("THERE IS A SUBNODE " + subNode.toString());
                    subMethodCounter += 1;
                    foundNestedMethodCall = true;
                }
            }

            if (!foundNestedMethodCall) {
                parseMethodCallString(node.toString(), structure);
                System.out.println("PARSING METHOD");
            } else {
                parseNestedMethodCall(node.toString(), subMethodCounter, structure);
                System.out.println("PARSING METHOD STACK");
            }

            if (!node.findAncestor(IfStmt.class).equals(Optional.empty())) {
                // System.out.println( node.findAncestor(IfStmt.class).get().getElseStmt());
                System.out.println("THIS MEHTOD IS INSIDE AN IF BLOCK");
            }
            System.out.println("--------------------------------");
        } else {
            subMethodCounter--;        // decrement submethod counter
        }

    }

    public void parseNestedMethodCall(String nestedMethodCall, int subMethodCounter, DiagramStructure structure) {

        boolean containsParameters = true;
        while (containsParameters) {
            nestedMethodCall = nestedMethodCall.replaceAll("\\([^()]*\\)", "");  // remove parameters  GOTTA CHECK THIS FOR NESTED BRACKETS

            if (nestedMethodCall.contains("(") || nestedMethodCall.contains(")")) {
                nestedMethodCall = nestedMethodCall.replaceAll("\\([^()]*\\)", "");  // remove parameters  GOTTA CHECK THIS FOR NESTED BRACKETS
            } else {
                containsParameters = false;
            }
        }
        System.out.println("AFTER " + nestedMethodCall);
        String[] splitArray = nestedMethodCall.split("\\."); // split the call without parameters by . dot

        for (String string : splitArray) {
            System.out.println(string);

        }

        parseMethodCallString(splitArray[0], structure);
        String lastClassName = "";

        for (int i = 1; i <= subMethodCounter; i++) {
            System.out.println(subMethodCounter);
            //   System.out.println("XXXXXXXXXX -- "+ splitArray[i-1] + "."  + splitArray[i]);
            structure.addMethodCall(splitArray[i]);

            boolean foundClassMethod = false;
            int methodIndex = 0;
            for (String classMethod : structure.getClassMethodNames()) {
                if (classMethod.equals(splitArray[i - 1])) {
                    System.out.println("THIS IS A CLASS METHOD");
                    foundClassMethod = true;
                    break;
                }
                methodIndex++;
            }
            boolean foundVariable = false;
            int variableIndex = 0;

            for (String classMethod : structure.getVariableDeclarations()) {
                if (classMethod.equals(splitArray[i - 1])) {
                    System.out.println("THIS IS A VARIABLE");
                    foundVariable = true;
                    break;
                }
                variableIndex++;
            }

            boolean foundParameter = false;
            int parameterIndex = 0;
            for (String methodParameter : structure.getParameterNames()) {
                if (methodParameter.equals(splitArray[i - 1])) {
                    System.out.println("THIS IS A PARAMETER");
                    foundParameter = true;
                    break;
                }
                parameterIndex++;
            }

            boolean foundClassField = false;
            int classFieldIndex = 0;

            for (String classField : structure.getClassFieldNames()) {
                if (classField.equals(splitArray[i - 1])) {
                    System.out.println("THIS IS A CLASS FIELD");
                    foundClassField = true;
                    break;
                }
                classFieldIndex++;
            }

            if (foundClassMethod) {
                lastClassName = structure.getClassMethodReturnTypes().get(methodIndex);
                structure.addMethodCallTarget(lastClassName);

            } else if (foundVariable) {
                lastClassName = structure.getVariableDeclarationTypes().get(variableIndex);
                structure.addMethodCallTarget(lastClassName);
            } else if (foundParameter) {
                lastClassName = structure.getParameterType().get(parameterIndex);
                structure.addMethodCallTarget(lastClassName);
            } else if (foundClassField) {
                lastClassName = structure.getClassFieldTypes().get(classFieldIndex);
                structure.addMethodCallTarget(lastClassName);
            } else {                                                                     // if all else fails try to find method in class in package

                boolean classFound = false;
                Class<?> lastClass = null;
                for (String pkg : packageDependencies) {

                    try {
                        lastClass = Class.forName(pkg + lastClassName);     // MUST CHECK FOR ALL PACKAGES
                        classFound = true;
                        break;
                    } catch (Exception e) {
                        System.out.println("CHECKING NEXT PACKAGE");
                    }

                }
                if (classFound) {
                    for (Method method : lastClass.getMethods()) {
                        if (method.getName().equals(splitArray[i - 1])) {
                            structure.addMethodCallTarget(method.getReturnType().getSimpleName());
                        }
                    }
                } else {
                    System.out.println("COULD NOT RESOLVE ANY CLASSES");
                    structure.addMethodCallTarget("cantfindmethodtargetERROR");

                }
            }
        }

    }

    public void parseMethodCallString(String methodCall, DiagramStructure structure) {

        methodCall = methodCall.replaceAll("\\([^()]*\\)", "");  // remove parameters  GOTTA CHECK THIS FOR NESTED BRACKETS
        String[] splitArray2 = methodCall.split("\\."); // split the call without parameters by . dot
        structure.addMethodCall(splitArray2[splitArray2.length - 1]);        // the last string in the array should be the actual call

        boolean foundClassMethod = false;
        for (String classMethod : structure.getClassMethodNames()) {
            if (classMethod.equals(splitArray2[0])) {
                System.out.println("THIS IS A CLASS MEHTOD");
                foundClassMethod = true;
            }
        }
        boolean foundVariable = false;
        int variableIndex = 0;

        for (String classMethod : structure.getVariableDeclarations()) {
            if (classMethod.equals(splitArray2[0])) {
                System.out.println("THIS IS A VARIABLE");
                foundVariable = true;
                break;
            }
            variableIndex++;
        }

        boolean foundParameter = false;
        int parameterIndex = 0;
        for (String methodParameter : structure.getParameterNames()) {
            if (methodParameter.equals(splitArray2[0])) {
                System.out.println("THIS IS A PARAMETER");
                foundParameter = true;
                break;
            }
            parameterIndex++;
        }

        boolean foundClassField = false;
        int classFieldIndex = 0;
        for (String classField : structure.getClassFieldNames()) {
            System.out.println("CLASS FIELD FINDING AT " + classField);
            if (classField.equals(splitArray2[0])) {
                System.out.println("THIS IS A CLASS FIELD");
                foundClassField = true;
                break;
            }
            classFieldIndex++;
        }


        if (foundClassMethod) {
            structure.addMethodCallTarget("this");
        } else if (foundVariable) {
            structure.addMethodCallTarget(structure.getVariableDeclarationTypes().get(variableIndex));
        } else if (foundParameter) {
            structure.addMethodCallTarget(structure.getParameterType().get(parameterIndex));
        } else if (foundClassField) {
            structure.addMethodCallTarget(structure.getClassFieldTypes().get(classFieldIndex));
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

    // GETTERS AND SETTERS


    public List<String> getPackageDependencies() {
        return packageDependencies;
    }

    public void setPackageDependencies(List<String> packageDependencies) {
        this.packageDependencies = packageDependencies;
    }

    public void addPackageDependencies(String packageDependency) {
        this.packageDependencies.add(packageDependency);
    }

}
