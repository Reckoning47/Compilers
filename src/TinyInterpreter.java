// CSCI 468
// Chris Cooper, Spencer Lawry, Matthew Gober
// Interpreter class for tiny code translation

import java.util.ArrayList;

public class TinyInterpreter {

    public ArrayList<String> tinyCode = new ArrayList<>();
    public ArrayList<String> code = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> st;
    public int regCounter = 0;

    public TinyInterpreter(ArrayList<ArrayList<ArrayList<String>>> st){
        this.st = st;
    }

    // check each node and decide on semantic action
    public void checkNode(ASTNode rootIn){
        ASTNode root = rootIn;
        switch (root.op){
            case "read":
                String [] read_variables = root.data.split(",");
                for (String entry:read_variables ) {
                    String type = getType(root.level, entry);
                    if(type.equals("STRING")){
                        code.add("READS "+ entry);
                        tinyCode.add("sys reads " + entry);
                    }
                    else if(type.equals("FLOAT")){
                        code.add("READF "+ entry);
                        tinyCode.add("sys readr " + entry);
                    }
                    else if(type.equals("INT")){
                        code.add("READI "+ entry);
                        tinyCode.add("sys readi " + entry);
                    }
                }
            case "write":
                String [] write_variables = root.data.split(",");
                for (String entry:write_variables ) {
                    String type = getType(root.level, entry);
                    if(type.equals("STRING")){
                        code.add("WRITES "+ entry);
                        tinyCode.add("sys writes " + entry);
                    }
                    else if(type.equals("FLOAT")){
                        code.add("WRITEF "+ entry);
                        tinyCode.add("sys writer " + entry);
                    }
                    else if(type.equals("INT")){
                        code.add("WRITEI "+ entry);
                        tinyCode.add("sys writei " + entry);
                    }
                }
            case "id":
                root.varData = root.data;
                String type = getType(root.level, root.data);
                root.dataType = type;
        }
        if (root.op.equals("assign")){
            String itemName = root.childList.get(0).data;
            String type = getType(root.childList.get(0).level, itemName);

            if(type.equals("FLOAT")){
                code.add("STOREF "+ root.childList.get(1).varData + " "+itemName);
                tinyCode.add("move "+ root.childList.get(1).varData + " "+itemName );
            }
            else if(type.equals("INT")){
                code.add("STOREI "+ root.childList.get(1).varData + " "+itemName);
                tinyCode.add("move "+ root.childList.get(1).varData + " "+itemName );
            }
        }
        else if (root.op.equals("id")){
            root.varData = root.data;
            String type = getType(root.level, root.data);
            root.dataType = type;
        }
        else if (root.op.equals("integer")){
            root.varData = "r"+regCounter;
            regCounter++;
            root.dataType = "INT";
            code.add("STOREI "+ root.data + " "+root.varData);
            tinyCode.add("move "+ root.data + " "+root.varData);
        }
        else if (root.op.equals("float")){
            root.varData = "r"+regCounter;
            regCounter++;
            root.dataType ="FLOAT";
            code.add("STOREF "+ root.data + " "+root.varData);
            tinyCode.add("move "+ root.data + " "+root.varData);
        }
        else if (root.op.equals("+")){
            root.varData = "r"+regCounter;
            regCounter++;
            root.dataType = root.childList.get(0).dataType;

            if(root.dataType.equals("INT")){
                code.add("ADDI "+ root.childList.get(1).varData + " "+root.childList.get(0).varData +" " +root.varData);
                tinyCode.add("move "+root.childList.get(1).varData +" "+root.varData);
                tinyCode.add("addi "+root.childList.get(0).varData + " " +root.varData);
            }
            else{
                code.add("ADDF "+ root.childList.get(1).varData + " "+root.childList.get(0).varData +" " +root.varData);
                tinyCode.add("move "+root.childList.get(1).varData +" "+root.varData);
                tinyCode.add("addr "+root.childList.get(0).varData + " " +root.varData);
            }
        }
        else if (root.op.equals("-")){
            root.varData = "r"+regCounter;
            regCounter++;
            root.dataType = root.childList.get(0).dataType;

            if(root.dataType.equals("INT")){
                code.add("SUBI "+ root.childList.get(1).varData + " "+root.childList.get(0).varData +" " +root.varData);
                tinyCode.add("move "+root.childList.get(1).varData +" "+root.varData);
                tinyCode.add("subi "+root.childList.get(0).varData + " " +root.varData);
            }else{
                code.add("SUBF "+ root.childList.get(1).varData + " "+root.childList.get(0).varData +" " +root.varData);
                tinyCode.add("move "+root.childList.get(1).varData +" "+root.varData);
                tinyCode.add("subr "+root.childList.get(0).varData + " " +root.varData);
            }
        }
        else if (root.op.equals("*")){
            root.varData = "r"+regCounter;
            regCounter++;
            root.dataType = root.childList.get(0).dataType;

            if(root.dataType.equals("INT")){
                code.add("MULI "+ root.childList.get(1).varData + " "+root.childList.get(0).varData +" " +root.varData);
                tinyCode.add("move "+root.childList.get(1).varData +" "+root.varData);
                tinyCode.add("muli "+root.childList.get(0).varData + " " +root.varData);
            }else{
                code.add("MULF "+ root.childList.get(1).varData + " "+root.childList.get(0).varData +" " +root.varData);
                tinyCode.add("move "+root.childList.get(1).varData +" "+root.varData);
                tinyCode.add("mulr "+root.childList.get(0).varData + " " +root.varData);

            }
        }
        else if (root.op.equals("/")){
            root.varData = "r"+regCounter;
            regCounter++;
            root.dataType = root.childList.get(0).dataType;

            if(root.dataType.equals("INT")){
                code.add("DIVI "+ root.childList.get(1).varData + " "+root.childList.get(0).varData +" " +root.varData);
                tinyCode.add("move "+root.childList.get(1).varData +" "+root.varData);
                tinyCode.add("divi "+root.childList.get(0).varData + " " +root.varData);
            }else{
                code.add("DIVF "+ root.childList.get(1).varData + " "+root.childList.get(0).varData +" " +root.varData);
                tinyCode.add("move "+root.childList.get(1).varData +" "+root.varData);
                tinyCode.add("divr "+root.childList.get(0).varData + " " +root.varData);
            }
        }
        else if (root.op.equals("varDec")){
            String [] read_variables = root.data.split(",");
            for (String entry:read_variables ) {
                code.add("DECL "+"VAR "+ entry);
                tinyCode.add("var " + entry);

            }
        }
        else if (root.op.equals("strDec")){
            String [] read_variables = root.data.split(",");
            for (String entry:read_variables ) {
                code.add("DECL "+ "STRING "+ entry);
                tinyCode.add("str " + entry);
            }
        }
        else if (root.op.equals("nop")){
            tinyCode.add("sys halt");
        }
    }

    public String getType(String levelInput, String varInput){
        String varType="";
        ArrayList<ArrayList<String>> levelList = new ArrayList<>();

        for(ArrayList<ArrayList<String>> stBlock:st) {
            String name_of_table = stBlock.get(0).get(0).split(" ")[2];
            if(name_of_table.equals(levelInput)){
                levelList = stBlock;
            }
        }
        for(ArrayList<String> stBlock:levelList) {
            if(stBlock.size()>1){
                String name = stBlock.get(1);
                if(name.equals(varInput)){
                    varType = stBlock.get(3);
                }
            }
        }
        return varType;
    }

    // recursively go through each node
    public void traverseTable(ASTNode root){
        for (int i=0; i < root.childList.size(); i++) {
            traverseTable(root.childList.get(i));
        }
        checkNode(root);
    }

    public void printTinyOutput(){
//        for (String line2:code) {
//            System.out.println(";" + line2);
//        }
        for (String line:tinyCode) {
            System.out.println(line);
        }
    }
}
