package com.EiriniManu;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class MethodNodeExplorer extends NodeExplorer{
    @Override
    public Integer checkNode(Node node){
            int subMethodCounter = 0;
            for (Node subNode : node.findAll(MethodCallExpr.class)) {  // check for nested method calls
                if (subNode != node) {                                                               // ignore original node
                    subMethodCounter += 1;
                }
            }
            return  subMethodCounter;                                               // return amount of nested method calls
    }
}
