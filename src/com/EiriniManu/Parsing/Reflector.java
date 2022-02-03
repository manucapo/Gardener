package com.EiriniManu.Parsing;

/*
    This class represents an object that can "reflect" on other java classes using the Java Reflection API (https://www.oracle.com/technical-resources/articles/java/javareflection.html)
    This allows us to quickly obtain some contextual information on the method we want to make a diagram of.
    This is the first layer of information extraction in our process.
*/

import com.EiriniManu.Messaging.IMessageObserver;
import com.EiriniManu.Messaging.IMessageSender;
import com.EiriniManu.Messaging.MessageTag;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class Reflector implements IReflector, IMessageSender {

    private List<IMessageObserver> observerList;

    public Reflector() {
        this.observerList = new ArrayList<>();
    }

    public void reflectOnClass(Object obj) {   // Get information on a java class and update diagram structure
        Class<?> cls = obj.getClass();                                           // get class


        Object[] className = {MessageTag.IMPLEMENTINGCLASS,cls.getSimpleName()};
        sendMessage(className);

        for (Method method : cls.getDeclaredMethods()) {                                   // for every Method declared in the class
            Object[] methodName = {MessageTag.CLASSMETHODNAME,method.getName()};                            // get method name
            sendMessage(methodName);
            Object[] returnType = {MessageTag.CLASSMETHODRETURNTYPE,method.getReturnType().getSimpleName()};  // get method return type
            sendMessage(returnType);
        }


        for (Field field : cls.getDeclaredFields()) {                                    // for every field declared in a class
            Object[] name = {MessageTag.CLASSFIELDNAME,field.getName()};                            // get field name
            sendMessage(name);
            Object[] returnType = {MessageTag.CLASSFIELDTYPE,field.getType().getSimpleName()};     // get field type
            sendMessage(returnType);
        }
    }

    public void reflectOnMethod(Object obj, String methodName, Class<?>... parameterTypes) { // Get information on a java method implemented in class by name


        Object[] mtdName = {MessageTag.METHODNAME,methodName};// get method name
        sendMessage(mtdName);

        Class<?> cls = obj.getClass();

        Object[] clsName = {MessageTag.CALLINGCLASS,cls.getSimpleName()};// get class
        sendMessage(mtdName);

        try {
            Method mtd = cls.getMethod(methodName, parameterTypes);                 // get method

            for (Class<?> param : mtd.getParameterTypes()) {                                // for each parameter type in list

            }
            Object[] returnType = {MessageTag.METHODRETURNTYPE,mtd.getReturnType().getSimpleName()};                 // get the method return type
            sendMessage(returnType);
        } catch (Exception e) {
            System.out.println("ERROR TESTING METHOD ");
            System.out.println(e.toString());
        }
    }

    @Override
    public void addObserver(IMessageObserver observer) {
        this.observerList.add(observer);
    }

    @Override
    public void removeObserver(IMessageObserver observer) {
        this.observerList.remove(observer);
    }

    @Override
    public void sendMessage(Object message) {
        for (IMessageObserver observer: observerList
             ) {
            observer.update(message);
        }
    }

}
