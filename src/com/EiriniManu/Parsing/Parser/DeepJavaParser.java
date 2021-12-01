package com.EiriniManu.Parsing.Parser;

import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Messaging.MessageTag;
import com.EiriniManu.Parsing.*;
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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeepJavaParser extends SafeJavaParser{

    public DeepJavaParser() {

        packageDependencies = new ArrayList<>();
        catchParameterTypes = new ArrayList<>();
        catchParameterNames = new ArrayList<>();
        parameterNames = new ArrayList<>();
        parameterTypes = new ArrayList<>();
        classMethodNames = new ArrayList<>();
        implementingClassName = "NULL";
        variableDeclarationNames = new ArrayList<>();
        variableDeclarationTypes = new ArrayList<>();
        classFieldNames = new ArrayList<>();
        classFieldTypes = new ArrayList<>();
        observerList = new ArrayList<>();

    }

    public void parseMethodNode(Node methodcallNode) {
        System.out.println("--------------------TEST PARSER--------------------");
        System.out.println("ORIGINAL METHOD CALL : " + methodcallNode);

        XmlPrinter printer = new XmlPrinter(true);
        System.out.println(printer.output(methodcallNode));

        List<String> methodNameStack = new ArrayList<>();
        List<String> methodTargetStack = new ArrayList<>();
        List<String> methodTargetTypeNameStack = new ArrayList<>();

        List<MethodCallExpr> containedMethods = methodcallNode.findAll(MethodCallExpr.class, Node.TreeTraversal.BREADTHFIRST);

        for (int i = 0; i < containedMethods.size(); i++) {
            MethodCallExpr subMethod = containedMethods.get(i);
            String subMethodName = subMethod.findAll(SimpleName.class, Node.TreeTraversal.BREADTHFIRST).get(0).toString();
            methodNameStack.add(subMethodName);
        }

        Node previousScope = null;

        for (int i = containedMethods.size() - 1; i >= 0; i--) {
            boolean targetFound = false;
            boolean classFound = false;
            Class<?> lastClass = null;

            MethodCallExpr subMethod = containedMethods.get(i);
            String subMethodName = subMethod.findAll(SimpleName.class, Node.TreeTraversal.BREADTHFIRST).get(0).toString();

            Node scope = null;
            if (subMethod.getScope().isPresent()) {                       // get the nearest sub method scope. By removing previous scope from current scope
                scope = subMethod.getScope().get();
                if (previousScope != null) {
                    scope.remove(previousScope);
                }
                System.out.println("DEBUGING SCOPE : " + scope);
            }

            for (String classMethod : classMethodNames) {                   // check if node is a class method
                if (classMethod.equals(subMethodName)) {
                    System.out.println("THIS IS A CLASS MEHTOD");
                    methodTargetStack.add(implementingClassName);
                    methodTargetTypeNameStack.add("");
                    targetFound = true;
                    break;
                }
            }


            if (scope != null) {
                if (!targetFound) {

                    int catchParameterIndex = 0;
                    if (scope.findFirst(SimpleName.class).isPresent() && subMethod.findAncestor(CatchClause.class).isPresent()) {  // if method is in catch clause. check catch parameters
                        for (String methodCatchParameter : catchParameterNames) {
                            if (methodCatchParameter.equals(scope.findFirst(SimpleName.class).get().toString())) {
                                System.out.println("THIS IS A CATCH PARAMETER");
                                methodTargetStack.add(catchParameterTypes.get(catchParameterIndex).replaceAll("<.*>", " "));
                                methodTargetTypeNameStack.add("");
                                targetFound = true;
                                break;
                            }
                            catchParameterIndex++;
                        }
                    }
                }

                if (!targetFound) {
                    int parameterIndex = 0;
                    if (scope.findFirst(SimpleName.class).isPresent()) {
                        for (String methodParameter : parameterNames) {
                            if (methodParameter.equals(scope.findFirst(SimpleName.class).get().toString())) {
                                System.out.println("THIS IS A PARAMETER");
                                methodTargetStack.add(parameterTypes.get(parameterIndex).replaceAll("<.*>", " "));
                                methodTargetTypeNameStack.add("");
                                targetFound = true;
                                break;
                            }
                            parameterIndex++;
                        }
                    }
                }


                if (!targetFound) {
                    int variableIndex = 0;
                    if (scope.findFirst(SimpleName.class).isPresent()) {
                        for (String classMethod : variableDeclarationNames) {
                            if (classMethod.equals(scope.findFirst(SimpleName.class).get().toString())) {
                                System.out.println("THIS IS A VARIABLE");
                                methodTargetStack.add(variableDeclarationTypes.get(variableIndex).replaceAll("<.*>", " "));
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
                    if (scope.findFirst(SimpleName.class).isPresent()) {
                        for (String classField : classFieldNames) {
                            System.out.println("CLASS FIELD FINDING AT " + classField);
                            if (classField.equals(scope.findFirst(SimpleName.class).get().toString())) {
                                System.out.println("THIS IS A CLASS FIELD");
                                methodTargetStack.add(classFieldTypes.get(classFieldIndex).replaceAll("<.*>", " "));
                                methodTargetTypeNameStack.add("");
                                targetFound = true;
                                break;
                            }
                            classFieldIndex++;
                        }
                    }
                }

                if (!targetFound) {

                    if (scope.findFirst(FieldAccessExpr.class).isPresent()) {
                        Node fieldAcess = scope.findFirst(FieldAccessExpr.class).get();
                        if (fieldAcess.findFirst(NameExpr.class).isPresent()) {
                            methodTargetStack.add(fieldAcess.findFirst(NameExpr.class).get().getNameAsString());
                            methodTargetTypeNameStack.add("");
                            targetFound = true;
                        }
                    }
                }
            }


            if (subMethod.findFirst(NameExpr.class).isPresent()) {         // try to resolve method target by using last class
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


            if (!targetFound) {                                             // try to resolve method if last class was a container class
                for (String pkg : packageDependencies) {
                    try {
                        String previousTarget = methodTargetStack.get(methodTargetStack.size() - 1);
                        if (previousTarget.contains("[")) {
                            previousTarget = "Object";          // if last target was an array
                        } else if (previousTarget.equals("List")) {                      // if last target was a list
                            if (methodNameStack.get(i + 1).equals("get")) {     // if last method was get
                                previousTarget = methodTargetTypeNameStack.get(methodTargetStack.size() - 1);
                                methodTargetStack.add(previousTarget);
                                targetFound = true;
                            }
                        } else if (previousTarget.equals("Optional")) {                      // if last target was optional
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

                if (classFound && !targetFound) {                                            // If class inside container was found try to resolve
                    for (Method method : lastClass.getDeclaredMethods()) {             // CHECK DECLARED METHODS
                        if (method.getName().equals(methodNameStack.get(i + 1))) {
                            if (method.getReturnType().getSimpleName().replaceAll("<.*>", " ").equals("List") || method.getReturnType().getSimpleName().replaceAll("<.*>", " ").equals("Optional")) {  // try to resolve some generic types
                                Type type = method.getGenericReturnType();

                                if (type instanceof ParameterizedType) {
                                    ParameterizedType pt = (ParameterizedType) type;
                                    String typeName = pt.getActualTypeArguments()[0].getTypeName().split("\\.")[pt.getActualTypeArguments()[0].getTypeName().split("\\.").length - 1];

                                    for (Type str : pt.getActualTypeArguments()
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

                    if (!targetFound) {                                             // CHECK INHERITED METHODS
                        for (Method method : lastClass.getMethods()) {
                            if (method.getName().equals(methodNameStack.get(i + 1))) {

                                if (method.getReturnType().getSimpleName().replaceAll("<.*>", " ").equals("List") || method.getReturnType().getSimpleName().replaceAll("<.*>", " ").equals("Optional")) {  // try to resolve some generic types
                                    Type type = method.getGenericReturnType();

                                    if (type instanceof ParameterizedType) {
                                        ParameterizedType pt = (ParameterizedType) type;
                                        String typeName = pt.getActualTypeArguments()[0].getTypeName().split("\\.")[pt.getActualTypeArguments()[0].getTypeName().split("\\.").length - 1];
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


                if (false) {                     // SAFE MODE
                    methodNameStack.remove(i);
                } else if (true) {                       // LOST MESSAGE MODE
                    methodTargetStack.add("LOSTMESSAGE");
                } else {
                    methodTargetStack.add("ERROR");
                }


            }

            previousScope = scope;
            System.out.println("RELOOPING -----------------------------");
        }

        for (int i = methodNameStack.size() - 1; i >= 0; i--) {
            System.out.println("NAME : " + methodNameStack.get(i));

            Object[] type = {MessageTag.METHODCALL, methodNameStack.get(i)};
            sendMessage(type);

            //    structure.addMethodCall(methodNameStack.get(i));                                                 // first contained name should be method name

        }

        for (int i = 0; i <= methodTargetStack.size() - 1; i++) {
            System.out.println("TARGET : " + methodTargetStack.get(i));

            Object[] type = {MessageTag.METHODCALLTARGET, methodTargetStack.get(i)};
            sendMessage(type);
            //
            // structure.addMethodCallTarget(methodTargetStack.get(i));                                                 // first contained name should be method name
        }
    }
}
