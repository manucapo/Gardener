package com.EiriniManu.Parsing;

import com.EiriniManu.Messaging.IMessageObserver;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.CatchClause;

public class NodeExplorerFactory {

    public static NodeExplorer create(Class<?> nodeType, IMessageObserver observer){
        if (nodeType == null){
            return  null;
        } else if (nodeType == MethodCallExpr.class){

        }
        else if (nodeType == Parameter.class){
            NodeExplorer explorer = new ParameterNodeExplorer(observer);
            return  new ParameterNodeExplorer(observer);
        } else if (nodeType == VariableDeclarationExpr.class){
            NodeExplorer explorer = new VariableNodeExplorer(observer);
            return new VariableNodeExplorer(observer);
        }  else if (nodeType == CatchClause.class){
            NodeExplorer explorer = new CatchNodeExplorer(observer);
            return new CatchNodeExplorer(observer);
        }
        return  null;
    }
}
