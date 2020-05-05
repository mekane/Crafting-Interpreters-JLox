package test;

import com.craftinginterpreters.lox.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    public void parsesTerminals() {
        Expr.Literal e = (Expr.Literal) parseSingleExpression("27");
        assertEquals(27.0, e.value);

        e = (Expr.Literal) parseSingleExpression("\"27\"");
        assertEquals("27", e.value);

        e = (Expr.Literal) parseSingleExpression("false");
        assertEquals(false, e.value);

        e = (Expr.Literal) parseSingleExpression("true");
        assertEquals(true, e.value);

        e = (Expr.Literal) parseSingleExpression("nil");
        assertEquals(null, e.value);
    }

    @Test
    public void parsesExpressionsInParentheses() {
        Expr.Grouping g = (Expr.Grouping) parseSingleExpression("(19)");
        Expr.Literal e = (Expr.Literal) g.expression;
        assertEquals(19.0, e.value);
    }

    @Test
    public void parsesUnaryOperators() {
        Expr.Unary e = (Expr.Unary) parseSingleExpression("-1");
        Token token = e.operator;
        Expr.Literal right = (Expr.Literal) e.right;

        assertEquals(TokenType.MINUS, token.type);
        assertEquals(1.0, right.value);


        e = (Expr.Unary) parseSingleExpression("!0");
        token = e.operator;
        right = (Expr.Literal) e.right;

        assertEquals(TokenType.BANG, token.type);
        assertEquals(0.0, right.value);
    }

    @Test
    public void parsesEqualityOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("11 == 28");
        Token token = e.operator;
        Expr.Literal left = (Expr.Literal) e.left;
        Expr.Literal right = (Expr.Literal) e.right;

        assertEquals(TokenType.EQUAL_EQUAL, token.type);
        assertEquals(11.0, left.value);
        assertEquals(28.0, right.value);
    }

    @Test
    public void parsesNotEqualOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("false != true");
        Token token = e.operator;
        Expr.Literal left = (Expr.Literal) e.left;
        Expr.Literal right = (Expr.Literal) e.right;

        assertEquals(TokenType.BANG_EQUAL, token.type);
        assertEquals(false, left.value);
        assertEquals(true, right.value);
    }


    private Expr parseSingleExpression(String expression) {
        Parser p = new Parser(new Scanner(expression).scanTokens());
        return p.parse();
    }
}