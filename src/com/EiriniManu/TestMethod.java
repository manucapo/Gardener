package com.EiriniManu;

/*
    This class represents an object that can expose some methods to test the program with.
*/

import com.EiriniManu.IO.DiagramStructure;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.NameExpr;

import java.util.ArrayList;
import java.util.List;

public class TestMethod implements ITestMethod {

    List<String> methodTargetStack = new ArrayList<>();
    DiagramStructure structure = DiagramStructure.getInstance();
    int parameterIndex = 0;
    String str = "";

    public TestMethod() {
        methodTargetStack.add("TEST");
        // TODO CONSTRUCTOR
    }

    public int test1(String s, int i, boolean bool) {
        this.test2("2", 1, true);
        return i;
    }

    public int test2(String s, int i, boolean bool) {
        test1("2", 1, true);
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
        test7("a", 2, true).split("\\.");
        return "2";
    }

    public String test9(String s, int i, boolean bool) {
        this.test7("a", 2, true).split("\\.");
        return "2";
    }


    public String test10(String s, int j, boolean bool) {
        System.out.println("if testprint");
        Math.abs(2);
        return "26";
    }

    public String test11(String s, int j, boolean bool) {

        System.out.println(s.toString());  // BUG DETECTING NESTED SYSTEM CALL
        return "26";
    }

    public String test12(String s, int j, boolean bool) {

        System.out.println(s.toString().split("").clone().toString());  // BUG DETECTING NESTED SYSTEM CALL
        return "26";
    }

    public String test13(String s, int j, boolean bool) {
        s.toString().split("").clone().toString();
        return "26";
    }

    public String test14(String s, int j, boolean bool) {
        try {
            DiagramStructure e = DiagramStructure.getInstance();
            str.split("");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "26";
    }

    public String test15(String s, int j, boolean bool) {
        try {
            String e = "";
            e.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "26";
    }

    public String test16(String s, int j, boolean bool) {

        Node node = null;
        node.findFirst(NameExpr.class).isPresent();

        return "26";
    }

    public String test17(String s, int j, boolean bool) {

        Node node = null;
        node.findAncestor(NameExpr.class).isPresent();

        return "26";
    }

    public String test18(String s, int j, boolean bool) {        // LIMITATION: RESOLVING THE TYPE OF GENERICS IS SOMETIMES IMPOSSIBLE
        Node node = null;
        if (node.findAncestor(NameExpr.class).isPresent()) {
            s.equals(node.findAncestor(NameExpr.class).get());
        }

        return "26";
    }

    public String test19(String s, int j, boolean bool) {
        Node node = null;
        if (node.findAncestor(NameExpr.class).isPresent()) {
            s.equals(node.findAncestor(NameExpr.class).get().toString());
        }

        return "26";
    }

    public String test20(String s, int j, boolean bool) {
        String f = "2";
        System.out.println(test1(s.toString(), Integer.parseInt(f), bool));
        return "26";
    }

    public String test21(String s, int j, boolean bool) {
        structure.getMethodName();
        return "26";
    }

    public String test22(String s, int j, boolean bool) {
        if (true) {
            test1("s", 2, true);
        }
        return "26";
    }

    public String test23(String s, int j, boolean bool) {
        if (true) {
            test1("s", 2, true);
            if (true) {
                test1("s", 2, true);
                if (true) {
                    test5("s", 2, true);
                    test6("s", 2, true);
                    if (true) {
                        test5("s", 2, true);
                        test6("s", 2, true);
                    }
                }
                if (true) {
                    test5("s", 2, true);
                    test6("s", 2, true);
                }
            }
        }
        test1("s", 2, true);
        if (true) {
            test2(test4("s", 2, true), 2, true);
            if (true) {
                test1("s", 2, true);
                if (true) {
                    test1("s", 2, true);
                    if (true) {
                        test5("s", 2, true);
                        test6("s", 2, true);
                        if (true) {
                            test5("s", 2, true);
                            test6("s", 2, true);
                        }
                    }
                    if (true) {
                        test5("s", 2, true);
                        test6("s", 2, true);
                    }
                }
            }
            test1("s", 2, true);
            if (true) {
                test5("s", 2, true);
                test6("s", 2, true);
            }
        }
        test12("s", 2, true);
        if (true) {
            test5("s", 2, true);
            test6("s", 2, true);
        }
        test6("s", 2, true);
        test6("s", 2, true);
        test6("s", 2, true);
        return "26";
    }

    public String test24(String s, int j, boolean bool) {
        if (true) {
            test1("s", 2, true);
            if (true) {
                test2("s", 2, true);
            }
        }

        if (true) {
            test1("s", 2, true);
            test1("s", 2, true);
        }
        return "26";
    }

    public String test25(String s, int j, boolean bool) {
        if (true) {
            test1("s", 2, true);

        }
        test3("s", 2, true);
        if (true) {
            test3("s", 2, true);
        }
        test3("s", 2, true);
        return "26";
    }

    public String test26(String s, int j, boolean bool) {
        test3("s", 2, true);
        if (true) {
            test1("s", 2, true);
            for (int i = 0; i < 2; i++) {
                test3("s", 2, true);
            }

        }
        test3("s", 2, true);
        for (int i = 0; i < 2; i++) {
            test3("s", 2, true);
            if (true) {
                test1("s", 2, true);
                for (int f = 0; f < 2; f++) {
                    test3("s", 2, true);
                }

            }
        }
        test3("s", 2, true);
        return "26";
    }

    public String test27(String s, int j, boolean bool) {
        test3("s", 2, true);
        for (int b = 0; b < 2; b++) {


        int x = 0;
        while (x < 2){
            if (true) {
                test1("s", 2, true);
                for (int i = 0; i < 2; i++) {
                    test3("s", 2, true);
                }
                int k = 0;
                while (k < 10) {
                    test3("s", 2, true);
                    if (true) {
                        test1("s", 2, true);
                        for (int i = 0; i < 2; i++) {
                            test3("s", 2, true);
                        }
                        k++;
                    }
                }
                test8("s", 2, true);
                for (int i = 0; i < 2; i++) {
                    test3("s", 2, true);
                    if (true) {
                        test1("s", 2, true);
                        for (int f = 0; f < 2; f++) {
                            test3("s", 2, true);
                        }

                    }
                }
                test3("s", 2, true);

            }
            test25("s", 2, true);
            x++;
        }
        test25("s", 2, true);
        test25("s", 2, true);
        if(true) {
            test25("s", 2, true);
        }
        x = 0;
        while (x < 2) {
            while (x < 2) {
                if (true) {
                    test1("s", 2, true);
                    for (int i = 0; i < 2; i++) {
                        test3("s", 2, true);
                    }
                    int k = 0;
                    while (k < 10) {
                        test3("s", 2, true);
                        if (true) {
                            test1("s", 2, true);
                            for (int i = 0; i < 2; i++) {
                                test3("s", 2, true);
                            }
                            k++;
                        }
                    }
                    test8("s", 2, true);
                    for (int i = 0; i < 2; i++) {
                        test3("s", 2, true);
                        if (true) {
                            test1("s", 2, true);
                            for (int f = 0; f < 2; f++) {
                                test3("s", 2, true);
                            }

                        }
                    }
                    test3("s", 2, true);

                }
                test25("s", 2, true);
                x++;
            }
        }
        }
        return "26";
    }


}