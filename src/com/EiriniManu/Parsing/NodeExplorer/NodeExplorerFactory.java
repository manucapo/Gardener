package com.EiriniManu.Parsing.NodeExplorer;

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
            return  new MethodNodeExplorer(observer);
        }
        else if (nodeType == Parameter.class){
            return  new ParameterNodeExplorer(observer);
        } else if (nodeType == VariableDeclarationExpr.class){
            return new VariableNodeExplorer(observer);
        }  else if (nodeType == CatchClause.class){
            return new CatchNodeExplorer(observer);
        }
        return  null;
    }
}
