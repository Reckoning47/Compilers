// CSCI 468
// Chris Cooper, Spencer Lawry, Matthew Gober
// Driver class for Compiler

import java.io.*;
import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Driver {

    public static void main(String[] args) throws Exception {
        // Number of tests you want to use, MUST FOLLOW NAMING SCHEME "test#.micro" and will output to "test#.out
        // must start at 1.
//        int testCases = 20;
//        for(int i =1; i <= testCases; i++) {


        String fileName = "Step4/inputs/test_mult.micro";      // set later to args[0] and setup in bash script
        String outputName = "Step4/outputs/test_multTester.out";     // set output args[0] + ".out";
        FileWriter myWriter = new FileWriter(outputName);

        try {

            // create the grammar from CharStream interface
            LittleLexer grammar = new LittleLexer(CharStreams.fromFileName(fileName));
            // create the token stream and set channel to 0 to pull tokens
            CommonTokenStream tokens = new CommonTokenStream(grammar, 0);
            // create list of tokens and utilize the Vocabulary interface to return string associated with token
            // Save for previous steps testing
            //  List<Token> tokenList = tokens.getTokens();

            Vocabulary vocab = grammar.getVocabulary();
            LittleParser myParser = new LittleParser(tokens);
            ParseTree tree = myParser.start();
            CustomNewListener listener = new CustomNewListener();
            ParseTreeWalker pTree = new ParseTreeWalker();
            pTree.walk(listener, tree);

            TinyInterpreter tinyCompiled = new TinyInterpreter(listener.st);
            tinyCompiled.traverseTable(listener.rootASTNode);
            System.out.println(tinyCompiled.printTinyOutput());
            myWriter.write(tinyCompiled.printTinyOutput());

            // iterate through linkedhashmap, putting entries into Arraylist to be written out
            // https://stackoverflow.com/questions/12310914/how-to-iterate-through-linkedhashmap-with-lists-as-values
            // Reference: "In Java 8:" will return the keys and values from symbol_table



            // part 3
            /*
            symbol_table.forEach((key, value) -> {
                if(key != "GLOBAL") {
                    output_to_writer.add("");
                }
                if(key != "DECLARATION ERROR") {
                    output_to_writer.add("Symbol table " + key);
                }
                else{
                    output_to_writer_error.add(key + " n");
                }
                value.forEach((id_name, string_description) -> {
                    output_to_writer.add("name " + id_name + " type " + string_description);
                });
            });

            if(output_to_writer_error.isEmpty()) {
                for (int i = 0; i < output_to_writer.size(); i++) {
                    myWriter.write(output_to_writer.get(i).toString() + "\n");
                }
            }
            else{
                // output error text
                myWriter.write(output_to_writer_error.get(0).toString());
            }


            */

            // part 2
            /*
            errors = myParser.getNumberOfSyntaxErrors();
            if (errors > 0) {
                System.out.println("Not accepted");
                myWriter.write("Not accepted");
            } else {
                System.out.println("Accepted");
                myWriter.write("Accepted");
            }
            */

            /*
            // part 1
            for(int i=0; i < tokenList.size(); i++){
                int type = tokenList.get(i).getType();

                // EOF is 0, so exclude it from output
                //removed appends
                if (type > 0) {
                    String symbol = vocab.getSymbolicName(type);
                    System.out.println("Token Type: " + symbol);
                    System.out.println("Value: " + tokenList.get(i).getText());

                    myWriter.write("Token Type: " + symbol +"\n" + "Value: " + tokenList.get(i).getText() +"\n");

                }
            }
            */
            myWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}