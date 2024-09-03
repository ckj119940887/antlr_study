package expression;

import java.util.List;
import java.util.ArrayList;
import antlr.ExprBaseVisitor;
import antlr.ExprParser.*;

public class AntlrToProgram extends ExprBaseVisitor<Program> {

    public List<String> semanticErrors;

    public Program visitProgram(ProgramContext ctx) {
        Program prog = new Program();

        semanticErrors = new ArrayList<>();
        AntlrToExpression exprVisitor = new AntlrToExpression(semanticErrors);

        for(int i = 0; i < ctx.getChildCount(); i++) {
            if(i == ctx.getChildCount() - 1) {
                // EOF
                // Do not convert it to an Expression object
            }
            else {
                prog.addExpression(exprVisitor.visit(ctx.getChild(i)));
            }
        }

        return prog;
    }
}