package com.EiriniManu.Parsing;

import com.EiriniManu.Messaging.IMessageObserver;
import com.github.javaparser.ast.Node;

import java.util.ArrayList;

public class VariableNodeExplorer extends NodeExplorer{

    public VariableNodeExplorer(IMessageObserver observer){
        setObserverList(new ArrayList<>());
        this.addObserver(observer);
    }

     @Override
    public void checkNode(Node node){         // split parameter node name by space.  first string should be type.   second string should be name
         String[] splitArray = node.toString().split(" ");
         Object[] type = {Fields.VARIABLEDECLARATIONTYPE,splitArray[0]};
         sendMessage(type);
         Object[] name = {Fields.VARIABLEDECLARATIONNAME,splitArray[1]};
         sendMessage(name);
    }
}
