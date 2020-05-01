// CSCI 468
// Chris Cooper, Spencer Lawry, Matthew Gober
// Realized class of LittleListener inherited from LittleBaseListener

import java.util.Stack;
import java.util.ArrayList;

public class CustomNewListener extends LittleBaseListener {
    // Step 4 variables
    public ASTNode rootASTNode;
    public int blockLevel = 1;
    public int tableReference = 0;
    public ArrayList<ArrayList<ArrayList<String>>> st = new ArrayList<>();
    public Stack<ArrayList<ArrayList<String>>> parseStack;
    public Stack<ArrayList<ArrayList<String>>> tempStack;
    public ArrayList<ArrayList<String>> parseTreeSymbols;
    public ArrayList<String> tempSymbolItem;

    public CustomNewListener() {
        parseStack = new Stack<>();
        tempStack = new Stack<>();
        rootASTNode = new ASTNode();
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterStart(LittleParser.StartContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitStart(LittleParser.StartContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterProgram(LittleParser.ProgramContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitProgram(LittleParser.ProgramContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterId(LittleParser.IdContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitId(LittleParser.IdContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterPgm_body(LittleParser.Pgm_bodyContext ctx) {
        //Setup global scope
        parseTreeSymbols = new ArrayList<>();
        parseTreeSymbols.add(new ArrayList<>());
        parseTreeSymbols.get(0).add("Symbol table GLOBAL");
        parseStack.push(parseTreeSymbols);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitPgm_body(LittleParser.Pgm_bodyContext ctx) {
        st.add(parseStack.pop());
        while(!parseStack.empty()){
            parseTreeSymbols = parseStack.peek();
            for(int i = 1; i < parseTreeSymbols.size(); i++){
                st.get(tableReference).add(parseTreeSymbols.get(i));
            }
            tempStack.push(parseStack.pop());
        }
        tableReference++;
        while(!tempStack.empty()){
            parseStack.push(tempStack.pop());
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterDecl(LittleParser.DeclContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitDecl(LittleParser.DeclContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterString_decl(LittleParser.String_declContext ctx) {
        //new String item added, create arraylist holder and push to stack
        tempSymbolItem = new ArrayList<>();
        tempSymbolItem.add("name");
        tempSymbolItem.add(ctx.id().getText());
        tempSymbolItem.add("type");
        tempSymbolItem.add("STRING");
        tempSymbolItem.add("value");
        tempSymbolItem.add(ctx.str().getText());
        parseStack.peek().add(tempSymbolItem);

        // create node to hold
        ASTNode stringNode = new ASTNode();
        String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
        stringNode.parent = rootASTNode;
        stringNode.op = "strDec";
        stringNode.data = ctx.getChild(1).getText() +" "+ctx.getChild(3).getText();
        stringNode.level = stLevel;
        rootASTNode.childList.add(stringNode);
        rootASTNode = stringNode;

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitString_decl(LittleParser.String_declContext ctx) {
        rootASTNode = rootASTNode.parent;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterStr(LittleParser.StrContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitStr(LittleParser.StrContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterVar_decl(LittleParser.Var_declContext ctx) {
        //new variable added, push to stack
        String type = ctx.var_type().getText();
        String [] varNameList = ctx.id_list().getText().split(",");
        for (String name:varNameList) {
            tempSymbolItem = new ArrayList<>();
            tempSymbolItem.add("name");
            tempSymbolItem.add(name);
            tempSymbolItem.add("type");
            tempSymbolItem.add(type);
            parseStack.peek().add(tempSymbolItem);
        }
        ASTNode varNode = new ASTNode();
        String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
        varNode.parent = rootASTNode;
        varNode.op = "varDec";
        varNode.data = ctx.getChild(1).getText();
        varNode.level = stLevel;
        rootASTNode.childList.add(varNode);
        rootASTNode = varNode;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitVar_decl(LittleParser.Var_declContext ctx) {
        rootASTNode = rootASTNode.parent;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterVar_type(LittleParser.Var_typeContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitVar_type(LittleParser.Var_typeContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterAny_type(LittleParser.Any_typeContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitAny_type(LittleParser.Any_typeContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterId_list(LittleParser.Id_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitId_list(LittleParser.Id_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterId_tail(LittleParser.Id_tailContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitId_tail(LittleParser.Id_tailContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterParam_decl_list(LittleParser.Param_decl_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitParam_decl_list(LittleParser.Param_decl_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterParam_decl(LittleParser.Param_declContext ctx) {
        //new parameter added, push variable
        tempSymbolItem = new ArrayList<>();
        tempSymbolItem.add("name");
        tempSymbolItem.add(ctx.id().getText());
        tempSymbolItem.add("type");
        tempSymbolItem.add(ctx.var_type().getText());
        
        // make new paramNode
        ASTNode paramNode = new ASTNode();
        String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
        paramNode.parent = rootASTNode;
        paramNode.op = "varDec";
        paramNode.data = ctx.id().getText();
        paramNode.level = stLevel;
        rootASTNode.childList.add(paramNode);
        rootASTNode = paramNode;

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitParam_decl(LittleParser.Param_declContext ctx) {
        rootASTNode = rootASTNode.parent;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterParam_decl_tail(LittleParser.Param_decl_tailContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitParam_decl_tail(LittleParser.Param_decl_tailContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterFunc_declarations(LittleParser.Func_declarationsContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFunc_declarations(LittleParser.Func_declarationsContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterFunc_decl(LittleParser.Func_declContext ctx) {
        // Update symbol table from parse tree
        parseTreeSymbols = new ArrayList<>();
        parseTreeSymbols.add(new ArrayList<>());
        parseTreeSymbols.get(0).add("Symbol table "+ctx.id().getText());
        parseStack.push(parseTreeSymbols);

        ASTNode funcNode = new ASTNode();
        String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
        funcNode.level = stLevel;
        funcNode.op = "function";
        funcNode.parent = rootASTNode;
        funcNode.data = ctx.getChild(2).getText();
        rootASTNode.childList.add(funcNode);
        rootASTNode = funcNode;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFunc_decl(LittleParser.Func_declContext ctx) {
        // increment through parseStack adding to symbol table
        st.add(parseStack.pop());
        while(!parseStack.empty()){
            parseTreeSymbols = parseStack.peek();
            for(int i = 1; i < parseTreeSymbols.size(); i++){
                st.get(tableReference).add(parseTreeSymbols.get(i));
            }
            tempStack.push(parseStack.pop());
        }
        tableReference++;
        while(!tempStack.empty()){
            parseStack.push(tempStack.pop());
        }
        rootASTNode = rootASTNode.parent;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterFunc_body(LittleParser.Func_bodyContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFunc_body(LittleParser.Func_bodyContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterStmt_list(LittleParser.Stmt_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitStmt_list(LittleParser.Stmt_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterStmt(LittleParser.StmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitStmt(LittleParser.StmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterBase_stmt(LittleParser.Base_stmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitBase_stmt(LittleParser.Base_stmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterAssign_stmt(LittleParser.Assign_stmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitAssign_stmt(LittleParser.Assign_stmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterAssign_expr(LittleParser.Assign_exprContext ctx) {
        // create initial expression entry
        if(ctx.getChildCount() > 2){
            ASTNode assignNode = new ASTNode();
            String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
            assignNode.level = stLevel;
            assignNode.parent = rootASTNode;
            assignNode.op = "assign";
            rootASTNode.childList.add(assignNode);

            ASTNode idNode = new ASTNode();
            idNode.level = stLevel;
            idNode.parent = assignNode;
            idNode.op = "id";
            idNode.data = ctx.getChild(0).getText();
            assignNode.childList.add(idNode);
            rootASTNode = assignNode;
        }

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitAssign_expr(LittleParser.Assign_exprContext ctx) {
        rootASTNode = rootASTNode.parent;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterRead_stmt(LittleParser.Read_stmtContext ctx) {
        String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
        ASTNode readNode = new ASTNode();
        readNode.level = stLevel;
        readNode.parent = rootASTNode;
        readNode.data = ctx.getChild(2).getText();
        readNode.op = "read";
        rootASTNode.childList.add(readNode);
        rootASTNode = readNode;

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitRead_stmt(LittleParser.Read_stmtContext ctx) {
        rootASTNode = rootASTNode.parent;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterWrite_stmt(LittleParser.Write_stmtContext ctx) {
        String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
        ASTNode writeNode = new ASTNode();
        writeNode.level = stLevel;
        writeNode.parent = rootASTNode;
        writeNode.op = "write";
        writeNode.data = ctx.getChild(2).getText();
        rootASTNode.childList.add(writeNode);
        rootASTNode = writeNode;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitWrite_stmt(LittleParser.Write_stmtContext ctx) {
        rootASTNode = rootASTNode.parent;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterReturn_stmt(LittleParser.Return_stmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitReturn_stmt(LittleParser.Return_stmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterExpr(LittleParser.ExprContext ctx) {
        String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
        ASTNode expr = new ASTNode();
        expr.level = stLevel;
        expr.parent = rootASTNode;
        expr.op = "expr";
        rootASTNode.childList.add(expr);
        rootASTNode = expr;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitExpr(LittleParser.ExprContext ctx) {
        // reset last node
        ASTNode lastNode = rootASTNode.parent;
        while(rootASTNode.childList.size()==2 ){
            rootASTNode.op = rootASTNode.childList.get(0).op;
            rootASTNode.data = rootASTNode.childList.get(0).data;
            rootASTNode=rootASTNode.childList.get(0);
        }
        if(rootASTNode.childList.size()==1){
            rootASTNode.parent.childList.add( rootASTNode.childList.get(0));
            rootASTNode.childList.get(0).parent = rootASTNode.parent;
            rootASTNode.parent.childList.remove(rootASTNode);
            // last case
            rootASTNode.parent = null;
        }
        rootASTNode = lastNode;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterExpr_prefix(LittleParser.Expr_prefixContext ctx) {
        if(ctx.getChildCount() > 2){
            // handle add / subtract operators
            if(ctx.getChild(2).getChild(0).getText().equals("+")){
                ASTNode addOpExpr = new ASTNode();
                String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
                addOpExpr.level = stLevel;
                addOpExpr.parent = rootASTNode;
                addOpExpr.op = "+";
                rootASTNode.childList.add(addOpExpr);
                rootASTNode = addOpExpr;

            }else if(ctx.getChild(2).getChild(0).getText().equals("-")){
                ASTNode subOpExpr = new ASTNode();
                String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
                subOpExpr.level = stLevel;
                subOpExpr.parent = rootASTNode;
                subOpExpr.op = "-";
                rootASTNode.childList.add(subOpExpr);
                rootASTNode = subOpExpr;
            }
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitExpr_prefix(LittleParser.Expr_prefixContext ctx) {
        if(ctx.getChildCount() > 2){
            rootASTNode = rootASTNode.parent;
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterFactor(LittleParser.FactorContext ctx) {
        ASTNode enterFactorNode = new ASTNode();
        String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
        enterFactorNode.level = stLevel;
        enterFactorNode.parent = rootASTNode;
        enterFactorNode.op = "fact";
        rootASTNode.childList.add(enterFactorNode);
        rootASTNode = enterFactorNode;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFactor(LittleParser.FactorContext ctx) {
        // reset last node
        ASTNode lastNode = rootASTNode.parent;
        while(rootASTNode.childList.size()==2 ){
            rootASTNode.op = rootASTNode.childList.get(0).op;
            rootASTNode.data = rootASTNode.childList.get(0).data;
            rootASTNode=rootASTNode.childList.get(0); 
        }
        if(rootASTNode.childList.size()==1){
            rootASTNode.parent.childList.add( rootASTNode.childList.get(0));
            rootASTNode.childList.get(0).parent = rootASTNode.parent;
            rootASTNode.parent.childList.remove(rootASTNode);
            // last case
            rootASTNode.parent = null;
        }
        rootASTNode = lastNode;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterFactor_prefix(LittleParser.Factor_prefixContext ctx) {
        if(ctx.getChildCount() > 2){
            if(ctx.getChild(2).getChild(0).getText().equals("*")){
                ASTNode operandMult = new ASTNode();
                String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
                operandMult.level = stLevel;
                operandMult.parent = rootASTNode;
                operandMult.op = "*";
                rootASTNode.childList.add(operandMult);
                rootASTNode = operandMult;
            }
            else if(ctx.getChild(2).getChild(0).getText().equals("/")){
                ASTNode divisor = new ASTNode();
                String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
                divisor.level = stLevel;
                divisor.parent = rootASTNode;
                divisor.op = "/";
                rootASTNode.childList.add(divisor);
                rootASTNode = divisor;

            }

        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFactor_prefix(LittleParser.Factor_prefixContext ctx) {
        if(ctx.getChildCount() > 2){
            // full parent node
            rootASTNode = rootASTNode.parent;
        }

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterPostfix_expr(LittleParser.Postfix_exprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitPostfix_expr(LittleParser.Postfix_exprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterCall_expr(LittleParser.Call_exprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitCall_expr(LittleParser.Call_exprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterExpr_list(LittleParser.Expr_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitExpr_list(LittleParser.Expr_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterExpr_list_tail(LittleParser.Expr_list_tailContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitExpr_list_tail(LittleParser.Expr_list_tailContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterPrimary(LittleParser.PrimaryContext ctx){
        if(ctx.getChildCount()==1){
            String primaryValue = ctx.getText();
            String type = "id";
            try {
                Float.parseFloat(primaryValue);
                type = "float";
            }
            catch(Exception e) {}
            try {
                Integer.parseInt(primaryValue);
                type = "integer";
            }
            catch(Exception e) {}


            // check what dataType is returned after parse attempts
            if(type.equals("id")){
                String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
                ASTNode idNode= new ASTNode();
                idNode.level = stLevel;
                idNode.parent = rootASTNode;
                rootASTNode.childList.add(idNode);
                idNode.op = type;
                idNode.data = primaryValue;
                rootASTNode = idNode;
            }
            else if (type.equals("float")){
                String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
                ASTNode floatNode = new ASTNode();
                floatNode.level = stLevel;
                floatNode.parent = rootASTNode;
                rootASTNode.childList.add(floatNode);
                floatNode.op = type;
                floatNode.data = primaryValue;
                rootASTNode = floatNode;
            }
            else if (type.equals("integer")){
                String stLevel = parseTreeSymbols.get(0).get(0).split(" ")[2];
                ASTNode intNode = new ASTNode();
                intNode.level = stLevel;
                intNode.parent = rootASTNode;
                rootASTNode.childList.add(intNode);
                intNode.op = type;
                intNode.data = primaryValue;
                rootASTNode = intNode;
            }
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitPrimary(LittleParser.PrimaryContext ctx) {
        if(ctx.getChildCount() == 1){
            rootASTNode = rootASTNode.parent;
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterAddop(LittleParser.AddopContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitAddop(LittleParser.AddopContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterMulop(LittleParser.MulopContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitMulop(LittleParser.MulopContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
        parseTreeSymbols = new ArrayList<>();
        parseTreeSymbols.add(new ArrayList<>());
        parseTreeSymbols.get(0).add("Symbol table blockLevel " + Integer.toString(blockLevel));
        parseStack.push(parseTreeSymbols);
        blockLevel++;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
        st.add(parseStack.pop());
        while(!parseStack.empty()){
            parseTreeSymbols = parseStack.peek();
            for(int i = 1; i < parseTreeSymbols.size(); i++){
                st.get(tableReference).add(parseTreeSymbols.get(i));
            }

            tempStack.push(parseStack.pop());
        }
        tableReference++;
        while(!tempStack.empty()){
            parseStack.push(tempStack.pop());
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterElse_part(LittleParser.Else_partContext ctx) {
        ctx.decl().getText();
        parseTreeSymbols = new ArrayList<>();
        parseTreeSymbols.add(new ArrayList<>());
        parseTreeSymbols.get(0).add("Symbol table blockLevel " + Integer.toString(blockLevel));
        parseStack.push(parseTreeSymbols);
        blockLevel++;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitElse_part(LittleParser.Else_partContext ctx) {
        ctx.decl().getText();
        st.add(parseStack.pop());
        while(!parseStack.empty()){
            parseTreeSymbols = parseStack.peek();
            for(int i = 1; i < parseTreeSymbols.size(); i++){
                st.get(tableReference).add(parseTreeSymbols.get(i));
            }

            tempStack.push(parseStack.pop());
        }
        tableReference++;
        while(!tempStack.empty()){
            parseStack.push(tempStack.pop());
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterCond(LittleParser.CondContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitCond(LittleParser.CondContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterComop(LittleParser.ComopContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitComop(LittleParser.ComopContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
        parseTreeSymbols = new ArrayList<>();
        parseTreeSymbols.add(new ArrayList<>());
        parseTreeSymbols.get(0).add("Symbol table blockLevel " + Integer.toString(blockLevel));
        parseStack.push(parseTreeSymbols);
        blockLevel++;

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
        st.add(parseStack.pop());
        while(!parseStack.empty()){
            parseTreeSymbols = parseStack.peek();
            for(int i = 1; i < parseTreeSymbols.size(); i++){
                st.get(tableReference).add(parseTreeSymbols.get(i));
            }
            tempStack.push(parseStack.pop());
        }
        tableReference++;
        while(!tempStack.empty()){
            parseStack.push(tempStack.pop());
        }
    }

}
