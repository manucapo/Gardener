package com.EiriniManu;

/*
    This class represents an object that can expose some methods to test the program with.
*/

import java.util.ArrayList;
import java.util.List;

public class TestMethod implements  ITestMethod{

    public TestMethod(){
        // TODO CONSTRUCTOR
    }

    public int test1(String s, int i, boolean bool) {
        this.test2("2",1,true);
        return i;
    }

    public int test2(String s, int i, boolean bool) {
       test1("2",1,true);
        return 26;
    }

    public int test3(String s, int i, boolean bool) {
        System.out.println("test");
        return 2;
    }


    public String test4(String s, int i, boolean bool) {
        s.split("\\.");
        return "2";
    }

    public String test5(String s, int i, boolean bool) {
        String f = "TEST F";
        f.split("\\.");
        return "2";
    }

    public String test6(String s, int i, boolean bool) {
        String[] x = {"hes", "test"};
        x.clone();
        return "2";
    }

    public String test7(String s, int i, boolean bool) {
        Math.abs(2);
        return "2";
    }

    public String test8(String s, int i, boolean bool) {
        test7("a", 2, true).split("\\.");          // BUG HERE
        return "2";
    }

    public String test9(String s, int i, boolean bool) {
        this.test7("a", 2, true).split("\\.");       // BUG HERE
        return "2";
    }


    public String test10(String s, int j, boolean bool){
        System.out.println("if testprint");
        Math.abs(2);
        return "26";
    }

    public String test11(String s, int j, boolean bool){

              System.out.println(s.toString());  // BUG DETECTING NESTED SYSTEM CALL
       return "26";
    }

    public String test12(String s, int j, boolean bool){

        System.out.println(s.toString().split("").clone().toString());  // BUG DETECTING NESTED SYSTEM CALL
        return "26";
    }

    public String test13(String s, int j, boolean bool){
        s.toString().split("").clone().toString();
        return "26";
    }



}
