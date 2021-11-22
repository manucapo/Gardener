package com.EiriniManu;

public interface IReflector {
    void ReflectOnClass(Object o, DiagramStructure diagramStructure);
    void ReflectOnMethod(Object o, String methodName,  DiagramStructure diagramStructure , Class<?>... parameterTypes);
}
