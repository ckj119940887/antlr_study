package expression;

import org.antlr.v4.runtime.*;

import java.util.Collections;
import java.util.List;

public class MyErrorListener extends BaseErrorListener {
    public static boolean hasErrors = false;

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {

        hasErrors = true;

        List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        System.err.println("Syntax Error!");
        System.err.println( "Token " + "\"" + ((Token) offendingSymbol).getText() + "\""
                            +
                            " (Line " + line + ":" + charPositionInLine + ") "
                            +
                            ": " + msg);
        System.err.println("Rule Stack: " + stack);
    }
}
