package com.EiriniManu;

/*
    This class represents an object that can "reflect" on other java classes using the Java Reflection API (https://www.oracle.com/technical-resources/articles/java/javareflection.html)
    This allows us to quickly obtain some contextual information on the method we want to make a diagram of.
    This is the first layer of information extraction in our process.
*/

import java.lang.reflect.Field;
import java.lang.reflect.Method;           // Import Java Reflection API
import java.lang.reflect.Parameter;

public class Reflector implements IReflector {

    public Reflector() {
        //TODO CONSTRUCTOR
    }

    public void ReflectOnClass(Object obj, DiagramStructure diagramStructure) {   // Get information on a java class and update diagram structure
        Class<?> cls = obj.getClass();                                           // get class
        diagramStructure.setImplementingClassName(cls.getSimpleName());              // update diagram structure



        for (Method method : cls.getDeclaredMethods()) {                      // for every Method declared in the class
            diagramStructure.addClassMethodName(method.getName());            // update diagram structure
            diagramStructure.addClassMethodReturnType(method.getReturnType().getSimpleName());
        }


        for (Field field : cls.getDeclaredFields()) {
            diagramStructure.addClassFieldNames(field.getName());
            diagramStructure.addGetClassFieldTypes(field.getType().getSimpleName());
        }
    }

    public void ReflectOnMethod(Object obj, String methodName, DiagramStructure diagramStructure, Class<?>... parameterTypes) { // Get information on a java method implemented in class by name

        Class<?> cls = obj.getClass();                                            // get class

        try {
            Method mtd = cls.getMethod(methodName, parameterTypes);                 // get method

            for (Class<?> param : mtd.getParameterTypes()) {                                // for each parameter type in list
            }
            diagramStructure.setMethodReturnType(mtd.getReturnType().getSimpleName());

        } catch (Exception e) {
            System.out.println("ERROR TESTING METHOD ");
            System.out.println(e.toString());
        }
    }
}
