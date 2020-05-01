// CSCI 468
// Chris Cooper, Spencer Lawry, Matthew Gober
// Node class for abstract syntax tree

import java.util.ArrayList;

public class ASTNode {
    ASTNode parent;
    String op;
    String data;
    String dataType;
    ArrayList<ASTNode> childList = new ArrayList<ASTNode>();

    String level;
    String varData;



    public ASTNode(){
        op ="nop";
    }
    public void addChild(ASTNode child) {
        this.childList.add(child);
    }
}