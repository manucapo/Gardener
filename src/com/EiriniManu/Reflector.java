package com.EiriniManu;

/*
    This class represents an object that can "reflect" on other java classes using the Java Reflection API (https://www.oracle.com/technical-resources/articles/java/javareflection.html)
    This allows us to quickly obtain some contextual information on the method we want to make a diagram of.
    This is the first layer of information extraction in our process.
*/

import java.lang.reflect.Method;           // Import Java Reflection API

public class Reflector implements IReflector {

    public Reflector() {
        //TODO CONSTRUCTOR
    }

    public void ReflectOnClass(Object obj, DiagramStructure diagramStructure) {   // Get information on a java class and update diagram structure
        Class<?> cls = obj.getClass();                                           // get class
        System.out.println("CLASS ------------------------- ");
        System.out.println("CLASS NAME -> " + cls.getName());                  // get class name
        diagramStructure.setImplementingClassName(cls.getName());              // update diagram structure

        System.out.println("CLASS METHODS -> ");

        for (Method method : cls.getDeclaredMethods()) {                      // for every Method declared in the class
            System.out.println(method.getName());                             // get method name
            diagramStructure.addClassMethodName(method.getName());            // update diagram structure
        }

    }

    public void ReflectOnMethod(Object obj, String methodName, DiagramStructure diagramStructure, Class<?>... parameterTypes) { // Get information on a java method implemented in class by name

        Class<?> cls = obj.getClass();                                            // get class

        try {
            Method mtd = cls.getMethod(methodName, parameterTypes);                 // get method

            System.out.println(" \n");
            System.out.println("METHOD ------------------------- ");
            System.out.println("METHOD NAME -> " + mtd.getName());                           // get method name
            System.out.println("DECLARING CLASS -> " + mtd.getDeclaringClass().getName());   // get declaring class name
            System.out.println("DEFAULT VALUE-> " + mtd.getDefaultValue());                  // get default value
            System.out.println("PARAMETER COUNT -> " + mtd.getParameterCount());             // get parameter count

            System.out.println("PARAMETER TYPE -> ");
            for (Class<?> param : mtd.getParameterTypes()) {                                // for each parameter type in list
                System.out.println(param.getName());                                         // get name of type
                diagramStructure.addParameterTypeName(param.getName());                      // update diagram structure information
            }
            System.out.println("RETURN TYPE -> " + mtd.getReturnType());                      // get return type

        } catch (Exception e) {
            System.out.println("ERROR TESTING METHOD ");
            System.out.println(e.toString());
        }
    }
}
