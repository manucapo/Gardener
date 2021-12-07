package com.EiriniManu.Parsing.Parser;

import com.EiriniManu.IO.DiagramStructure;
import com.EiriniManu.Messaging.MessageTag;
import com.EiriniManu.Parsing.NodeExplorer.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.printer.XmlPrinter;

import java.util.ArrayList;
import java.util.List;

public class DeepJavaParser extends SafeJavaParser {

    private String className;
    private String classFilePath;
    private String packageName;
    private CompilationUnit cu;
    private int runDepth = 3;
    private static int run;
    private static String methodCaller;

    private boolean nestedMethodFound;


    List<String> methodNameStack = new ArrayList<>();
    List<String> methodTargetStack = new ArrayList<>();
    List<String> methodTargetTypeNameStack = new ArrayList<>();

    public void execute(String methodName, String className, String classFilePath, String packageName, DiagramStructure structure) {

        this.className = className;
        this.classFilePath = classFilePath;
        this.packageName = packageName;
        this.cu = this.ParseFile(className, this.SetSourceRoot(classFilePath, packageName));
        for (Package pkg : Package.getPackages()) {
            if (pkg.getName().contains(packageName)) {
                if (!packageDependencies.contains(pkg.getName())) {
                    packageDependencies.add(pkg.getName());
                }
            }
        }
        run = 0;
        methodCaller = methodName;
        this.parseMethod(this.ParseFile(className, this.SetSourceRoot(classFilePath, packageName)), className, methodName, structure);
    }

    public void parseMethod(CompilationUnit cu, String className, String methodName, DiagramStructure diagramStructure) {

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
                    nodeExplorer.checkNode(node);
                }

                for (Node node : method.findAll(CatchClause.class, Node.TreeTraversal.PREORDER)) { // extract information on variables declared inside catch clause
                    CatchNodeExplorer nodeExplorer = (CatchNodeExplorer) NodeExplorerFactory.create(CatchClause.class);
                    nodeExplorer.checkNode(node);
                }

                for (Node node : method.findAll(VariableDeclarationExpr.class, Node.TreeTraversal.PREORDER)) {  // extract information on variables inside method
                    VariableNodeExplorer nodeExplorer = (VariableNodeExplorer) NodeExplorerFactory.create(VariableDeclarationExpr.class);

                    nodeExplorer.checkNode(node);
                }

                int subMethodCounter = 0;

