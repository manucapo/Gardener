package com.EiriniManu.Parsing.NodeExplorer;

import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Messaging.MessageTag;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;


import java.util.ArrayList;

public class BlockNodeExplorer extends  NodeExplorer{

    BlockNodeExplorer(IMessageObserver observer){
        setObserverList(new ArrayList<>());
        this.addObserver(observer);
    }

    @Override
    public void checkNode(Node node) {

        if ((node instanceof IfStmt) || (node instanceof ForStmt) || (node instanceof WhileStmt) ) {
            Object[] msg = {MessageTag.BLOCKNODE, node};
            sendMessage(msg);
        }
    }
}
