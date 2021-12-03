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
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.printer.XmlPrinter;
import jdk.nashorn.internal.ir.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockJavaParser extends SafeJavaParser {

    private List<Node> blockNodes;

    public BlockJavaParser(){
        blockNodes = new ArrayList<>();
    }

    public void execute(String methodName, String className, String classFilePath, String packageName, DiagramStructure structure){
        for (Package pkg: Package.getPackages()) {
            if (pkg.getName().contains(packageName)){
                if (!packageDependencies.contains(pkg.getName())) {
                    packageDependencies.add(pkg.getName());
                }
            }
        }
        this.ParseMethod(this.ParseFile(className, this.SetSourceRoot(classFilePath,packageName)), className, methodName, structure);
    }

    public void ParseMethod(CompilationUnit cu, String className, String methodName, DiagramStructure diagramStructure) {

        ClassOrInterfaceDeclaration clsX = new ClassOrInterfaceDeclaration();                 // Holder variable for the class declaration node.

        if (cu.getClassByName(className).isPresent()) {                                      // Check if class with corresponding name exists. :
            clsX = cu.getClassByName(className).get();                                       // if yes hold the class declaration node
        }

        for (MethodDeclaration method : clsX.getMethods()) {                                 // for every method declaration node in the class :
            if (method.getName().toString().equals(methodName)) {                            // Find method with given name

                XmlPrinter printer = new XmlPrinter(true);
                   System.out.println(printer.output(method));                                // DEBUG PRINTER


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

                for (Node node : method.findAll(Statement.class, Node.TreeTraversal.BREADTHFIRST)){                        // extract block information
                    BlockNodeExplorer nodeExplorer = (BlockNodeExplorer) NodeExplorerFactory.create(BlockStmt.class);
                    nodeExplorer.checkNode(node);
                }

                int subMethodCounter = 0;

                for (Node node : method.findAll(MethodCallExpr.class, Node.TreeTraversal.PREORDER)) { // extract information on method calls inside method

                    if (subMethodCounter == 0){
                        MethodNodeExplorer nodeExplorer = (MethodNodeExplorer) NodeExplorerFactory.create(MethodCallExpr.class);
                        subMethodCounter = nodeExplorer.countSubMethods(node);
                        nodeExplorer.setParser(this);
                        nodeExplorer.checkNode(node);
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
            String blockName = "top 0 0";

            for(int blockIndex = blockNodes.size() - 1; blockIndex >= 0; blockIndex--){             // find deepest block
                if (blockNodes.get(blockIndex).isAncestorOf(subMethod)){
                    if (blockIndex == 0){
                        blockName = "if " + (blockIndex + 1) + " 0";
                        break;
                    } else {
                        boolean parentFound = false;
                        for(int blockParentIndex = blockIndex - 1; blockParentIndex >= 0; blockParentIndex--){          // find closest block parent
                            if (blockNodes.get(blockParentIndex).isAncestorOf(subMethod)){
                                blockName = "if " + (blockIndex + 1) + " " + (blockParentIndex + 1);
                                parentFound = true;
                                break;
                            }
                        }
                        if (!parentFound){
                            blockName = "if " + (blockIndex + 1) + " 0";
                        }
                        break;
                    }
                }
            }

            Object[] blockType = {MessageTag.METHODBLOCK, blockName};
            sendMessage(blockType);


            for (String classMethod : classMethodNames) {                   // check if node is a class method
                if (classMethod.equals(subMethodName)) {
                    System.out.println("THIS IS A CLASS MEHTOD");
                    methodTargetStack.add(implementingClassName);
                    methodTargetTypeNameStack.add("");
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
                                    methodNameStack.remove(i);   // avoid target call and target name list desync
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
                methodNameStack.remove(i);   // SAFE MODE
            }
            previousScope = scope;
            System.out.println("RELOOPING -----------------------------");
        }

        for (int i = methodNameStack.size() - 1; i >= 0; i--) {
            System.out.println("NAME : " + methodNameStack.get(i));

            Object[] type = {MessageTag.METHODCALL, methodNameStack.get(i)};
            sendMessage(type);
        }

        for (int i = 0; i <= methodTargetStack.size() - 1; i++) {
            System.out.println("TARGET : " + methodTargetStack.get(i));

            Object[] type = {MessageTag.METHODCALLTARGET, methodTargetStack.get(i)};
            sendMessage(type);
        }
    }

    public void update(Object o) {

        Object[] data = (Object[]) o;
        MessageTag field = (MessageTag) data[0];
        String string = null;
        Node node = null;

        if (field.equals(MessageTag.BLOCKNODE)){
            node = (Node) data[1];
        } else {
            string =     (String) data[1];
        }


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
            case BLOCKNODE:
                addBlockNode(node);
                break;
            default:
                break;
        }
    }

    public void addBlockNode(Node blockNode) {
        this.blockNodes.add(blockNode);
    }
}
