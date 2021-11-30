package com.EiriniManu.IO;

/*
    This class represents a data structure that contains all information needed by the plantUML diagram to generate the desired UML sequence diagram
    Any information extracted from a method should be reflected on this "structure".
    Add information here as necessary. Keeping the information in Strings is a good idea because the information will eventually be mapped to
    Strings in a text file.
 */

import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Messaging.IMessageSender;
import com.EiriniManu.Messaging.MessageTag;

import java.util.ArrayList;
import java.util.List;



public class DiagramStructure implements IMessageObserver, IMessageSender {
    private List<IMessageObserver> observerList;

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
    private List<String> catchParameterTypes;
    private List<String> catchParameterNames;
    private List<String> methodCalls;             //  A list of methodCalls made by the method  ( 1 layer of calls atm)
    private List<String> methodCallTargets;       // The target classes of the method calls
    private List<String> variableDeclarations;     //  A list of variable declarations made by the method  ( 1 layer of calls atm)
    private List<String> variableDeclarationTypes; //  The types of variable declarations made by the method  ( 1 layer of calls atm)

    private static final DiagramStructure instance = new DiagramStructure();  // singleton instance

    private DiagramStructure() {                    // Default constructor. Initializes the class to "safe" values.
        implementingClassName = "NULL";           // The string "NULL" was chosen as default as a class or method could never be called "NULL". Allowing error detection
        callingClassName = "NULL";
        classMethodNames = new ArrayList<>();     // Are ArrayLists the best data structure for our lists of Strings?
        classMethodReturnTypes = new ArrayList<>();
        methodName = "NULL";
        methodReturnType = "NULL";
        methodCalls = new ArrayList<>();
        methodCallTargets = new ArrayList<>();
        variableDeclarations = new ArrayList<>();
        variableDeclarationTypes = new ArrayList<>();
        parameterNames = new ArrayList<>();
        parameterType = new ArrayList<>();
        catchParameterTypes = new ArrayList<>();
        catchParameterNames = new ArrayList<>();
        classFieldNames = new ArrayList<>();
        classFieldTypes = new ArrayList<>();
        observerList = new ArrayList<>();
    }

    public static DiagramStructure getInstance(){
        return instance;
    }

    // Getters and setters
    public String getImplementingClassName() {
        return implementingClassName;
    }

    public void setImplementingClassName(String implementingClassName) {
        this.implementingClassName = implementingClassName;
        Object[] msg = {MessageTag.IMPLEMENTINGCLASS,this.implementingClassName };
        sendMessage(msg);
    }

    public String getCallingClassName() {
        return callingClassName;
    }

    private void setCallingClassName(String callingClassName) {

        this.callingClassName = callingClassName;

        Object[] msg = {MessageTag.CALLINGCLASS,this.callingClassName };
        sendMessage(msg);
    }

    public void addClassMethodName(String name) {

        classMethodNames.add(name);
        Object[] msg = {MessageTag.CLASSMETHODNAME,name};
        sendMessage(msg);
    }


    public String getMethodName() {
        return methodName;
    }

    private void setMethodName(String methodName) {

        this.methodName = methodName;
        Object[] msg = {MessageTag.METHODNAME,this.methodName };
        sendMessage(msg);
    }


    private void setMethodReturnType(String methodReturnType) {
        this.methodReturnType = methodReturnType;
        Object[] msg = {MessageTag.METHODRETURNTYPE,this.methodReturnType };
        sendMessage(msg);
    }


    private void addParameterType(String name) {
        this.parameterType.add(name);
        Object[] msg = {MessageTag.PARAMETERTYPE,name};
        sendMessage(msg);
    }


    private void addParameterName(String parameterName) {
        this.parameterNames.add(parameterName);
        Object[] msg = {MessageTag.PARAMETERNAME,parameterName };
        sendMessage(msg);
    }


    private void addCatchParameterTypes(String catchParameterType) {

        this.catchParameterTypes.add(catchParameterType);
        Object[] msg = {MessageTag.CATCHPARAMETERTYPE,catchParameterType };
        sendMessage(msg);
    }


    private void addCatchParameterNames(String catchParameterName) {

        this.catchParameterNames.add(catchParameterName);
        Object[] msg = {MessageTag.CATCHPARAMETERNAME,catchParameterName};
        sendMessage(msg);
    }

