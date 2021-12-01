package com.EiriniManu.Messaging;

import java.util.List;

public interface IMessageSender {
    void addObserver(IMessageObserver observer);
    void removeObserver(IMessageObserver observer);
    void sendMessage(Object o);
}
