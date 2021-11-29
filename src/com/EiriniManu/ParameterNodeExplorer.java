package com.EiriniManu;

import com.github.javaparser.ast.Node;

import java.util.ArrayList;

public class ParameterNodeExplorer extends  NodeExplorer{

    public ParameterNodeExplorer(IMessageObserver observer){
        setObserverList(new ArrayList<>());
        this.addObserver(observer);
    }

    @Override
    public void checkNode(Node node){     // split parameter node name by space.  first string should be type.   second string should be name
        String[] splitArray = node.toString().split(" ");
        Object[] type = {Fields.PARAMETERTYPE,splitArray[0]};
        sendMessage(type);
        Object[] name = {Fields.PARAMETERNAME,splitArray[1]};
        sendMessage(name);
    }

}
