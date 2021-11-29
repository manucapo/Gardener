package com.EiriniManu;

import com.github.javaparser.ast.Node;

abstract class NodeExplorer implements INodeExplorer {
    public abstract Object checkNode(Node node);
}
