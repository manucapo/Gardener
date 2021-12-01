package com.EiriniManu.Parsing;

public interface IReflector {
    void ReflectOnClass(Object o);
    void ReflectOnMethod(Object o, String methodName, Class<?>... parameterTypes);
}