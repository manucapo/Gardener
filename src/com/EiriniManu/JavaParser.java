package com.EiriniManu;


import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;

import java.nio.file.Paths;



public class JavaParser implements IJavaParser {

    public JavaParser() {
        //TODO CONSTRUCTOR
    }

    public SourceRoot SetSourceRoot(String path, String packageName){
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.packageAbsolutePath(path,packageName));
        return  sourceRoot;
    }

    public CompilationUnit ParseFile(String fileName, SourceRoot sourceRoot){
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());

        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);

        sourceRoot.getParserConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = sourceRoot.parse("", fileName);

        Log.info("DONE PARSING");
        /*
        "C:\\Users\\manol\\Desktop\\Softwarentwicklung VF\\gardener\\src"
        "com.EiriniManu"
        "JavaParser.java"
        */


       // sourceRoot.saveAll(Paths.get("C:\\Users\\manol\\Desktop\\Softwarentwicklung VF\\PARSED"));

        return cu;
    }

    public void ParseMethodFromClass(CompilationUnit cu, String className, String methodName) {
        try {

            ClassOrInterfaceDeclaration clsX = cu.getClassByName(className).get();

            MethodVisitor methodVisitor = new MethodVisitor();

            for (MethodDeclaration method : clsX.getMethods()) {


                if (method.getName().toString().equals(methodName)) {
                    System.out.println(method.getName().toString());
                    method.walk(Node.TreeTraversal.PREORDER, this::CheckNodeMethod);

                    // method.accept(new MethodVisitor(), null); ALTERNATE METHOD TO CHECK NODES
                }
            }


        } catch (Exception e) {
            System.out.println("ERROR PARSING JAVA FILE");
            System.out.println(e.toString());
        }
    }

    public void CheckNodeMethod(Node node) {
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);

        if (node instanceof MethodCallExpr) {
            System.out.println("--------------------------------");
            System.out.println(node.toString());
            System.out.println(((MethodCallExpr) node).getTypeArguments());


            for (Node chilNode : node.getChildNodes() ) {

            }
            System.out.println("--------------------------------");

        }
    }

    static class MethodVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(MethodCallExpr n, Void arg) {

            System.out.println(n.getName());
            super.visit(n, arg);
        }
    }

}
