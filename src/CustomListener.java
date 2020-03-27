// CSCI 468
// Chris Cooper, Spencer Lawry, Matthew Gober
// Realized Listener class that extends from LittleBaseListener produced by ANTLR

import java.util.Stack;
import java.util.LinkedHashMap;

public class CustomListener extends LittleBaseListener {
    int block_index = 0;
    // might be difficult to output later but good for now
    String errors = "";
    // Use LinkedHashMap to keep order by insertion
    Stack<LinkedHashMap<String,String>> parse_stack = new Stack<LinkedHashMap<String,String>>();
    LinkedHashMap<String, LinkedHashMap<String,String>> output_symbol_table = new LinkedHashMap<String, LinkedHashMap<String,String>>();

    /////////////////////////////////////////////
    // Return final output table of parse stack
    public LinkedHashMap<String, LinkedHashMap<String, String>> output_table(){
        if(!errors.isEmpty()){
            LinkedHashMap<String,String> error = new LinkedHashMap<String,String>();
            LinkedHashMap<String, LinkedHashMap<String,String>> error_output = new LinkedHashMap<String, LinkedHashMap<String,String>>();
            error.put("error", errors);
            error_output.put("DECLARATION ERROR", error);
            return error_output;
        } else {
            return output_symbol_table;
        }
    }

    /////////////////////////////////////////////

    @Override
    public void enterProgram(LittleParser.ProgramContext ctx){
        parse_stack.push(new LinkedHashMap<>());
        output_symbol_table.put("GLOBAL", parse_stack.peek());
    }

    @Override
    public void exitProgram(LittleParser.ProgramContext ctx){
        parse_stack.pop();
    }

    @Override
    public void exitString_decl(LittleParser.String_declContext ctx){
        LinkedHashMap<String,String> scope = parse_stack.peek();
        if(!(scope.containsKey(ctx.id().getStart().getText()))){
            scope.put(ctx.id().getStart().getText(), "STRING value " + ctx.str().getStart().getText());
        } else {
            if(errors.equals("")){
                errors = ctx.id().getStart().getText();
            }
        }
    }

    @Override
    public void exitVar_decl(LittleParser.Var_declContext ctx){
        LinkedHashMap<String, String> currentScope = parse_stack.peek();
        LittleParser.Id_tailContext ctxList = ctx.id_list().id_tail();

        if (!(currentScope.containsKey(ctx.id_list().getStart().getText()))){
            currentScope.put(ctx.id_list().getStart().getText(), ctx.var_type().getStart().getText());
            while(ctxList.getStart().getText().contains(",")){
                if(!(currentScope.containsKey(ctxList.id().getStart().getText()))){
                    currentScope.put(ctxList.id().getStart().getText(), ctx.var_type().getStart().getText());
                    ctxList = ctxList.id_tail();
                } else{
                    if(errors.isEmpty()){
                        errors = ctxList.id().getStart().getText();
                    }
                }
            }
        }
        else {
            if(errors.isEmpty()){
                errors = ctx.id_list().getStart().getText();
            }
        }
    }

    @Override
    public void exitParam_decl(LittleParser.Param_declContext ctx){
        LinkedHashMap<String,String> scope = parse_stack.peek();
        if(!(scope.containsKey(ctx.id().getStart().getText()))){
            scope.put(ctx.id().getStart().getText(), ctx.var_type().getStart().getText());
        }
        else {
            if(errors.equals("")){
                errors = ctx.id().getStart().getText();
            }
        }
    }

    @Override
    public void enterFunc_decl(LittleParser.Func_declContext ctx){
        parse_stack.push(new LinkedHashMap<>());
        output_symbol_table.put(ctx.id().getStart().getText(), parse_stack.peek());
    }

    @Override
    public void exitFunc_decl(LittleParser.Func_declContext ctx){
        parse_stack.pop();
    }

    @Override
    public void enterIf_stmt(LittleParser.If_stmtContext ctx){
        // update tree level to the current block of the code
        block_index++;
        parse_stack.push(new LinkedHashMap<>());
        output_symbol_table.put("BLOCK " + block_index, parse_stack.peek());
    }

    @Override
    public void exitIf_stmt(LittleParser.If_stmtContext ctx){
        if(!(ctx.else_part().getChildCount() > 0)){
            parse_stack.pop();
        }
    }

    @Override
    public void enterElse_part(LittleParser.Else_partContext ctx){
        if(ctx.getChildCount() > 0){
            block_index++;
            parse_stack.pop();
            parse_stack.push(new LinkedHashMap<>());
            output_symbol_table.put("BLOCK " + block_index, parse_stack.peek());
        }
    }

    @Override
    public void exitElse_part(LittleParser.Else_partContext ctx){
        if(ctx.getChildCount() > 0){
            parse_stack.pop();
        }
    }

    @Override
    public void enterWhile_stmt(LittleParser.While_stmtContext ctx){
        block_index++;
        parse_stack.push(new LinkedHashMap<>());
        output_symbol_table.put("BLOCK " + block_index, parse_stack.peek());
    }

    @Override
    public void exitWhile_stmt(LittleParser.While_stmtContext ctx){
        parse_stack.pop();
    }
}