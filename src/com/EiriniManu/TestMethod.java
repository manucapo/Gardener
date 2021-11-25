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
        Test2("2");  // BUG METHOD TARGET IS Test2 SHOULD BE THIS CLASS        ( FIXED NEEDS TEST )
        if (i == 2) {
            System.out.println("if testprint");
        }  else {
            System.out.println("else testprint");
        }
        String f = "TEST F";
        s.split("\\.");   // BUG  METHOD TARGET IS s SHOULD BE String ( OR THIS CLASS ? ) ( FIXED NEEDS TEST)
        f.split("\\.");  // BUG  METHOD TARGET IS s SHOULD BE String ( OR THIS CLASS ? )  ( FIXED NEEDS TEST)
        Math.abs(2);
        test4("a").split("\\.");  // BUG METHOD IS test4 AND split SHOULD BE SPLIT (detecting two method calls)
      //  test4("a.").split("\\.");  // BUG METHOD IS test4 SHOULD BE SPLIT
        return 26;
    }

    public String test4(String s){
        return "test4";
    }


}
