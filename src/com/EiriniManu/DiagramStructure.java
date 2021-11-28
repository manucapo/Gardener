package com.EiriniManu;

/*
    This class represents a data structure that contains all information needed by the plantUML diagram to generate the desired UML sequence diagram
    Any information extracted from a method should be reflected on this "structure".
    Add information here as necessary. Keeping the information in Strings is a good idea because the information will eventually be mapped to
    Strings in a text file.
 */

import java.util.ArrayList;
import java.util.List;

public class DiagramStructure {


    private String implementingClassName;         // Name of class where method is implemented
    private String callingClassName;              // Name of class that calls a method (must it be given by user ?)
    private List<String> classMethodNames;        // A list of names of every method implemented in the implementing class
    private List<String> classMethodReturnTypes;        // A list of names of every method return type  in the implementing class
    private List<String> classFieldNames;              // A list of names of every declared field  in the implementing class
    private List<String> classFieldTypes;            // A list of names of every declared field TYPE in the implementing class
    private String methodName;                    // The name of the method the user is interested in
    private String methodReturnType;                  // the return Type of the method
    private List<String> parameterType;      //  A list of names of every parameter TYPE that belong to the method the user is interested in
    private List<String> parameterNames;      // A list of names of every parameter that belongs to the method
    private List<String> methodCalls;             //  A list of methodCalls made by the method  ( 1 layer of calls atm)
    private List<String> methodCallTargets;       // The target classes of the method calls
    private List<String> variableDeclarations;     //  A list of variable declarations made by the method  ( 1 layer of calls atm)
    private List<String> variableDeclarationTypes; //  The types of variable declarations made by the method  ( 1 layer of calls atm)

    public DiagramStructure() {                    // Default constructor. Initializes the class to "safe" values.
        implementingClassName = "NULL";           // The string "NULL" was chosen as default as a class or method could never be called "NULL". Allowing error detection
        callingClassName = "NULL";
        classMethodNames = new ArrayList<>();     // Are ArrayLists the best data structure for our lists of Strings?
        classMethodReturnTypes = new ArrayList<>();
        methodName = "NULL";
        methodReturnType = "NULL";
        parameterType = new ArrayList<>();
        methodCalls = new ArrayList<>();
        methodCallTargets = new ArrayList<>();
        variableDeclarations = new ArrayList<>();
        variableDeclarationTypes = new ArrayList<>();
        parameterNames = new ArrayList<>();
        parameterType = new ArrayList<>();
        classFieldNames = new ArrayList<>();
        classFieldTypes = new ArrayList<>();
    }

    // Getters and setters
    public String getImplementingClassName() {
        return implementingClassName;
    }

    public void setImplementingClassName(String implementingClassName) {
        this.implementingClassName = implementingClassName;
    }

    public String getCallingClassName() {
        return callingClassName;
    }

    public void setCallingClassName(String callingClassName) {
        this.callingClassName = callingClassName;
    }

    public void addClassMethodName(String name) {
        classMethodNames.add(name);
    }


    public void setClassMethodNames(List<String> classMethodNames) {
        this.classMethodNames = classMethodNames;
    }

    public List<String> getClassMethodNames(){
        return classMethodNames;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public String getMethodReturnType() {
        return methodReturnType;
    }

    public void setMethodReturnType(String methodReturnType) {
        this.methodReturnType = methodReturnType;
    }

    public List<String> getParameterType() {
        return parameterType;
    }

    public void addParameterType(String name) {
        this.parameterType.add(name);
    }

    public void setParameterType(List<String> parameterTypes) {
        this.parameterType = (parameterTypes);
    }


    public List<String> getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(List<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public void addParameterName(String parameterName) {
        this.parameterNames.add(parameterName);
    }

    public List<String> getMethodCalls() {
        return methodCalls;
    }

    public void setMethodCalls(List<String> methodCalls) {
        this.methodCalls = methodCalls;
    }

    public void addMethodCall(String methodCall) {
        this.methodCalls.add(methodCall);             // the last string in the array should be the actual call
    }

    public List<String> getMethodCallTargets(){
        return methodCallTargets;
    }

    public void addMethodCallTarget(String methodCallTarget) {
        this.methodCallTargets.add(methodCallTarget);
    }

    public List<String> getVariableDeclarations() {
        return variableDeclarations;
    }

    public void setVariableDeclarations(List<String> variableDeclarations) {
        this.variableDeclarations = variableDeclarations;
    }

    public void addVariableDeclarations(String variableDeclaration) {
        this.variableDeclarations.add(variableDeclaration);
    }

    public List<String> getVariableDeclarationTypes() {
        return variableDeclarationTypes;
    }

    public void setVariableDeclarationTypes(List<String> variableDeclarationTypes) {
        this.variableDeclarationTypes = variableDeclarationTypes;
    }

    public void addVariableDeclarationTypes(String variableDeclarationType) {
        this.variableDeclarationTypes.add(variableDeclarationType);
    }


    public List<String> getClassMethodReturnTypes() {
        return classMethodReturnTypes;
    }

    public void setClassMethodReturnType(List<String> classMethodReturnType) {
        this.classMethodReturnTypes = classMethodReturnType;
    }

    public void addClassMethodReturnType(String classMethodReturnType) {
        this.classMethodReturnTypes.add(classMethodReturnType);
    }


    public List<String> getClassFieldNames() {
        return classFieldNames;
    }

    public void setClassFieldNames(List<String> classFieldNames) {
        this.classFieldNames = classFieldNames;
    }

    public void addClassFieldNames(String classFieldName) {
        this.classFieldNames.add(classFieldName);
    }

    public List<String> getClassFieldTypes() {
        return classFieldTypes;
    }

    public void setClassFieldTypes(List<String> classFieldTypes) {
        this.classFieldTypes = classFieldTypes;
    }

    public void addGetClassFieldTypes(String getClassFieldType) {
        this.classFieldTypes.add(getClassFieldType);
    }
}
