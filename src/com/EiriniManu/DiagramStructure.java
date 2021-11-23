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
    private String methodName;                    // The name of the method the user is interested in
    private List<String> parameterTypeNames;      //  A list of names of every parameter TYPE that belong to the method the user is interested in
    private List<String> methodCalls;
    private List<String> methodCallTargets;

    public DiagramStructure() {                    // Default constructor. Initializes the class to "safe" values.
        implementingClassName = "NULL";           // The string "NULL" was chosen as default as a class or method could never be called "NULL". Allowing error detection
        callingClassName = "NULL";
        classMethodNames = new ArrayList<>();     // Are ArrayLists the best data structure for our lists of Strings?
        methodName = "NULL";
        parameterTypeNames = new ArrayList<>();
        methodCalls = new ArrayList<>();
        methodCallTargets = new ArrayList<>();
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

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParameterTypeNames() {
        return parameterTypeNames;
    }

    public void addParameterTypeName(String name) {
        this.parameterTypeNames.add(name);
    }

    public void setParameterTypeName(List<String> parameterTypeNames) {
        this.parameterTypeNames = (parameterTypeNames);
    }

    public List<String> getMethodCalls() {
        return methodCalls;
    }

    public void setMethodCalls(List<String> methodCalls) {
        this.methodCalls = methodCalls;
    }

    public void addMethodCall(String methodCall) {
        String[] splitArray1 = methodCall.split("\\(", 0);        // split method call until the first ( bracket
        String callWithParametersRemoved = splitArray1[0];                   // the string before the ( bracket should be the call without parameters.
        String[] splitArray2 =  callWithParametersRemoved.split("\\."); // split the call without parameters by . dot
        this.methodCalls.add(splitArray2[splitArray2.length -1]);             // the last string in the array should be the actual call
        this.methodCallTargets.add(splitArray2[0]);                           // the first string in the array should be the target class
    }

    public List<String> getMethodCallTargets(){
        return methodCallTargets;
    }
}
