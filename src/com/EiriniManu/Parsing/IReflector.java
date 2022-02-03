package com.EiriniManu.Parsing;

/*
    This interface defines an object that can "reflect" on other java classes using the Java Reflection API (https://www.oracle.com/technical-resources/articles/java/javareflection.html)
    Such an object should allow the program to quickly obtain some contextual information on a method or a class.
*/

public interface IReflector {
    void reflectOnClass(Object o);
    void reflectOnMethod(Object o, String methodName, Class<?>... parameterTypes);
}
