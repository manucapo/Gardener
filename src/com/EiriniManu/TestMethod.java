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
        String[] x = {"hes", "test"};
        s.split("\\.");
        f.split("\\.");
        x.clone();
        Math.abs(2);


        test4("a").split("\\.");
        test4("a").split("\\.");
        test4("a").split("\\.");
        test4("a").split("\\.").clone();          // bug missing clone
        System.out.println(s.toString());  // BUG DETECTING SYSTEM AS METHOD NAME
        return 26;
    }

    public String test4(String s){
        return "test4";
    }


}
