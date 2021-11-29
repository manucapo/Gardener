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
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.printer.XmlPrinter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;
import org.python.antlr.op.Param;


import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

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

    public CompilationUnit ParseFile(String className, SourceRoot sourceRoot) {                    // Parse the code and return the AST
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());                     // Set the parser to log errors to standard out.
        CompilationUnit cu = sourceRoot.parse("", className + ".java");               // parse the file with a corresponding name in the root path.
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

                XmlPrinter printer = new XmlPrinter(true);
             //   System.out.println(printer.output(method));                                // DEBUG PRINTER


                for (Parameter node : method.getParameters()) {                       // extract method parameters
                    ParameterNodeExplorer nodeExplorer = (ParameterNodeExplorer) NodeExplorerFactory.create(Parameter.class);
                    String[] splitArray =  nodeExplorer.checkNode(node);
                    diagramStructure.addParameterType(splitArray[0]);         // first string should be type
                    diagramStructure.addParameterName(splitArray[1]);         // second string should be name
                }

                for (Node node : method.findAll(CatchClause.class, Node.TreeTraversal.PREORDER)) { // extract information on variables declared inside catch clause
                    CatchNodeExplorer nodeExplorer = (CatchNodeExplorer) NodeExplorerFactory.create(CatchClause.class);
                     List<String[]> typeNameList = nodeExplorer.checkNode(node);
                    for (String[] splitArray : typeNameList) {
                        diagramStructure.addCatchParameterTypes(splitArray[0]);
                        diagramStructure.addCatchParameterNames(splitArray[1]);
                    }
                }

                for (Node node : method.findAll(VariableDeclarationExpr.class, Node.TreeTraversal.PREORDER)) {  // extract information on variables inside method
                    VariableNodeExplorer nodeExplorer = (VariableNodeExplorer) NodeExplorerFactory.create(VariableDeclarationExpr.class);
                    String[] splitArray =  nodeExplorer.checkNode(node);
                    diagramStructure.addVariableDeclarationTypes(splitArray[0]);
                    diagramStructure.addVariableDeclarations(splitArray[1]);
                }

                for (Node node : method.findAll(MethodCallExpr.class, Node.TreeTraversal.PREORDER)) { // extract information on method calls inside method
                    MethodNodeExplorer nodeExplorer = (MethodNodeExplorer) NodeExplorerFactory.create(MethodCallExpr.class);

                    if (subMethodCounter == 0){
                        subMethodCounter = nodeExplorer.checkNode(node);
                        parseMethodNode(node, diagramStructure);
                    } else {subMethodCounter--;}

            }
        }
    }
    }



    public void parseMethodNode(Node methodcallNode, DiagramStructure structure) {
        System.out.println("--------------------TEST PARSER--------------------");
        System.out.println("ORIGINAL METHOD CALL : " + methodcallNode);

        XmlPrinter printer = new XmlPrinter(true);
        System.out.println(printer.output(methodcallNode));

        List<String> methodNameStack = new ArrayList<>();
        List<String> methodTargetStack = new ArrayList<>();
        List<String> methodTargetTypeNameStack = new ArrayList<>();

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

                    int catchParameterIndex = 0;
                    if (subMethod.findFirst(NameExpr.class).isPresent() && subMethod.findAncestor(CatchClause.class).isPresent()) {  // if method is in catch clause. check catch parameters
                        for (String methodParameter : structure.getCatchParameterNames()) {
                            if (methodParameter.equals(subMethod.findFirst(NameExpr.class).get().getNameAsString())) {
                                System.out.println("THIS IS A CATCH PARAMETER");
                                methodTargetStack.add(structure.getCatchParameterTypes().get(catchParameterIndex).replaceAll("<.*>", " "));
                                methodTargetTypeNameStack.add("");
                                targetFound = true;
                                break;
                            }
                            catchParameterIndex++;
                        }
                    }

                    int parameterIndex = 0;
                    if (subMethod.findFirst(NameExpr.class).isPresent()) {
                        for (String methodParameter : structure.getParameterNames()) {
                            if (methodParameter.equals(subMethod.findFirst(NameExpr.class).get().getNameAsString())) {
                                System.out.println("THIS IS A PARAMETER");
                                methodTargetStack.add(structure.getParameterType().get(parameterIndex).replaceAll("<.*>", " "));
                                methodTargetTypeNameStack.add("");
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
                            methodTargetTypeNameStack.add("");
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
                                methodTargetTypeNameStack.add("");
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
                        if (classField.equals(subMethod.findFirst(NameExpr.class).get().getNameAsString())) {
                            System.out.println("THIS IS A CLASS FIELD");
                            methodTargetStack.add(structure.getClassFieldTypes().get(classFieldIndex).replaceAll("<.*>", " "));
                            methodTargetTypeNameStack.add("");
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
                                methodTargetTypeNameStack.add("");
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
                            methodTargetTypeNameStack.add("");
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
                        } else if (previousTarget.equals("List")){                      // if last target was a list
                            if (methodNameStack.get(i + 1).equals("get")) {     // if last method was get
                                previousTarget = methodTargetTypeNameStack.get(methodTargetStack.size() - 1);
                                methodTargetStack.add(previousTarget);
                                targetFound = true;
                            }
                        } else if (previousTarget.equals("Optional")){                      // if last target was optional
                            if (methodNameStack.get(i + 1).equals("get")) {     // if last method was get
                                previousTarget = methodTargetTypeNameStack.get(methodTargetStack.size() - 1);
                                methodTargetStack.add(previousTarget);
                                targetFound = true;
                            }
                        }
                        lastClass = Class.forName(pkg + previousTarget);     // MUST CHECK FOR ALL PACKAGES
                        classFound = true;
                        break;
                    } catch (Exception e) {
                     // TODO
                    }

                }

                if (classFound && ! targetFound) {
                    for (Method method : lastClass.getDeclaredMethods()) {             // CHECK DECLARED METHODS
                        if (method.getName().equals(methodNameStack.get(i+1))) {
                            if (method.getReturnType().getSimpleName().replaceAll("<.*>", " ").equals("List") || method.getReturnType().getSimpleName().replaceAll("<.*>", " ").equals("Optional")){  // try to resolve some generic types
                                Type type = method.getGenericReturnType();

                                if(type instanceof  ParameterizedType){
                                    ParameterizedType pt = (ParameterizedType) type;
                                    String typeName = pt.getActualTypeArguments()[0].getTypeName().split("\\.")[pt.getActualTypeArguments()[0].getTypeName().split("\\.").length -1];

                                    for (Type str: pt.getActualTypeArguments()
                                         ) {
                                        System.out.println(str.getTypeName());
                                    }

                                    System.out.println("BINGO :" + typeName);
                                    methodTargetTypeNameStack.add(typeName);
                                }
                            } else {
                                methodTargetTypeNameStack.add("");
                            }
                            methodTargetStack.add(method.getReturnType().getSimpleName().replaceAll("<.*>", " "));
                            targetFound = true;
                            break;
                        }
                    }

                    if (!targetFound){                                             // CHECK INHERITED METHODS
                        for (Method method : lastClass.getMethods()) {
                            if (method.getName().equals(methodNameStack.get(i+1))) {

                                if (method.getReturnType().getSimpleName().replaceAll("<.*>", " ").equals("List") || method.getReturnType().getSimpleName().replaceAll("<.*>", " ").equals("Optional")){  // try to resolve some generic types
                                    Type type = method.getGenericReturnType();

                                    if(type instanceof  ParameterizedType){
                                        ParameterizedType pt = (ParameterizedType) type;
                                        String typeName = pt.getActualTypeArguments()[0].getTypeName().split("\\.")[pt.getActualTypeArguments()[0].getTypeName().split("\\.").length -1];
                                        methodTargetTypeNameStack.add(typeName);
                                    }
                                } else {
                                    methodTargetTypeNameStack.add("");
                                }
                                methodTargetStack.add(method.getReturnType().getSimpleName().replaceAll("<.*>", " "));
                                targetFound = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (!targetFound) {
                System.out.println("COULD NOT RESOLVE ANY TARGETS");

                if (false){                     // SAFE MODE
                    methodNameStack.remove(i);
                } else if (true) {                       // LOST MESSAGE MODE
                    methodTargetStack.add("LOSTMESSAGE");
                } else {
                    methodTargetStack.add("ERROR");
                }



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
