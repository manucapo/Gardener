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
import com.github.javaparser.printer.XmlPrinter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;


import java.lang.reflect.Field;
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
                    //  checkMethodCallNode(node, diagramStructure, cu);
                    checkMethodNodeTest(node, diagramStructure, cu);
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


    public void checkMethodNodeTest(Node node, DiagramStructure structure, CompilationUnit cu) {
        if (subMethodCounter == 0) {           // Skip the process once for each sub method found
            boolean foundNestedMethodCall = false;

            for (Node subNode : node.findAll(MethodCallExpr.class, Node.TreeTraversal.PREORDER)) {  // check for nested method calls
                if (subNode != node) {                                                               // ignore original node
                    subMethodCounter += 1;
                    foundNestedMethodCall = true;
                }
            }
            parseMethodNodeTest(node, subMethodCounter, structure, " ");

        } else {
            subMethodCounter--;        // decrement submethod counter
        }
    }

    public String parseMethodNodeTest(Node methodcallNode, int subMethodCounter, DiagramStructure structure, String lastClassName) {
        System.out.println("--------------------TEST PARSER--------------------");
        System.out.println("ORIGINAL METHOD CALL : " + methodcallNode);

        XmlPrinter printer = new XmlPrinter(true);
      //  System.out.println(printer.output(methodcallNode));

        List<String> methodNameStack = new ArrayList<>();
        List<String> methodTargetStack = new ArrayList<>();

        List<MethodCallExpr> containedMethods = methodcallNode.findAll(MethodCallExpr.class, Node.TreeTraversal.BREADTHFIRST);

        for (int i = 0; i < containedMethods.size(); i++) {
            Node subMethod = containedMethods.get(i);
            String subMethodName = subMethod.findAll(SimpleName.class, Node.TreeTraversal.BREADTHFIRST).get(0).toString();
            methodNameStack.add(subMethodName);
        }

        for (int i = containedMethods.size() - 1; i >= 0; i--) {
            Node subMethod = containedMethods.get(i);
            String subMethodName = subMethod.findAll(SimpleName.class, Node.TreeTraversal.BREADTHFIRST).get(0).toString();

            boolean targetFound = false;
            boolean classFound = false;
            Class<?> lastClass = null;

            if (i == containedMethods.size() - 1) {
                if (!targetFound) {

                    int parameterIndex = 0;
                    if (subMethod.findFirst(NameExpr.class).isPresent()) {
                        for (String methodParameter : structure.getParameterNames()) {
                            if (methodParameter.equals(subMethod.findFirst(NameExpr.class).get().getNameAsString())) {
                                System.out.println("THIS IS A PARAMETER");
                                methodTargetStack.add(structure.getParameterType().get(parameterIndex).replaceAll("<.*>", " "));
                                targetFound = true;
                                break;
                            }
                            parameterIndex++;
                        }
                    }
                }
                if (!targetFound) {
                    for (String classMethod : structure.getClassMethodNames()) {
                        if (classMethod.equals(subMethodName)) {
                            System.out.println("THIS IS A CLASS MEHTOD");
                            methodTargetStack.add(structure.getImplementingClassName());
                            targetFound = true;
                            break;
                        }
                    }
                }


                if (!targetFound) {

                    int variableIndex = 0;
                    if (subMethod.findFirst(NameExpr.class).isPresent()) {
                        for (String classMethod : structure.getVariableDeclarations()) {
                            if (classMethod.equals(subMethod.findFirst(NameExpr.class).get().getNameAsString())) {
                                System.out.println("THIS IS A VARIABLE");
                                methodTargetStack.add(structure.getVariableDeclarationTypes().get(variableIndex).replaceAll("<.*>", " "));
                                targetFound = true;
                                break;
                            }
                            variableIndex++;
                        }

                    }
                }

                if (!targetFound) {
                    int classFieldIndex = 0;
                    for (String classField : structure.getClassFieldNames()) {
                        System.out.println("CLASS FIELD FINDING AT " + classField);
                        if (classField.equals(subMethodName)) {
                            System.out.println("THIS IS A CLASS FIELD");
                            methodTargetStack.add(structure.getClassFieldTypes().get(classFieldIndex).replaceAll("<.*>", " "));
                            targetFound = true;
                            break;
                        }
                        classFieldIndex++;
                    }
                }

                if (subMethod.findFirst(NameExpr.class).isPresent()) {
                        for (String pkg : packageDependencies) {

                            try {
                                lastClass = Class.forName(pkg + subMethod.findFirst(NameExpr.class).get().getNameAsString());     // MUST CHECK FOR ALL PACKAGES
                                methodTargetStack.add(lastClass.getSimpleName().replaceAll("<.*>", " "));
                                targetFound = true;
                                break;
                            } catch (Exception e) {
                                        //TODO
                            }
                        }
                }
            }

                if (!targetFound) {

                    if (subMethod.findFirst(FieldAccessExpr.class).isPresent()) {
                        Node fieldAcess = subMethod.findFirst(FieldAccessExpr.class).get();
                        if (fieldAcess.findFirst(NameExpr.class).isPresent()) {
                            methodTargetStack.add(fieldAcess.findFirst(NameExpr.class).get().getNameAsString());
                            targetFound = true;
                        }
                    }
                }



            if (!targetFound) {

                for (String pkg : packageDependencies) {
                    try {
                        String previousTarget = methodTargetStack.get(methodTargetStack.size() - 1);
                        if (previousTarget.contains("[")){
                           previousTarget = "Object";          // if last target was an array
                        }
                        lastClass = Class.forName(pkg + previousTarget);     // MUST CHECK FOR ALL PACKAGES
                        classFound = true;
                        break;
                    } catch (Exception e) {
                     // TODO
                    }

                }
                if (classFound) {
                    for (Method method : lastClass.getDeclaredMethods()) {
                        if (method.getName().equals(methodNameStack.get(i+1))) {
                            methodTargetStack.add(method.getReturnType().getSimpleName().replaceAll("<.*>", " "));
                           // methodTargetStack.add(lastClass.getSimpleName());
                            break;
                        }
                    }
                }

            }

            if (!targetFound && !classFound) {
                System.out.println("COULD NOT RESOLVE ANY TARGETS");
                methodTargetStack.add("cantfindmethodtargetERROR");

            }
            System.out.println("RELOOPING -----------------------------");
        }

        for (int i = methodNameStack.size() - 1; i >= 0; i--) {
            System.out.println("NAME : " + methodNameStack.get(i));
            structure.addMethodCall(methodNameStack.get(i));                                                 // first contained name should be method name
        }

        for (int i = 0; i <= methodTargetStack.size() - 1; i++) {
            System.out.println("TARGET : " + methodTargetStack.get(i));
            structure.addMethodCallTarget(methodTargetStack.get(i));                                                 // first contained name should be method name
        }


        return "cantfindmethodtargetERROR";
    }

    public void parseMethodCallString(String methodCall, DiagramStructure structure) {

        methodCall = removeParametersFromMethodCall(methodCall);
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
            structure.addMethodCallTarget(structure.getVariableDeclarationTypes().get(variableIndex).replaceAll("<.*>", " "));
        } else if (foundParameter) {
            structure.addMethodCallTarget(structure.getParameterType().get(parameterIndex).replaceAll("<.*>", " "));
        } else if (foundClassField) {
            structure.addMethodCallTarget(structure.getClassFieldTypes().get(classFieldIndex).replaceAll("<.*>", " "));
        } else {
            System.out.println("KKKKKKKKKKKKK" + splitArray2[0]);
            structure.addMethodCallTarget(splitArray2[0].replaceAll("<.*>", " "));
        }

    }


    private String removeParametersFromMethodCall(String nestedMethodCall) {
        boolean containsParameters = true;
        while (containsParameters) {
            if (nestedMethodCall.contains("(") || nestedMethodCall.contains(")")) {
                nestedMethodCall = nestedMethodCall.replaceAll("\\([^()]*\\)", "");  // remove parameters  GOTTA CHECK THIS FOR NESTED BRACKETS
            } else {
                containsParameters = false;
            }
        }
        return nestedMethodCall;
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
