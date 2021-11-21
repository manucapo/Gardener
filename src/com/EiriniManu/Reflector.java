package com.EiriniManu;

import java.lang.reflect.Method;

public class Reflector implements IReflector{

    public Reflector(){
        //TODO CONSTRUCTOR
    }

    public void ReflectOnClass(Object o){
        Class cls = o.getClass();
        System.out.println("CLASS ------------------------- ");
        System.out.println("CLASS NAME -> " + getClass().getName());
        System.out.println("CLASS METHODS -> ");
        for (Method method : cls.getDeclaredMethods()) {
            System.out.println(method.getName());

        }
    }

    public void ReflectOnMethod(Object o, String methodName, Class<?>... parameterTypes) {
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
            }

            System.out.println("RETURN TYPE -> " + mtd.getReturnType());

        }catch (Exception e){
            System.out.println("ERROR TESTING METHOD ");
            System.out.println(e.toString());
        }
    }
}
