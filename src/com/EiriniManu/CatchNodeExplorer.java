package com.EiriniManu;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CatchNodeExplorer extends NodeExplorer{
    @Override
    public List<String[]> checkNode(Node node){
        List<String[]> typeNameList = new ArrayList<>();
        int index = 0;
        for (Parameter param : node.findAll(Parameter.class)) {
            ParameterNodeExplorer parameterNodeExplorer = (ParameterNodeExplorer) NodeExplorerFactory.create(Parameter.class);
           String[] splitArray = parameterNodeExplorer.checkNode(param);
           typeNameList.add(splitArray);
           index++;
        }
       return typeNameList;
    }
}
