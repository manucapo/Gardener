package com.EiriniManu;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.CatchClause;

public class NodeExplorerFactory {

    public static NodeExplorer create(Class<?> nodeType){
        if (nodeType == null){
            return  null;
        } else if (nodeType == MethodCallExpr.class){
            return new MethodNodeExplorer();
        } else if (nodeType == Parameter.class){
            return  new ParameterNodeExplorer();
        } else if (nodeType == VariableDeclarationExpr.class){
            return new VariableNodeExplorer();
        }  else if (nodeType == CatchClause.class){
            return new CatchNodeExplorer();
        }
        return  null;
    }
}
