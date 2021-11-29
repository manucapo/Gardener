package com.EiriniManu.Parsing;

import com.EiriniManu.Messaging.IMessageObserver;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;

import java.util.ArrayList;

public class CatchNodeExplorer extends NodeExplorer{

    public CatchNodeExplorer(IMessageObserver observer){
        setObserverList(new ArrayList<>());
        this.addObserver(observer);
    }

    @Override
    public void checkNode(Node node){
        for (Parameter param : node.findAll(Parameter.class)) {
            String[] splitArray = node.toString().split(" ");
            Object[] type = {Fields.CATCHPARAMETERTYPE,splitArray[0]};
            sendMessage(type);
            Object[] name = {Fields.CATCHPARAMETERNAME,splitArray[1]};
            sendMessage(name);
        }
    }

}
