package app;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CommonTokenStream;
import antlr.ExprLexer;
import antlr.ExprParser;
import expression.*;
import java.io.IOException;

public class ExpressionApp {
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: file name");
        }
        else {
            String fileName = args[0];
            ExprParser parser = getParser(fileName);

            // build a parser tree
            // parse from the start symbol 'prog'
            ParseTree antlrAST = parser.prog();

            if(MyErrorListener.hasErrors){
                /*let the syntax error be reported*/
            } else {
                // Create a visitor for converting the parse tree into expression
                AntlrToProgram progVisitor = new AntlrToProgram();
                Program prog = progVisitor.visit(antlrAST);

                if(progVisitor.semanticErrors.isEmpty()) {
                    ExpressionProcessor ep = new ExpressionProcessor(prog.expressions);
                    for(String evaluation: ep.getEvaluationResults()) {
                        System.out.println(evaluation);
                    }
                }
                else {
                    for(String err: progVisitor.semanticErrors) {
                        System.out.println(err);
                    }
                }
            }
        }
    }

    private static ExprParser getParser(String fileName) {
        ExprParser parser = null;

        try {
            CharStream input = CharStreams.fromFileName(fileName);
            ExprLexer lexer = new ExprLexer(input);
            CommonTokenStream token = new CommonTokenStream(lexer);
            parser = new ExprParser(token);

            // syntax error handling
            parser.removeErrorListeners();
            parser.addErrorListener(new MyErrorListener());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parser;
    }
}