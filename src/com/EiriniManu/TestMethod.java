package com.EiriniManu;

/*
    This class represents an object that can expose some methods to test the program with.
*/

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

    public int Test3(String s, int i, boolean bool) {
        this.Test1(2);
        this.Test2("2");
        if (i == 2) {
            System.out.println("if testprint");
        }  else {
            System.out.println("else testprint");
        }
        Math.abs(2);
        return 26;
    }


}
