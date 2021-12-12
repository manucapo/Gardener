package com.EiriniManu.Parsing.NodeExplorer;

/*
    This class represents an object that can extract information from a node belonging to the AST.
    Different nodes are handled with different algorithms by classes that extend the NodeExplorer class
*/

import com.github.javaparser.ast.Node;

public interface INodeExplorer {
     void checkNode(Node node);
}
