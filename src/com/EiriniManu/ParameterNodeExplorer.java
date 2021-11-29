package com.EiriniManu;

import com.github.javaparser.ast.Node;

public class ParameterNodeExplorer extends  NodeExplorer{

    @Override
    public String[] checkNode(Node node){     // split parameter node name by space.  first string should be type.   second string should be name
        return  node.toString().split(" ");
    }
}
