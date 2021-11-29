package com.EiriniManu;

public interface IMessageSender {
    void addObserver(IMessageObserver observer);
    void removeObserver(IMessageObserver observer);
    void sendMessage(Object o);
}