    public List<String> getMethodCalls() {
        return methodCalls;
    }


    public void addMethodCall(String methodCall) {
        this.methodCalls.add(methodCall);             // the last string in the array should be the actual call
        Object[] msg = {MessageTag.METHODCALL,methodCall};
        sendMessage(msg);
    }

    public List<String> getMethodCallTargets(){
        return methodCallTargets;
    }

    public void addMethodCallTarget(String methodCallTarget) {

        this.methodCallTargets.add(methodCallTarget);
        Object[] msg = {MessageTag.METHODCALLTARGET,methodCallTarget };
        sendMessage(msg);
    }


    private void addVariableDeclarations(String variableDeclaration) {
        this.variableDeclarations.add(variableDeclaration);
        Object[] msg = {MessageTag.VARIABLEDECLARATIONNAME,variableDeclaration };
        sendMessage(msg);
    }


    private void addVariableDeclarationTypes(String variableDeclarationType) {
        this.variableDeclarationTypes.add(variableDeclarationType);
        Object[] msg = {MessageTag.VARIABLEDECLARATIONTYPE,variableDeclarationType };
        sendMessage(msg);
    }


    private void addClassMethodReturnType(String classMethodReturnType) {
        this.classMethodReturnTypes.add(classMethodReturnType);
        Object[] msg = {MessageTag.CLASSMETHODRETURNTYPE,classMethodReturnType};
        sendMessage(msg);
    }

    private void addClassFieldNames(String classFieldName) {

        this.classFieldNames.add(classFieldName);
        Object[] msg = {MessageTag.CLASSFIELDNAME, classFieldName};
        sendMessage(msg);
    }


    private void addClassFieldTypes(String classFieldType) {

        this.classFieldTypes.add(classFieldType);
        Object[] msg = {MessageTag.CLASSFIELDTYPE,classFieldType };
        sendMessage(msg);
    }

    public void reset(){
        implementingClassName = "NULL";
        callingClassName = "NULL";
        classMethodNames = new ArrayList<>();
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
        catchParameterTypes = new ArrayList<>();
        catchParameterNames = new ArrayList<>();
        classFieldNames = new ArrayList<>();
        classFieldTypes = new ArrayList<>();
    }

    @Override
    public void update(Object o) {

        Object[] data = (Object[]) o;
        MessageTag field = (MessageTag) data[0];
        String string = (String) data[1];


        switch (field){
            case IMPLEMENTINGCLASS:
                setImplementingClassName(string);
                break;
            case CALLINGCLASS:
                setCallingClassName(string);
                break;
            case CLASSMETHODNAME:
                addClassMethodName(string);
                break;
            case CLASSMETHODRETURNTYPE:
                addClassMethodReturnType(string);
                break;
            case CLASSFIELDNAME:
                addClassFieldNames(string);
                break;
            case CLASSFIELDTYPE:
                addClassFieldTypes(string);
                break;
            case METHODNAME:
                setMethodName(string);
                break;
            case METHODRETURNTYPE:
                setMethodReturnType(string);
                break;
            case PARAMETERTYPE:
                addParameterType(string);
                break;
            case PARAMETERNAME:
                addParameterName(string);
                break;
            case CATCHPARAMETERTYPE:
                addCatchParameterTypes(string);
                break;
            case CATCHPARAMETERNAME:
                addCatchParameterNames(string);
                break;
            case METHODCALL:
                addMethodCall(string);
                break;
            case METHODCALLTARGET:
                addMethodCallTarget(string);
                break;
            case VARIABLEDECLARATIONNAME:
                addVariableDeclarations(string);
                break;
            case VARIABLEDECLARATIONTYPE:
                addVariableDeclarationTypes(string);
                break;
            default:
                break;
        }
    }

    @Override
    public void addObserver(IMessageObserver observer) {
        this.observerList.add(observer);
    }

    @Override
    public void removeObserver(IMessageObserver observer) {
        this.observerList.remove(observer);
    }

    @Override
    public void sendMessage(Object message) {
        for (IMessageObserver observer : observerList) {
            observer.update(message);
        }
    }
}


