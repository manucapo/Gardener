package com.EiriniManu.IO;

import com.github.javaparser.ast.Node;
import jnr.ffi.annotations.In;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DiagramFileWriter {

    public void createDiagramFile(String path){
        DiagramStructure structure = DiagramStructure.getInstance();
        List<Node> blockNodes = structure.getBlockNodes();
        List<String> methodBlock = structure.getMethodBlock();
        try{
            FileWriter writer = new FileWriter(path);
            writer.write("@startuml \n");
            writer.write("participant Actor \n");
            writer.write("Actor -> " + structure.getImplementingClassName() + ": " + structure.getMethodName() + "\n");
            writer.write("activate " + structure.getImplementingClassName() + "\n");
            int i = 0;
            int blockCount = 0;
            for (String method : structure.getMethodCalls()) {

                if( i != structure.getMethodCalls().size() -1 && !structure.getMethodBlock().get(i).contains("top")) {
                 if (!structure.getMethodBlock().get(i+1).equals(structure.getMethodBlock().get(i))) {
                    if (structure.getMethodBlock().get(i+1).contains("top")){
                        writer.write("alt " + structure.getMethodBlock().get(i) + "\n");
                        blockCount++;
                    }
                    else if (structure.getMethodBlock().get(i-1).contains("top")) {
                        writer.write("alt " + structure.getMethodBlock().get(i) + "\n");
                        blockCount++;
                    }
                 }
                }

                if(structure.getMethodCallTargets().get(i) == "this"){
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

                if( i == structure.getMethodCalls().size() -1){
                    for (int j = 0; j < blockCount; j++) {
                        writer.write("end \n");;
                    }
                } else{
                    String blockMethod = structure.getMethodBlock().get(i);
                    System.out.println(" BLOCK METHOD : " + blockMethod);
                    String blockType = structure.getMethodBlock().get(i).split(" ")[0];
                    System.out.println(" BLOCK TYPE : " + blockType);
                    int blockId = Integer.parseInt(structure.getMethodBlock().get(i).split(" ")[1]);
                    System.out.println(" BLOCK ID : " + blockId);
                    int blockParent = Integer.parseInt(structure.getMethodBlock().get(i).split(" ")[2]);
                    System.out.println(" BLOCK PARENT : " + blockParent);
                    if(blockParent <=  Integer.parseInt(structure.getMethodBlock().get(i+1).split(" ")[2])){
                        if (structure.getMethodBlock().get(i+1).contains("top")){
                            writer.write("end \n");
                            blockCount--;
                        }

                    }

                }
                System.out.println("-----------------------------------------------------");
                i++;
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
