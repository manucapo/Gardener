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
        Test2("2");
        if (i == 2) {
            System.out.println("if testprint");
        }  else {
            System.out.println("else testprint");
        }
        String f = "TEST F";
        s.split("\\.");
        f.split("\\.");
        Math.abs(2);
        test4("a").split("\\.")[0].split("\\.");
        test4("a").split("\\.");
        test4("a").split("\\.").clone(); // BUG METHOD IS test4 AND split SHOULD BE SPLIT (detecting three method calls)
        return 26;
    }

    public String test4(String s){
        return "test4";
    }


}
