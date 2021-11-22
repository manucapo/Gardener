package com.EiriniManu;

/*
    This class represents an object that can "reflect" on other java classes using the Java Reflection API (https://www.oracle.com/technical-resources/articles/java/javareflection.html)
    This allows us to quickly obtain some contextual information on the method we want to make a diagram off.
*/

import java.lang.reflect.Method;

public class Reflector implements IReflector{

    public Reflector(){
        //TODO CONSTRUCTOR
    }

    public void ReflectOnClass(Object o, DiagramStructure diagramStructure){
        Class<?> cls = o.getClass();
        System.out.println("CLASS ------------------------- ");
        System.out.println("CLASS NAME -> " + cls.getName());
        diagramStructure.setImplementingClassName(cls.getName());

        System.out.println("CLASS METHODS -> ");

        for (Method method : cls.getDeclaredMethods()) {
            System.out.println(method.getName());
            diagramStructure.addClassMethodName(method.getName());
        }

    }

    public void ReflectOnMethod(Object o, String methodName, DiagramStructure diagramStructure , Class<?>... parameterTypes) {
        try {

            Class<?> cls = o.getClass();
            Method mtd =  cls.getMethod(methodName,parameterTypes);

            System.out.println(" \n");

            System.out.println("METHOD ------------------------- ");
            System.out.println("METHOD NAME -> " + mtd.getName());
            System.out.println("DECLARING CLASS -> " + mtd.getDeclaringClass().getName());
            System.out.println("DEFAULT VALUE-> " + mtd.getDefaultValue());
            System.out.println("PARAMETER COUNT -> " + mtd.getParameterCount());
            System.out.println("PARAMETER TYPE -> ");
            for (Class<?> param : mtd.getParameterTypes())  {
                System.out.println(param.getName());
                diagramStructure.addParameterTypeName(param.getName());
            }
            System.out.println("RETURN TYPE -> " + mtd.getReturnType());

        }catch (Exception e){
            System.out.println("ERROR TESTING METHOD ");
            System.out.println(e.toString());
        }
    }
}
