package com.EiriniManu;

/*
    This class represents an object that can expose some methods to test the program with.
*/

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.NameExpr;

import java.util.ArrayList;
import java.util.List;

public class TestMethod implements  ITestMethod{

    List<String> methodTargetStack = new ArrayList<>();
    DiagramStructure structure = new DiagramStructure();
    int parameterIndex = 0;

    public TestMethod(){
        methodTargetStack.add("TEST");
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

    public String test14(String s, int j, boolean bool){
      try {
          String e = "";
          e.toString();
      } catch (Exception e){
          System.out.println(e.toString());
      }
        return "26";
    }

    public String test15(String s, int j, boolean bool){
        try {
            String e = "";
            e.toString();
        } catch (Exception e){
            System.out.println(e.toString());
        }
        return "26";
    }

    public String test16(String s, int j, boolean bool){

        Node node = null;
        node.findFirst(NameExpr.class).isPresent();

        return "26";
    }

    public String test17(String s, int j, boolean bool){

        Node node = null;
        node.findAncestor(NameExpr.class).isPresent();

        return "26";
    }

    public String test18(String s, int j, boolean bool){        // RESOLVING THE TYPE OF
        Node node = null;
        if (node.findAncestor(NameExpr.class).isPresent()){
            s.equals(node.findAncestor(NameExpr.class).get());
        }

        return "26";
    }

    public String test19(String s, int j, boolean bool){
        Node node = null;
        if (node.findAncestor(NameExpr.class).isPresent()){
            s.equals(node.findAncestor(NameExpr.class).get().toString());
        }

        return "26";
    }




}
