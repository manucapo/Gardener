package com.EiriniManu.Parsing.NodeExplorer;

import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Messaging.MessageTag;
import com.github.javaparser.ast.Node;

import java.util.ArrayList;

public class ParameterNodeExplorer extends  NodeExplorer{

    ParameterNodeExplorer(IMessageObserver observer){
        setObserverList(new ArrayList<>());
        this.addObserver(observer);
    }

    @Override
    public void checkNode(Node node){     // split parameter node name by space.  first string should be type.   second string should be name
        String[] splitArray = node.toString().split(" ");
        Object[] type = {MessageTag.PARAMETERTYPE,splitArray[0]};
        sendMessage(type);
        Object[] name = {MessageTag.PARAMETERNAME,splitArray[1]};
        sendMessage(name);
    }

}
