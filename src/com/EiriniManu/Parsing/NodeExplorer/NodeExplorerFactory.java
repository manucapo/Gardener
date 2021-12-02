package com.EiriniManu.Parsing.NodeExplorer;

import com.EiriniManu.IO.DiagramStructure;
import com.EiriniManu.Messaging.IMessageObserver;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.CatchClause;

public class NodeExplorerFactory {

    public static NodeExplorer create(Class<?> nodeType){
        if (nodeType == null){
            return  null;
        } else if (nodeType == MethodCallExpr.class){
            return  new MethodNodeExplorer(DiagramStructure.getInstance());
        }
        else if (nodeType == Parameter.class){
            return  new ParameterNodeExplorer(DiagramStructure.getInstance());
        } else if (nodeType == VariableDeclarationExpr.class){
            return new VariableNodeExplorer(DiagramStructure.getInstance());
        }  else if (nodeType == CatchClause.class){
            return new CatchNodeExplorer(DiagramStructure.getInstance());
        }
        return  null;
    }
}
