package com.EiriniManu.Messaging;

public interface IStringListSender {
    void addObserver(IMessageObserver observer);
    void removeObserver(IMessageObserver observer);
    void sendMessage(Object o);
}
