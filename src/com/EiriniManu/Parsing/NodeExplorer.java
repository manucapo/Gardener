package com.EiriniManu.Parsing;

import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Messaging.IMessageSender;
import com.github.javaparser.ast.Node;

import java.util.List;

abstract class NodeExplorer implements INodeExplorer, IMessageSender {

    private List<IMessageObserver> observerList;
    private Object message;

    public NodeExplorer(){}

   public abstract void checkNode(Node node);

    public void setObserverList(List<IMessageObserver> observerList) {
        this.observerList = observerList;
    }

    @Override
    public void addObserver(IMessageObserver observer) {
            observerList.add(observer);
    }

    @Override
    public void removeObserver(IMessageObserver observer) {
        observerList.remove(observer);
    }

    @Override
    public void sendMessage(Object message) {
        for (IMessageObserver observer : observerList) {
            observer.update(message);
        }
    }
}
