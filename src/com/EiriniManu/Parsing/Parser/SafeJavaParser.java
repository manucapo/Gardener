package com.EiriniManu.Parsing.Parser;

/*
    This class represents an object that can create an AST (abstract symbol tree) from java source code and pass the resulting tree for information.
    This is the second layer of information extraction in our process.
*/

import com.EiriniManu.IO.DiagramStructure;
import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Messaging.MessageTag;
import com.EiriniManu.Parsing.NodeExplorer.CatchNodeExplorer;
import com.EiriniManu.Parsing.NodeExplorer.NodeExplorerFactory;
import com.EiriniManu.Parsing.NodeExplorer.ParameterNodeExplorer;
import com.EiriniManu.Parsing.NodeExplorer.VariableNodeExplorer;
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


import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class SafeJavaParser implements IJavaParser{

    protected List<String> packageDependencies;
    protected List<IMessageObserver> observerList;

    protected List<String> catchParameterNames;
    protected List<String> catchParameterTypes;
    protected List<String> parameterNames;
    protected List<String> parameterTypes;
    protected List<String> classMethodNames;
    protected String implementingClassName;
    protected List<String> variableDeclarationNames;
    protected List<String> variableDeclarationTypes;
    protected List<String> classFieldNames;
    protected List<String> classFieldTypes;




    public SafeJavaParser() {

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

    public void execute(String methodName, String className, String classFilePath, String packageName, DiagramStructure structure){
        this.ParseMethod(this.ParseFile(className, this.SetSourceRoot(classFilePath,packageName)), className, methodName, structure);
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


    public void ParseMethod(CompilationUnit cu, String className, String methodName, DiagramStructure diagramStructure) {

        ClassOrInterfaceDeclaration clsX = new ClassOrInterfaceDeclaration();                 // Holder variable for the class declaration node.

        if (cu.getClassByName(className).isPresent()) {                                      // Check if class with corresponding name exists. :
            clsX = cu.getClassByName(className).get();                                       // if yes hold the class declaration node
        }

        for (MethodDeclaration method : clsX.getMethods()) {                                 // for every method declaration node in the class :
            if (method.getName().toString().equals(methodName)) {                            // Find method with given name

                XmlPrinter printer = new XmlPrinter(true);
             //   System.out.println(printer.output(method));                                // DEBUG PRINTER


                for (Parameter node : method.getParameters()) {                       // extract method parameters
                    ParameterNodeExplorer nodeExplorer = (ParameterNodeExplorer) NodeExplorerFactory.create(Parameter.class, diagramStructure);
                    nodeExplorer.checkNode(node);
                }

                for (Node node : method.findAll(CatchClause.class, Node.TreeTraversal.PREORDER)) { // extract information on variables declared inside catch clause
                    CatchNodeExplorer nodeExplorer = (CatchNodeExplorer) NodeExplorerFactory.create(CatchClause.class, diagramStructure);
                     nodeExplorer.checkNode(node);
                }

                for (Node node : method.findAll(VariableDeclarationExpr.class, Node.TreeTraversal.PREORDER)) {  // extract information on variables inside method
                    VariableNodeExplorer nodeExplorer = (VariableNodeExplorer) NodeExplorerFactory.create(VariableDeclarationExpr.class, diagramStructure);

                    nodeExplorer.checkNode(node);
                }

                int subMethodCounter = 0;

                for (Node node : method.findAll(MethodCallExpr.class, Node.TreeTraversal.PREORDER)) { // extract information on method calls inside method

                    if (subMethodCounter == 0){
                        for (Node subNode : node.findAll(MethodCallExpr.class)) {  // check for nested method calls
                            if (subNode != node) {                                                               // ignore original node
                                subMethodCounter += 1;
                            }
                        }
                        parseMethodNode(node);
                    } else {subMethodCounter--;}

            }
        }
    }
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
                        for (String methodParameter : catchParameterNames) {
                            if (methodParameter.equals(subMethod.findFirst(NameExpr.class).get().getNameAsString())) {
                                System.out.println("THIS IS A CATCH PARAMETER");
                                methodTargetStack.add(catchParameterTypes.get(catchParameterIndex).replaceAll("<.*>", " "));
                                methodTargetTypeNameStack.add("");
                                targetFound = true;
                                break;
                            }
                            catchParameterIndex++;
                        }
                    }

                    int parameterIndex = 0;
                    if (subMethod.findFirst(NameExpr.class).isPresent()) {
                        for (String methodParameter : parameterNames) {
                            if (methodParameter.equals(subMethod.findFirst(NameExpr.class).get().getNameAsString())) {
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
                    for (String classMethod :  classMethodNames) {
                        if (classMethod.equals(subMethodName)) {
                            System.out.println("THIS IS A CLASS MEHTOD");
                            methodTargetStack.add(implementingClassName);
                            methodTargetTypeNameStack.add("");
                            targetFound = true;
                            break;
                        }
                    }
                }


                if (!targetFound) {

                    int variableIndex = 0;
                    if (subMethod.findFirst(NameExpr.class).isPresent()) {
                        for (String classMethod : variableDeclarationNames) {
                            if (classMethod.equals(subMethod.findFirst(NameExpr.class).get().getNameAsString())) {
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

                if (!targetFound && subMethod.findFirst(NameExpr.class).isPresent()) {

                    int classFieldIndex = 0;
                    for (String classField : classFieldNames) {
                        System.out.println("CLASS FIELD FINDING AT " + classField);
                        if (classField.equals(subMethod.findFirst(NameExpr.class).get().getNameAsString())) {
                            System.out.println("THIS IS A CLASS FIELD");
                            methodTargetStack.add(classFieldTypes.get(classFieldIndex).replaceAll("<.*>", " "));
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
                    methodNameStack.remove(i);


                    /*
                                    if (false){                     // SAFE MODE
                    methodNameStack.remove(i);
                } else if (true) {                       // LOST MESSAGE MODE
                    methodTargetStack.add("LOSTMESSAGE");
                } else {
                    methodTargetStack.add("ERROR");
                }
                     */

            }
            System.out.println("RELOOPING -----------------------------");
        }

        for (int i = methodNameStack.size() - 1; i >= 0; i--) {
            System.out.println("NAME : " + methodNameStack.get(i));

            Object[] type = {MessageTag.METHODCALL,methodNameStack.get(i)};
            sendMessage(type);

        //    structure.addMethodCall(methodNameStack.get(i));                                                 // first contained name should be method name

        }

        for (int i = 0; i <= methodTargetStack.size() - 1; i++) {
            System.out.println("TARGET : " + methodTargetStack.get(i));

            Object[] type = {MessageTag.METHODCALLTARGET,methodTargetStack.get(i)};
            sendMessage(type);
           //
            // structure.addMethodCallTarget(methodTargetStack.get(i));                                                 // first contained name should be method name
        }
    }

    // GETTERS AND SETTERS


    public List<String> getPackageDependencies() {
        return packageDependencies;
    }

    public void setPackageDependencies(List<String> packageDependencies) {
        this.packageDependencies = packageDependencies;
    }

    public void addDependency(String packageDependency) {
        this.packageDependencies.add(packageDependency);
    }

    @Override
    public void addObserver(IMessageObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(IMessageObserver observer) {
            observerList.remove(observer);
    }

    @Override
    public void sendMessage(Object message) {
        for (IMessageObserver observer : observerList) {
            observer.update(message);
        }
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
                break;
            case CLASSMETHODNAME:
                setClassMethodName(string);
                break;
            case CLASSMETHODRETURNTYPE:
                break;
            case CLASSFIELDNAME:
                addClassFieldName(string);
                break;
            case CLASSFIELDTYPE:
                addClassFieldType(string);
                break;
            case METHODNAME:
                break;
            case METHODRETURNTYPE:
                break;
            case PARAMETERTYPE:
                addParameterType(string);
                break;
            case PARAMETERNAME:
                addParameterNames(string);
                break;
            case CATCHPARAMETERTYPE:
                addCatchParameterType(string);
                break;
            case CATCHPARAMETERNAME:
                addCatchParameterName(string);
                break;
            case METHODCALL: ;
                break;
            case METHODCALLTARGET:
                break;
            case VARIABLEDECLARATIONNAME:
                addVariableDeclarationName(string);
                break;
            case VARIABLEDECLARATIONTYPE:
                addVariableDeclarationType(string);
                break;
            default:
                break;
        }
    }


    // SETTERS


    public void addCatchParameterName(String catchParameterName) {
        this.catchParameterNames.add(catchParameterName);
    }

    public void addCatchParameterType(String catchParameterType) {
        this.catchParameterTypes.add(catchParameterType);
    }

    public void addParameterNames(String parameterName) {
        this.parameterNames.add(parameterName);
    }

    public void addParameterType(String parameterType) {
        this.parameterTypes.add(parameterType);
    }

    public void setClassMethodName(String classMethodName) {
        this.classMethodNames.add(classMethodName);
    }

    public void setImplementingClassName(String implementingClassName) {
        this.implementingClassName = implementingClassName;
    }

    public void addVariableDeclarationName(String variableDeclarationName) {
        this.variableDeclarationNames.add(variableDeclarationName);
    }

    public void addVariableDeclarationType(String variableDeclarationType) {
        this.variableDeclarationTypes.add(variableDeclarationType);
    }

    public void addClassFieldName(String classFieldName) {
        this.classFieldNames.add(classFieldName);
    }

    public void addClassFieldType(String classFieldType) {
        this.classFieldTypes.add(classFieldType);
    }
}
