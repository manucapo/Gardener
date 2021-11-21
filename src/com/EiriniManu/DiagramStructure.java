package com.EiriniManu;

import java.util.List;
import java.util.Vector;

public class DiagramStructure implements IDiagramStructure {


    private String implementingClassName;

    private String callingClassName;

    private List<String> classMethodNames;

    private String methodName;

    private List<String> parameterTypeNames;

    public DiagramStructure(){


        implementingClassName = "NULL";
        callingClassName = "NULL";
        classMethodNames = new Vector<>();
        methodName = "NULL";
        parameterTypeNames = new Vector<>();

    }


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
}
