package expression;

import java.util.List;
import java.util.ArrayList;
import antlr.ExprBaseVisitor;
import antlr.ExprParser.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.*;

public class AntlrToExpression extends ExprBaseVisitor<Expression> {

    // symbol table
    private List<String> vars;
    private List<String> semanticErrors; // 1. duplicate declaration 2. reference to undeclaration

    public AntlrToExpression(List<String> semanticErrors) {
        vars = new ArrayList<>();
        this.semanticErrors = semanticErrors;
    }

    public Expression visitDeclaration(DeclarationContext ctx) {
        // i : INT = 5
        // 上述表达式在child中的编号是从左到右依次编号的
        // 0: i, 1: ':', 2: INT, 3: =, 4: 5

        // ID() equivalent to ctx.getChild(0).getSymbol(), 这是因为在定义语法的文件中使用的名字就是ID
        // ID() is a method generated to correspond to the token ID in the source grammar
        Token idToken = ctx.ID().getSymbol();
        int line = idToken.getLine();
        int column = idToken.getCharPositionInLine() + 1;

        String id = ctx.getChild(0).getText();
        // Maintaining the symbol table
        if(vars.contains(id)) {
            semanticErrors.add("Error: variable " + id + " already declared (" + line + ", " + column + ")");
        }
        else {
            vars.add(id);
        }

        String type = ctx.getChild(2).getText();
        int value = Integer.parseInt(ctx.NUM().getText());

        return new VariableDeclaration(id, type, value);
    }

    public Expression visitMultiplication(MultiplicationContext ctx) {
        Expression left = visit(ctx.getChild(0)); // recursively visit the left subtree of the current multiplication node
        Expression right = visit(ctx.getChild(2));
        return new Multiplication(left, right);
    }

    public Expression visitAddition(AdditionContext ctx) {
        Expression left = visit(ctx.getChild(0)); // recursively visit the left subtree of the current multiplication node
        Expression right = visit(ctx.getChild(2));
        return new Addition(left, right);
    }

    public Expression visitVariable(VariableContext ctx) {
        Token idToken = ctx.ID().getSymbol();
        int line = idToken.getLine();
        int column = idToken.getCharPositionInLine() + 1;

        String id = ctx.ID().getText();
        if(!vars.contains(id)) {
            semanticErrors.add("Error: variable " + id + " not declared (" + line + ", " + column + ")");
        }

        return new Variable(id);
    }

    public Expression visitNumber(NumberContext ctx) {
        String numText = ctx.getChild(0).getText();
        int num = Integer.parseInt(numText);
        return new Number(num);
    }
}