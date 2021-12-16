package com.EiriniManu;

/*
    This class represents an object that can expose some methods to test the program with.
*/

import com.EiriniManu.IO.DiagramStructure;
import com.EiriniManu.Parsing.NodeExplorer.CatchNodeExplorer;
import com.EiriniManu.Parsing.NodeExplorer.MethodNodeExplorer;
import com.EiriniManu.Parsing.NodeExplorer.NodeExplorerFactory;
import com.EiriniManu.Parsing.NodeExplorer.ParameterNodeExplorer;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.NameExpr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestMethod {

    MethodNodeExplorer explorer;
    List<String> methodTargetStack = new ArrayList<>();
    DiagramStructure structure = DiagramStructure.getInstance();
    int parameterIndex = 0;
    String str = "";

    public TestMethod() {
        methodTargetStack.add("TEST");
        // TODO CONSTRUCTOR
    }
    // SAFE PARSER TESTS-------------------------------------------------------------------------------------------------------------------------------------------
    public int test1(String s, int i, boolean bool) {             // The parser can parse single methods and attribute them to the target
        test1("2", 1, true);
        return i;
    }

    public int test2(String s, int i, boolean bool) {
        DiagramStructure.getInstance();                    // The parser can resolve call targets using scope information
        return i;
    }

    public int test3(String s, int i, boolean bool) {               // the parser can correctly identify the order of method calls
        test1("2", 1, true);
        this.test2("2", 1, true);
        test3("2", 1, true);
        return 2;
    }


    public String test4(String s, int i, boolean bool) {                // the safe parser ignores calls that are not made by the current project package
        test1("2", 1, true);
        Math.abs(2);                       // ignored
        DiagramStructure.getInstance();
        test3("2", 1, true);
        return "s";
    }

    public String test5(String s, int i, boolean bool) {
        test1( test4("2", 1, true), 1, true);     // the parser can deal with method that are passed as arguments of other methods and still identify the correct call order
        return "2";
    }

    public String test6(String s, int i, boolean bool) {         // the parser can resolve the call target of fields defined in the current class
        explorer.checkNode(null);
        return "2";
    }

    public String test7(CatchNodeExplorer n, int i, boolean bool) {         // the parser can resolve the call target of arguments passed to the original method call
        n.checkNode(null);
        return "2";
    }

    public String test8(String s, int i, boolean bool) {           // the parser can resolve the call target of fields defined inside the original method call
        ParameterNodeExplorer n = (ParameterNodeExplorer) NodeExplorerFactory.create(Parameter.class);
        n.checkNode(null);
        return "";
    }

    // REFLECTION PARSER TESTS-------------------------------------------------------------------------------------------------------------------------------------------
    public String test9(String s, int i, boolean bool) {         // the reflection parser can resolve the target of calls made by packages other than the current project package.
        Math.abs(2);                                             // (user must provide the external dependencies)
        return "";
    }


    public String test10(String s, int j, boolean bool) {     // the reflection parser can resolve the target of calls made using field acess expressions (i.e System.out)
        System.out.println("if testprint");
        return "26";
    }

    public String test11(String s, int j, boolean bool) {    // the reflection parser can recognize and correctly resolve  container classes

        int[] ints = {0,2};
        ints.clone();

        List<String> strings = new ArrayList<>();
        strings.get(0);

        Optional<Integer> i;
        i =Optional.of((Integer) 2);
        return "26";
    }

    public String test12(String s, int j, boolean bool) {            // the reflection parser can resolve the target of calls made by objects inside container classes
        String str = "HeLLO";
        str.split("")[0].toString();
        return "26";
    }

    public String test13(String s, int j, boolean bool) {              // the reflection parser can deal with containers in deeply nested calls
        System.out.println(s.toString().split("").clone().toString());
        return "26";
    }

    public String test14(String s, int j, boolean bool) {              // the reflection parser can resolve symbols declared inside of catch blocks
        try {
            DiagramStructure e = DiagramStructure.getInstance();
            str.split("");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "26";
    }

    public String test15(String s, int j, boolean bool) {          // the reflection parser can differentiate symbols declared inside of catch blocks to those declared outside of them
        try {
            String e = "";
            e.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "26";
    }

    public String test16(String s, int j, boolean bool) {    // the reflection parser can SOMETIMES resolve generic methods from scope context
        Node node = null;
        if (node.findAncestor(NameExpr.class).isPresent()) {
            s.equals(node.findAncestor(NameExpr.class).get());
        }

        return "26";
    }

    public String test17(String s, int j, boolean bool) { // the reflection parser CAN NOT resolve generic types without any further context information

        Node node = null;
        if (node.findAncestor(NameExpr.class).isPresent()) {
            s.equals(node.findAncestor(NameExpr.class).get().toString());
        }

        return "26";
    }

    public String test18(String s, int j, boolean bool) {        // the reflection parser can resolve types using different scope types even inside deeply nested calls.
        String f = "2";
        System.out.println(test1(s.toString(), Integer.parseInt(f), bool));
        return "26";
    }

    // BLOCK PARSER TESTS-------------------------------------------------------------------------------------------------------------------------------------------
    public String test19(String s, int j, boolean bool) {  // the block parser can identify if blocks
        if (true) {
            test1("s", 2, true);
        }
        return "26";
    }

    public String test20(String s, int j, boolean bool) {   // the block parser can identify for blocks
        for (int b = 0; b < 2; b++) {
            test1("s", 2, true);
        }
        return "";
    }

    public String test21(String s, int j, boolean bool) {    // the block parser can identify while blocks
        int cnt = 0;
        while (cnt < 2) {
            test1("s", 2, true);
            cnt++;
        }
        return "26";
    }

    public String test22(String s, int j, boolean bool) {  // the block parser can identify  nested blocks
        if (true) {
            test1("s", 2, true);
            explorer.checkNode(null);
            if (true) {
                test2("s", 2, true);
            }
        }

        if (true) {
            test1("s", 2, true);
            structure.getMethodCaller();
            test1("s", 2, true);
        }
        return "26";
    }

    public String test23(String s, int j, boolean bool) {      // the block parser can identify DEEPLY  nested blocks of different kinds
        test3("s", 2, true);
        for (int b = 0; b < 2; b++) {

            int x = 0;
            while (x < 2){
                if (true) {
                    test1("s", 2, true);
                    for (int i = 0; i < 2; i++) {
                        test3("s", 2, true);
                        structure.getMethodCaller();
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
                structure.getMethodCaller();
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
                                explorer.checkNode(null);
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
        test3("s", 2, true);
        explorer.checkNode(null);
        return "26";
    }

    // DEEP PARSER TESTS-------------------------------------------------------------------------------------------------------------------------------------------
    public String test24(String s, int j, boolean bool) {   // the deep parser can parse recursively nested methods ( currently only methods declared on the same class )
        test25("s", 2, true);
        return "26";
    }

    public String test25(String s, int j, boolean bool) {
            test24("s", 2, true);
        return "26";
    }

    public String test26(String s, int j, boolean bool) {  // the deep parser correctly recognizes the order of calls even if deeply recursively nested
        test27("s", 2, true);
        DiagramStructure.getInstance();
        return "26";
    }

    public String test27(String s, int j, boolean bool) {
        structure.getMethodCaller();
        test26("s", 2, true);
        explorer.checkNode(null);
        return "26";
    }

}