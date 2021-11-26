package com.EiriniManu;

/*
    This class represents an object that can expose some methods to test the program with.
*/

public class TestMethod implements  ITestMethod{

    public TestMethod(){
        // TODO CONSTRUCTOR
    }

    public int test1(int i) {
        return i;
    }

    public int test2(String s) {
        this.test1(2);
        return 26;
    }

    public int test3(String s, int i, boolean bool) {
        this.test1(2);
        test2("2");
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
        Math.abs(2);
        // test4("a").split("\\.").clone();          // bug on second run

       // System.out.println(s.toString());  // BUG DETECTING SYSTEM AS METHOD NAME
        return 26;
    }

    public String test4(String s){

        test4("a").split("\\.").clone();          // bug missing clone
        Math.abs(2);
        return "test4";
    }


}
