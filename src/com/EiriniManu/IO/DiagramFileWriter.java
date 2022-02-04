package com.EiriniManu.IO;

import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Messaging.MessageTag;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiagramFileWriter implements IMessageObserver {

    private static final DiagramFileWriter instance = new DiagramFileWriter();  // singleton instance

    private String methodName;
    private String implementingClassName;
    private List<String> methodCalls;
    private List<String> methodBlocks;
    private List<Node> blockNodes;
    private List<String> methodCallTargets;
    private List<String> methodCaller;
    private List<String> callingClassNames;

    private DiagramFileWriter(){
        methodName = "NULL";
        implementingClassName = "NULL";
        methodCalls = new ArrayList<>();
        blockNodes = new ArrayList<>();
        methodBlocks = new ArrayList<>();
        methodCallTargets = new ArrayList<>();
        methodCaller = new ArrayList<>();
        callingClassNames = new ArrayList<>();
    }

    public void reset(){
        methodName = "NULL";
        implementingClassName = "NULL";
        methodCalls = new ArrayList<>();
        blockNodes = new ArrayList<>();
        methodBlocks = new ArrayList<>();
        methodCallTargets = new ArrayList<>();
        methodCaller = new ArrayList<>();
        callingClassNames = new ArrayList<>();
    }
    @Override
    public void update(Object o) {

        Object[] data = (Object[]) o;
        MessageTag field = (MessageTag) data[0];
        String string = null;
        Node node = null;
        int index = 0;

        if (field.equals(MessageTag.BLOCKNODE)){
            node = (Node) data[1];
        } else if (field.equals(MessageTag.REMOVEMETHODCALLER)){
            index = (int) data[1];
        }
        else {
            if (data[1] instanceof  String) {
                string = (String) data[1];
            }
        }


        switch (field){
            case IMPLEMENTINGCLASS:
                setImplementingClassName(string);
                break;
            case METHODNAME:
                setMethodName(string);
                break;
            case METHODCALL:
                addMethodCall(string);
                break;
            case METHODBLOCK:
                addMethodBlock(string);
                break;
            case BLOCKNODE:
                addBlockNode(node);
                break;
            case METHODCALLTARGET:
                addMethodCallTarget(string);
                break;
            case ADDMETHODCALLER:
                addMethodCaller(string);
                break;
            case REMOVEMETHODCALLER:
                removeMethodCaller(index);
                break;
            case ADDCALLINGCLASS:
                addCallingClassName(string);
                break;
            case REMOVECALLINGCLASS:
                removeCallingClassName(index);
                break;
            case RESET:
                reset();
                break;
            default:
                break;
        }
    }

    public void createDiagramFile(String path){


        List<Node> openBlocks = new ArrayList<>();
        List<String> openMethodCalls = new ArrayList<>();
        List<String> openMethodTargets = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(path);
            writer.write("@startuml \n");
            writer.write("participant Actor \n");
            writer.write("Actor -> " + implementingClassName + ": " + methodName + "\n");
            writer.write("activate " + implementingClassName + "\n");
            int i = 0;
            int blockCount = 0;
            for (String method : methodCalls) {

            if(i > 0 && !methodBlocks.get(i).equals("x")){
                int count = 0;
                if (!methodBlocks.get(i).equals(methodBlocks.get(i-1))) {
                    if (!methodBlocks.get(i).equals("0")){

                    for (int j = openBlocks.size() - 1; j >= 0; j--) {
                        if (!openBlocks.get(j).isAncestorOf(blockNodes.get(Integer.parseInt(methodBlocks.get(i)) - 1))) {
                            if (!openBlocks.get(j).equals(blockNodes.get(Integer.parseInt(methodBlocks.get(i)) - 1))){
                                count++;
                            }

                        } else {
                            break;
                        }

                    }
                } else {
                        if (!methodBlocks.get(i-1).equals("0")){
                            count = blockCount;
                        }
                    }
                }
                for (int j = 0; j < count ; j++) {
                    writer.write("end \n");
                    openBlocks.remove(openBlocks.size()-1);
                    blockCount--;
                }
            }

            if(!methodBlocks.get(i).equals("x") ){
               if (!methodBlocks.get(i).equals("0")){

                       for (Node node : blockNodes){
                           if (node.isAncestorOf(blockNodes.get(Integer.parseInt(methodBlocks.get(i))-1)) && !openBlocks.contains(node)){
                               if(!node.equals(blockNodes.get(Integer.parseInt(methodBlocks.get(i))-1))){
                                   String blockName = "";
                                   String groupType = "alt";
                                   if(node instanceof IfStmt){
                                       groupType = "alt";
                                       blockName = "if";
                                   } else if ( node instanceof ForStmt){
                                       groupType = "loop";
                                       blockName = "for ";
                                   } else if (node instanceof WhileStmt){
                                       groupType = "loop";
                                       blockName = "while ";
                                   }
                                   writer.write(groupType + " " + blockName + " "  + blockNodes.indexOf(node) + "\n");
                                   openBlocks.add(node);
                                   blockCount++;
                               }

                           }
                       }


                       if (!openBlocks.contains(blockNodes.get(Integer.parseInt(methodBlocks.get(i))-1))) {
                           String blockName = "";
                           String groupType = "alt";
                           if(blockNodes.get(Integer.parseInt(methodBlocks.get(i))-1) instanceof IfStmt){
                               groupType = "alt";
                               blockName = "if";
                           } else if ( blockNodes.get(Integer.parseInt(methodBlocks.get(i))-1) instanceof ForStmt){
                               groupType = "loop";
                               blockName = "for ";
                           } else if (blockNodes.get(Integer.parseInt(methodBlocks.get(i))-1) instanceof WhileStmt){
                               groupType = "loop";
                               blockName = "while ";
                           }
                           writer.write(groupType + " " + blockName + " " + methodBlocks.get(i) + "\n");
                           openBlocks.add(blockNodes.get(Integer.parseInt(methodBlocks.get(i))-1));
                           blockCount++;
                       }


               }
            }

                if(methodCallTargets.get(i).equals("this")){

                    if (openMethodCalls.contains(methodCaller.get(i))) {
                        int count = openMethodCalls.size() - 1;
                        for (int j = count; j >= 0 ; j--) {
                            if (openMethodCalls.get(j).equals(methodCaller.get(i))){
                                writer.write(openMethodTargets.get(j) + "-->" + implementingClassName + "\n");
                                writer.write("deactivate " + openMethodTargets.get(j) + "\n");
                                openMethodCalls.remove(openMethodCalls.get(j));
                                openMethodTargets.remove(openMethodTargets.get(j));
                                break;
                            } else {
                                writer.write(openMethodTargets.get(j) + "-->" + implementingClassName + "\n");
                                writer.write("deactivate " + openMethodTargets.get(j) + "\n");
                                openMethodCalls.remove(openMethodCalls.get(j));
                                openMethodTargets.remove(openMethodTargets.get(j));
                            }
                        }


                    }

                    writer.write(implementingClassName + " -> " + implementingClassName + ": " + method + "\n");
                    writer.write("activate " + implementingClassName + "\n");
                    openMethodCalls.add(methodCaller.get(i));
                    openMethodTargets.add(implementingClassName);

                } else if (methodCallTargets.get(i).contains("[]")){

                    if (openMethodCalls.contains(methodCaller.get(i))) {
                        int count = openMethodCalls.size() - 1;
                        for (int j = count; j >= 0 ; j--) {
                            if (openMethodCalls.get(j).equals(methodCaller.get(i))){
                                writer.write(openMethodTargets.get(j) + "-->" + implementingClassName + "\n");
                                writer.write("deactivate " + openMethodTargets.get(j) + "\n");
                                openMethodCalls.remove(openMethodCalls.get(j));
                                openMethodTargets.remove(openMethodTargets.get(j));
                                break;
                            } else {
                                writer.write(openMethodTargets.get(j) + "-->" + implementingClassName + "\n");
                                writer.write("deactivate " + openMethodTargets.get(j) + "\n");
                                openMethodCalls.remove(openMethodCalls.get(j));
                                openMethodTargets.remove(openMethodTargets.get(j));
                            }
                        }


                    }



                    writer.write(implementingClassName + " -> " + "__Array__" + ": " + method + "\n");
                    writer.write("activate " + "__Array__" + "\n");
                    openMethodCalls.add(methodCaller.get(i));
                    openMethodTargets.add("__Array__");

                } else if (methodCallTargets.get(i).contains("LOSTMESSAGE")){
                    writer.write(implementingClassName + " ->x] " + ": " + method + "\n");
                } else {


                    if (openMethodCalls.contains(methodCaller.get(i))) {
                        int count = openMethodCalls.size() - 1;
                        for (int j = count; j >= 0 ; j--) {
                            if (openMethodCalls.get(j).equals(methodCaller.get(i))){
                                writer.write(openMethodTargets.get(j) + "-->" + implementingClassName + "\n");
                                writer.write("deactivate " + openMethodTargets.get(j) + "\n");
                                openMethodCalls.remove(openMethodCalls.get(j));
                                openMethodTargets.remove(openMethodTargets.get(j));
                                break;
                            } else {
                                writer.write(openMethodTargets.get(j) + "-->" + callingClassNames.get(j-1) + "\n");
                                writer.write("deactivate " + openMethodTargets.get(j) + "\n");
                                openMethodCalls.remove(openMethodCalls.get(j));
                                openMethodTargets.remove(openMethodTargets.get(j));
                            }
                        }


                    }
                    if (i == 0) {
                        writer.write(implementingClassName + " -> " + methodCallTargets.get(i) + ": " + method + "\n");
                        writer.write("activate " + methodCallTargets.get(i) + "\n");
                        openMethodCalls.add(methodCaller.get(i));
                        openMethodTargets.add(methodCallTargets.get(i));
                    } else {
                        writer.write(callingClassNames.get(i-1) + " -> " + methodCallTargets.get(i) + ": " + method + "\n");
                        writer.write("activate " + methodCallTargets.get(i) + "\n");
                        openMethodCalls.add(methodCaller.get(i));
                        openMethodTargets.add(methodCallTargets.get(i));
                    }

                }
                     if (i == methodCalls.size()- 1) {

                         for (int j = openMethodCalls.size()-1; j >= 0 ; j--) {
                             writer.write(openMethodTargets.get(j) + "-->" + implementingClassName + "\n");
                             writer.write("deactivate " + openMethodTargets.get(j) + "\n");
                         }

                    }


                i++;
            }


            for (int j = 0; j < blockCount ; j++) {
                writer.write("end \n");
            }
            writer.write("return \n");
            writer.write("@enduml \n");
            writer.close();
        }
        catch (IOException e) {
            System.out.println("ERROR WRITING FILE");
        }

    }

    public static DiagramFileWriter getInstance(){
        return instance;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setImplementingClassName(String implementingClassName) {
        this.implementingClassName = implementingClassName;
    }

    public void addMethodCall(String methodCall) {
        this.methodCalls.add(methodCall);
    }

    public void addMethodBlock(String methodBlock) {
        this.methodBlocks.add(methodBlock);
    }

    public void addBlockNode(Node blockNode) {
        this.blockNodes.add(blockNode);
    }

    public void addMethodCallTarget(String methodCallTarget) {
        this.methodCallTargets.add(methodCallTarget);
    }

    public void addMethodCaller(String methodCaller) {
        this.methodCaller.add(methodCaller);
    }

    public  void removeMethodCaller(int index){
        this.methodCaller.remove(index);
    }

    public void addCallingClassName(String callingClassName) {
        this.callingClassNames.add(callingClassName);
    }

    public void removeCallingClassName(int index) {
        this.callingClassNames.remove(index);
    }
}


