package com.EiriniManu.IO;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import jnr.ffi.annotations.In;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DiagramFileWriter {

    public void createDiagramFile(String path){
        DiagramStructure structure = DiagramStructure.getInstance();
        List<Node> methodNodes = structure.getMethodCallNodes();
        List<Node> blockNodes = structure.getBlockNodes();
        List<String> methodBlock = structure.getMethodBlock();

        List<Node> openBlocks = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(path);
            writer.write("@startuml \n");
            writer.write("participant Actor \n");
            writer.write("Actor -> " + structure.getImplementingClassName() + ": " + structure.getMethodName() + "\n");
            writer.write("activate " + structure.getImplementingClassName() + "\n");
            int i = 0;
            int blockCount = 0;
            for (String method : structure.getMethodCalls()) {

            if(i > 0 && !methodBlock.get(i).equals("x")){
                int count = 0;
                if (!methodBlock.get(i).equals(methodBlock.get(i-1))) {
                    if (!methodBlock.get(i).equals("0")){

                    for (int j = openBlocks.size() - 1; j >= 0; j--) {
                        if (!openBlocks.get(j).isAncestorOf(blockNodes.get(Integer.parseInt(methodBlock.get(i)) - 1))) {
                            if (!openBlocks.get(j).equals(blockNodes.get(Integer.parseInt(methodBlock.get(i)) - 1))){
                                count++;
                            }

                        } else {
                            break;
                        }

                    }
                } else {
                        if (!methodBlock.get(i-1).equals("0")){
                            count = blockCount;
                        }
                    }
                }
                for (int j = 0; j < count ; j++) {
                    writer.write("end \n");
                    openBlocks.remove(openBlocks.size()-1);
                    blockCount--;
                }
            }

            if(!methodBlock.get(i).equals("x") ){
               if (!methodBlock.get(i).equals("0")){
                   if (blockCount != 0){


                   if (!openBlocks.contains(blockNodes.get(Integer.parseInt(methodBlock.get(i))-1))) {
                       String blockName = "";
                       String groupType = "alt";
                      if(blockNodes.get(Integer.parseInt(methodBlock.get(i))-1) instanceof IfStmt){
                                groupType = "alt";
                                blockName = "if";
                      } else if ( blockNodes.get(Integer.parseInt(methodBlock.get(i))-1) instanceof ForStmt){
                          groupType = "loop";
                          blockName = "for ";
                      } else if (blockNodes.get(Integer.parseInt(methodBlock.get(i))-1) instanceof WhileStmt){
                          groupType = "loop";
                          blockName = "while ";
                      }
                       writer.write(groupType + " " + blockName + " " + methodBlock.get(i) + "\n");
                       openBlocks.add(blockNodes.get(Integer.parseInt(methodBlock.get(i))-1));
                       blockCount++;
                   }
                   } else {
                       for (Node node : blockNodes){
                           if (node.isAncestorOf(blockNodes.get(Integer.parseInt(methodBlock.get(i))-1))){
                               if(!node.equals(blockNodes.get(Integer.parseInt(methodBlock.get(i))-1))){
                                   String blockName = "";
                                   String groupType = "alt";
                                   if(node instanceof IfStmt){
                                       groupType = "alt";
                                       blockName = "if";
                                   } else if ( node instanceof ForStmt){
                                       groupType = "loop";
                                       blockName = "for ";
                                   } else if (node instanceof WhileStmt){
                                       groupType = "loop";
                                       blockName = "while ";
                                   }
                                   writer.write(groupType + " " + blockName + " "  + blockNodes.indexOf(node) + "\n");
                                   openBlocks.add(node);
                                   blockCount++;
                               }

                           }
                       }


                       if (!openBlocks.contains(blockNodes.get(Integer.parseInt(methodBlock.get(i))-1))) {
                           String blockName = "";
                           String groupType = "alt";
                           if(blockNodes.get(Integer.parseInt(methodBlock.get(i))-1) instanceof IfStmt){
                               groupType = "alt";
                               blockName = "if";
                           } else if ( blockNodes.get(Integer.parseInt(methodBlock.get(i))-1) instanceof ForStmt){
                               groupType = "loop";
                               blockName = "for ";
                           } else if (blockNodes.get(Integer.parseInt(methodBlock.get(i))-1) instanceof WhileStmt){
                               groupType = "loop";
                               blockName = "while ";
                           }
                           writer.write(groupType + " " + blockName + " " + methodBlock.get(i) + "\n");
                           openBlocks.add(blockNodes.get(Integer.parseInt(methodBlock.get(i))-1));
                           blockCount++;
                       }

                   }
               }
            }

                if(structure.getMethodCallTargets().get(i).equals("this")){
                    writer.write(structure.getImplementingClassName() + " -> " + structure.getImplementingClassName() + ": " + method + "\n");
                    writer.write("activate " + structure.getImplementingClassName() + "\n");
                    writer.write(structure.getImplementingClassName() + "-->" + structure.getImplementingClassName() + '\n');
                    writer.write("deactivate " + structure.getImplementingClassName() + "\n");
                } else if (structure.getMethodCallTargets().get(i).contains("[]")){
                    writer.write(structure.getImplementingClassName() + " -> " + "__Array__" + ": " + method + "\n");
                    writer.write("activate " + "__Array__" + "\n");
                    writer.write("__Array__" + "-->" + structure.getImplementingClassName() + "\n");
                    writer.write("deactivate " + "__Array__" + "\n");
                } else if (structure.getMethodCallTargets().get(i).contains("LOSTMESSAGE")){
                    writer.write(structure.getImplementingClassName() + " ->x] " + ": " + method + "\n");
                } else {
                    writer.write(structure.getImplementingClassName() + " -> " + structure.getMethodCallTargets().get(i) + ": " + method + "\n");
                    writer.write("activate " + structure.getMethodCallTargets().get(i) + "\n");
                    writer.write(structure.getMethodCallTargets().get(i) + "-->" + structure.getImplementingClassName() + "\n");
                    writer.write("deactivate " + structure.getMethodCallTargets().get(i) + "\n");
                }

                i++;
            }

            for (int j = 0; j < blockCount ; j++) {
                writer.write("end \n");
            }
            writer.write("return \n");
            writer.write("@enduml \n");
            writer.close();
        }
        catch (IOException e) {
            System.out.println("ERROR WRITING FILE");
        }

    }

}


/*
  if methodBlock[i].find("top") >= 0 :
    print("ok")
    blockCount = 0
    countedblocknumber = [""]
  elif methodBlock[i] != blockname and not countedblocknumber.count(methodBlock[i].split(" ")[1]) > 0:
   tempdiag.write("alt " + methodBlock[i] + '\n')
   blockCount += 1
   blockparent = methodBlock[i].split(" ")[2]
   blocknumber = methodBlock[i].split(" ")[1]
   blockname = methodBlock[i]
   countedblocknumber.append(blocknumber)
   print("PARENT : ")
   print(blockparent)
   print("BLOCK : ")
   print(blocknumber)
   print("COUNT : ")
   print(blockCount)
 */