                for (Node node : method.findAll(MethodCallExpr.class, Node.TreeTraversal.PREORDER)) { // extract information on method calls inside method

                    if (subMethodCounter == 0) {
                        MethodNodeExplorer nodeExplorer = (MethodNodeExplorer) NodeExplorerFactory.create(MethodCallExpr.class);
                        subMethodCounter = nodeExplorer.countSubMethods(node);
                        nodeExplorer.setParser(this);
                        methodCaller = methodName + run;
                        nodeExplorer.checkNode(node);
                    } else {
                        subMethodCounter--;
                    }

                }
            }
        }

    }

    public void parseMethodNode(Node methodcallNode) {
        System.out.println("--------------------TEST PARSER--------------------");
        System.out.println("ORIGINAL METHOD CALL : " + methodcallNode);

        XmlPrinter printer = new XmlPrinter(true);
        System.out.println(printer.output(methodcallNode));




        List<MethodCallExpr> containedMethods = methodcallNode.findAll(MethodCallExpr.class, Node.TreeTraversal.BREADTHFIRST);

        for (int i = 0; i < containedMethods.size(); i++) {
            MethodCallExpr subMethod = containedMethods.get(i);
            String subMethodName = subMethod.findAll(SimpleName.class, Node.TreeTraversal.BREADTHFIRST).get(0).toString();
            methodNameStack.add(subMethodName);
        }

        nestedMethodFound = false;

        Node previousScope = null;

        for (int i = containedMethods.size() - 1; i >= 0; i--) {
            boolean targetFound = false;
            boolean classFound = false;
            Class<?> lastClass = null;

            MethodCallExpr subMethod = containedMethods.get(i);
            String subMethodName = subMethod.findAll(SimpleName.class, Node.TreeTraversal.BREADTHFIRST).get(0).toString();

            Object[] methodNode = {MessageTag.METHODCALLNODE, containedMethods.get(i)};
            sendMessage(methodNode);

            Object[] blockType = {MessageTag.METHODBLOCK, "0"};
            sendMessage(blockType);


            Object[] caller = {MessageTag.METHODCALLER, methodCaller};
            sendMessage(caller);


            for (String classMethod : classMethodNames) {                   // check if node is a class method
                if (classMethod.equals(subMethodName)) {
                    System.out.println("THIS IS A CLASS MEHTOD");
                    methodTargetStack.add(implementingClassName);
                    methodTargetTypeNameStack.add("");
                    if(run < runDepth){
                        run++;

                    nestedMethodFound = true;

                         methodCaller = subMethodName;
                        parseMethodRecursive(cu,className,subMethodName,DiagramStructure.getInstance());
                            }
                    targetFound = true;
                    break;
                }
            }

            Node scope = null;
            if (subMethod.getScope().isPresent()) {                       // get the nearest sub method scope. By removing previous scope from current scope
                scope = subMethod.getScope().get();
                if (previousScope != null) {
                    scope.remove(previousScope);
                }
                System.out.println("DEBUGING SCOPE : " + scope);
            }


            if (scope != null) {
                if (!targetFound) {

                    int catchParameterIndex = 0;
                    if (scope.findFirst(SimpleName.class).isPresent() && subMethod.findAncestor(CatchClause.class).isPresent()) {  // if method is in catch clause. check catch parameters
                        for (String methodCatchParameter : catchParameterNames) {
                            if (methodCatchParameter.equals(scope.findFirst(SimpleName.class).get().toString())) {
                                System.out.println("THIS IS A CATCH PARAMETER");
                                boolean isSafe = false;
                                for (String pkg : packageDependencies) {
                                    try {
                                        Class.forName(pkg + "." + catchParameterTypes.get(catchParameterIndex).replaceAll("<.*>", " "));
                                        isSafe = true;
                                    }
                                    catch (Exception e){
                                        //TODO
                                    }
                                }
                                if (isSafe) {
                                    methodTargetStack.add(catchParameterTypes.get(catchParameterIndex).replaceAll("<.*>", " "));
                                    methodTargetTypeNameStack.add("");
                                } else {
                                    methodNameStack.remove(methodNameStack.size()-1);   // avoid target call and target name list desync
                                }
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
                                boolean isSafe = false;
                                for (String pkg : packageDependencies) {
                                    try {
                                        Class.forName(pkg + "." + parameterTypes.get(parameterIndex).replaceAll("<.*>", " "));
                                        isSafe = true;
                                    }
                                    catch (Exception e){
                                        //TODO
                                    }
                                }
                                if (isSafe) {
                                    methodTargetStack.add(parameterTypes.get(parameterIndex).replaceAll("<.*>", " "));
                                    methodTargetTypeNameStack.add("");
                                    targetFound = true;
                                }
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
                                boolean isSafe = false;
                                for (String pkg : packageDependencies) {
                                    try {
                                        Class.forName(pkg + "." + variableDeclarationTypes.get(variableIndex).replaceAll("<.*>", " "));
                                        isSafe = true;
                                    }
                                    catch (Exception e){
                                        //TODO
                                    }
                                }
                                if (isSafe) {
                                    methodTargetStack.add(variableDeclarationTypes.get(variableIndex).replaceAll("<.*>", " "));
                                    methodTargetTypeNameStack.add("");
                                    targetFound = true;
                                }
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
                                boolean isSafe = false;
                                for (String pkg : packageDependencies) {
                                    try {
                                        Class.forName(pkg + "." + classFieldTypes.get(classFieldIndex).replaceAll("<.*>", " "));
                                        isSafe = true;
                                    }
                                    catch (Exception e){
                                        //TODO
                                    }
                                }
                                if (isSafe) {
                                    methodTargetStack.add(classFieldTypes.get(classFieldIndex).replaceAll("<.*>", " "));
                                    methodTargetTypeNameStack.add("");
                                    targetFound = true;
                                }

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
                            boolean isSafe = false;
                            for (String pkg : packageDependencies) {
                                try {
                                    Class.forName(pkg + "." + fieldAcess.findFirst(NameExpr.class).get().getNameAsString());
                                    isSafe = true;
                                }
                                catch (Exception e){
                                    //TODO
                                }
                            }
                            if (isSafe) {
                                methodTargetStack.add(fieldAcess.findFirst(NameExpr.class).get().getNameAsString());
                                methodTargetTypeNameStack.add("");
                                targetFound = true;
                            }
                        }
                    }
                }


                if (!targetFound) {         // try to resolve method target by using last class
                    for (String pkg : packageDependencies) {
                        try {
                            lastClass = Class.forName(pkg + "." + scope.findFirst(SimpleName.class).get().toString());     // MUST CHECK FOR ALL PACKAGES
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
                        lastClass = Class.forName(pkg + "." + previousTarget);     // MUST CHECK FOR ALL PACKAGES
                        classFound = true;
                        break;
                    } catch (Exception e) {
                        // TODO
                    }

                }


            }

            if (!targetFound) {
                System.out.println("COULD NOT RESOLVE ANY TARGETS");
                methodNameStack.remove(methodNameStack.size() - 1);   // SAFE MODE
                DiagramStructure.getInstance().getMethodCaller().remove(DiagramStructure.getInstance().getMethodCaller().size()-1);
            }

            previousScope = scope;
            System.out.println("RELOOPING -----------------------------");
        }

        for (int i = 0; i <= methodNameStack.size() - 1; i++) {
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

            methodNameStack = new ArrayList<>();
         methodTargetStack = new ArrayList<>();
         methodTargetTypeNameStack = new ArrayList<>();

    }

    public void parseMethodRecursive(CompilationUnit cu, String className, String methodName, DiagramStructure diagramStructure){
    methodCaller = methodName;
    parseMethod(cu,className,methodName,diagramStructure);

    if(run != 0) {
        run--;
    }


    }
}
