package com.EiriniManu.Parsing.NodeExplorer;

import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Parsing.Parser.SafeJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.ArrayList;

public class MethodNodeExplorer extends NodeExplorer{

    private SafeJavaParser parser;

    public MethodNodeExplorer(IMessageObserver observer){
        setObserverList(new ArrayList<>());
        this.addObserver(observer);
    }

    public int countSubMethods(Node node){

        int subMethodCounter = 0;

        for (Node subNode : node.findAll(MethodCallExpr.class)) {  // check for nested method calls
            if (subNode != node) {                                                               // ignore original node
                subMethodCounter += 1;
            }
        }

        return subMethodCounter;
    }

    @Override
    public void checkNode(Node node) {
     //  parser.parseMethodNode(node);
    }
}
