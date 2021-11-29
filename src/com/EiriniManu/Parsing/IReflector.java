package com.EiriniManu.Parsing;

import com.EiriniManu.Parsing.DiagramStructure;

public interface IReflector {
    void ReflectOnClass(Object o);
    void ReflectOnMethod(Object o, String methodName, Class<?>... parameterTypes);
}
