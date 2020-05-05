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

    @Test
    public void parsesComparisonGreaterThanOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("2 > 3");
        Token token = e.operator;
        Expr.Literal left = (Expr.Literal) e.left;
        Expr.Literal right = (Expr.Literal) e.right;

        assertEquals(TokenType.GREATER, token.type);
        assertEquals(2.0, left.value);
        assertEquals(3.0, right.value);
    }

    @Test
    public void parsesComparisonGreaterThanEqualToOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("3 >= 4");
        Token token = e.operator;
        Expr.Literal left = (Expr.Literal) e.left;
        Expr.Literal right = (Expr.Literal) e.right;

        assertEquals(TokenType.GREATER_EQUAL, token.type);
        assertEquals(3.0, left.value);
        assertEquals(4.0, right.value);
    }

    @Test
    public void parsesComparisonLessThanOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("2 < 3");
        Token token = e.operator;
        Expr.Literal left = (Expr.Literal) e.left;
        Expr.Literal right = (Expr.Literal) e.right;

        assertEquals(TokenType.LESS, token.type);
        assertEquals(2.0, left.value);
        assertEquals(3.0, right.value);
    }

    @Test
    public void parsesComparisonLessThanEqualToOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("3 <= 4");
        Token token = e.operator;
        Expr.Literal left = (Expr.Literal) e.left;
        Expr.Literal right = (Expr.Literal) e.right;

        assertEquals(TokenType.LESS_EQUAL, token.type);
        assertEquals(3.0, left.value);
        assertEquals(4.0, right.value);
    }

    @Test
    public void parsesAdditionOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("2 + 2");
        assertBinaryExpression(e, 2.0, TokenType.PLUS, 2.0);
    }

    @Test
    public void parsesSubtractionOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("2 - 2");
        assertBinaryExpression(e, 2.0, TokenType.MINUS, 2.0);
    }



    private Expr parseSingleExpression(String expression) {
        Parser p = new Parser(new Scanner(expression).scanTokens());
        return p.parse();
    }

    private void assertBinaryExpression(Expr.Binary e, double left, TokenType tokenType, double right) {
        Token tokenActual = e.operator;
        Expr.Literal leftActual = (Expr.Literal) e.left;
        Expr.Literal rightActual = (Expr.Literal) e.right;

        assertEquals(tokenType, tokenActual.type);
        assertEquals(left, leftActual.value);
        assertEquals(right, rightActual.value);
    }
}