package test;

import com.craftinginterpreters.lox.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    public void parsesTerminals() {
        Expr.Literal e = (Expr.Literal) parseSingleExpression("27");
        assertEquals(e.value, 27.0);

        e = (Expr.Literal) parseSingleExpression("\"27\"");
        assertEquals(e.value, "27");

        e = (Expr.Literal) parseSingleExpression("false");
        assertEquals(e.value, false);

        e = (Expr.Literal) parseSingleExpression("true");
        assertEquals(e.value, true);

        e = (Expr.Literal) parseSingleExpression("nil");
        assertEquals(e.value, null);
    }

    @Test
    public void parsesExpressionsInParentheses() {
        Expr.Grouping g = (Expr.Grouping)parseSingleExpression("(19)");
        Expr.Literal e = (Expr.Literal)g.expression;
        assertEquals(e.value, 19.0);
    }

    @Test
    public void parsesUnaryOperators() {
        Expr.Unary e = (Expr.Unary)parseSingleExpression("-1");
        Token token = e.operator;
        Expr.Literal right = (Expr.Literal)e.right;

        assertEquals(TokenType.MINUS, token.type);
        assertEquals(1.0, right.value);


        e = (Expr.Unary)parseSingleExpression("!0");
        token = e.operator;
        right = (Expr.Literal)e.right;

        assertEquals(TokenType.BANG, token.type);
        assertEquals(0.0, right.value);
    }

    private Expr parseSingleExpression(String expression) {
        Parser p = new Parser(new Scanner(expression).scanTokens());
        return p.parse();
    }
}