package test;

import com.craftinginterpreters.lox.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    public void parsesTernaryOperator() {
        Expr.Ternary e = (Expr.Ternary) parseSingleExpression("true ? 7 : 9");

        Expr.Literal cond = (Expr.Literal) e.condition;
        Expr.Literal left = (Expr.Literal) e.left;
        Expr.Literal right = (Expr.Literal) e.right;

        assertEquals(true, cond.value);
        assertEquals(7.0, left.value);
        assertEquals(9.0, right.value);
    }

    @Test
    public void parsesNotEqualOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("false != true");
        assertBinaryExpression(e, false, TokenType.BANG_EQUAL, true);
    }

    @Test
    public void parsesEqualityOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("11 == 28");
        assertBinaryExpression(e, 11.0, TokenType.EQUAL_EQUAL, 28.0);
    }

    @Test
    public void parsesComparisonGreaterThanOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("2 > 3");
        assertBinaryExpression(e, 2.0, TokenType.GREATER, 3.0);
    }

    @Test
    public void parsesComparisonGreaterThanEqualToOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("3 >= 4");
        assertBinaryExpression(e, 3.0, TokenType.GREATER_EQUAL, 4.0);
    }

    @Test
    public void parsesComparisonLessThanOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("2 < 3");
        assertBinaryExpression(e, 2.0, TokenType.LESS, 3.0);
    }

    @Test
    public void parsesComparisonLessThanEqualToOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("3 <= 4");
        assertBinaryExpression(e, 3.0, TokenType.LESS_EQUAL, 4.0);
    }

    @Test
    public void parsesSubtractionOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("2 - 2");
        assertBinaryExpression(e, 2.0, TokenType.MINUS, 2.0);
    }

    @Test
    public void parsesAdditionOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("2 + 2");
        assertBinaryExpression(e, 2.0, TokenType.PLUS, 2.0);
    }

    @Test
    public void parsesDivisionOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("100 / 20");
        assertBinaryExpression(e, 100.0, TokenType.SLASH, 20.0);
    }

    @Test
    public void parsesMultiplicationOperator() {
        Expr.Binary e = (Expr.Binary) parseSingleExpression("6 * 7");
        assertBinaryExpression(e, 6.0, TokenType.STAR, 7.0);
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

    private Expr parseSingleExpression(String expression) {
        Parser p = new Parser(new Scanner(expression).scanTokens());
        return p.parse();
    }

    private void assertBinaryExpression(Expr.Binary e, boolean left, TokenType tokenType, boolean right) {
        assertEquals(tokenType, e.operator.type);
        assertEquals(left, ((Expr.Literal) e.left).value);
        assertEquals(right, ((Expr.Literal) e.right).value);
    }

    private void assertBinaryExpression(Expr.Binary e, double left, TokenType tokenType, double right) {
        assertEquals(tokenType, e.operator.type);
        assertEquals(left, ((Expr.Literal) e.left).value);
        assertEquals(right, ((Expr.Literal) e.right).value);
    }
}