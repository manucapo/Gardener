package com.EiriniManu;


import java.lang.reflect.Method;

public class TestMethod implements  ITestMethod{

    public TestMethod(){
        // TODO CONSTRUCTOR
    }

    public int Test1(int i) {
        return i;
    }


    public int Test2(String s) {
        this.Test1(2);
        return 26;
    }

    public int Test3(String s) {
        this.Test1(2);
        this.Test2("2");
        System.out.println("testprint");
        return 26;
    }


}
