package test;

import com.craftinginterpreters.lox.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class InterpreterTest {
    static Interpreter interpreter = new Interpreter();

    Token bang = new Token(TokenType.BANG, "!", null, 0);
    Token plus = new Token(TokenType.PLUS, "+", null, 0);
    Token minus = new Token(TokenType.MINUS, "-", null, 0);
    Token star = new Token(TokenType.STAR, "*", null, 0);
    Token slash = new Token(TokenType.SLASH, "/", null, 0);

    Token greater = new Token(TokenType.GREATER, ">", null, 0);
    Token greaterEqual = new Token(TokenType.GREATER_EQUAL, ">=", null, 0);
    Token less = new Token(TokenType.LESS, "<", null, 0);
    Token lessEqual = new Token(TokenType.LESS_EQUAL, "<=", null, 0);

    Token doubleEqual = new Token(TokenType.EQUAL_EQUAL, "==", null, 0);
    Token bangEqual = new Token(TokenType.BANG_EQUAL, "!=", null, 0);

    @Test
    public void evaluatesLiteralNull() {
        assertEquals(null, evaluateLiteral(null));
    }

    @Test
    public void evaluatesLiteralTrue() {
        assertEquals(true, evaluateLiteral(true));
    }

    @Test
    public void evaluatesLiteralFalse() {
        assertEquals(false, evaluateLiteral(false));
    }

    @Test
    public void evaluatesLiteralNumbers() {
        assertEquals(10.0, evaluateLiteral(10.0));
        assertEquals(-1000.0, evaluateLiteral(-1000.0));
    }

    @Test
    public void evaluatesLiteralStrings() {
        assertEquals(evaluateLiteral("Foo"), "Foo");
    }

    @Test
    public void evaluatesUnaryBang() {
        Expr.Unary notFalse = new Expr.Unary(bang, new Expr.Literal(false));
        assertEquals(true, interpreter.evaluate(notFalse));

        Expr.Unary notTrue = new Expr.Unary(bang, new Expr.Literal(true));
        assertEquals(false, interpreter.evaluate(notTrue));
    }

    @Test
    public void evaluatesUnaryMinus() {
        Expr.Unary negatePositive = new Expr.Unary(minus, new Expr.Literal(50.0));
        assertEquals(-50.0, interpreter.evaluate(negatePositive));

        Expr.Unary negateNegative = new Expr.Unary(minus, new Expr.Literal(-50.0));
        assertEquals(50.0, interpreter.evaluate(negateNegative));
    }

    @Test
    public void runtimeErrorForUnaryMinusWithNonNumber() {
        assertRuntimeError(new Expr.Unary(minus, new Expr.Literal(null)), "Operand must be a number");
        assertRuntimeError(new Expr.Unary(minus, new Expr.Literal(true)), "Operand must be a number");
        assertRuntimeError(new Expr.Unary(minus, new Expr.Literal("muffin")), "Operand must be a number");
    }

    @Test
    public void evaluatesBinaryMinus() {
        assertEquals(8.0, interpreter.evaluate(expr(10, minus, 2)));
    }

    @Test
    public void runtimeErrorsForBinaryMinus() {
        assertRuntimeError(expr(null, minus, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, minus, null), "Operands must be numbers");

        assertRuntimeError(expr(false, minus, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, minus, false), "Operands must be numbers");

        assertRuntimeError(expr("foo", minus, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, minus, "bar"), "Operands must be numbers");

        assertRuntimeError(expr("f", minus, "b"), "Operands must be numbers");
    }

    @Test
    public void evaluatesBinaryStar() {
        assertEquals(20.0, interpreter.evaluate(expr(10, star, 2)));
    }

    @Test
    public void runtimeErrorsForBinaryStar() {
        assertRuntimeError(expr(null, star, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, star, null), "Operands must be numbers");

        assertRuntimeError(expr(false, star, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, star, false), "Operands must be numbers");

        assertRuntimeError(expr("foo", star, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, star, "bar"), "Operands must be numbers");

        assertRuntimeError(expr("f", star, "b"), "Operands must be numbers");
    }

    @Test
    public void evaluatesBinarySlash() {
        assertEquals(5.0, interpreter.evaluate(expr(10, slash, 2)));
    }

    @Test
    public void runtimeErrorForBinarySlash() {
        assertRuntimeError(expr(null, slash, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, slash, null), "Operands must be numbers");

        assertRuntimeError(expr(false, slash, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, slash, false), "Operands must be numbers");

        assertRuntimeError(expr("foo", slash, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, slash, "bar"), "Operands must be numbers");

        assertRuntimeError(expr("f", slash, "b"), "Operands must be numbers");
    }

    @Test
    public void evaluatesBinaryPlusForNumbers() {
        assertEquals(12.0, interpreter.evaluate(expr(10, plus, 2)));
    }

    @Test
    public void evaluatesBinaryPlusForStrings() {
        assertEquals("foobar", interpreter.evaluate(expr("foo", plus, "bar")));
    }

    @Test
    public void runtimeErrorForBinaryPlus() {
        assertRuntimeError(expr(null, plus, 1), "Operands must be two numbers or two strings");
        assertRuntimeError(expr(1, plus, null), "Operands must be two numbers or two strings");
        assertRuntimeError(expr(null, plus, null), "Operands must be two numbers or two strings");

        assertRuntimeError(expr(false, plus, 1), "Operands must be two numbers or two strings");
        assertRuntimeError(expr(1, plus, false), "Operands must be two numbers or two strings");
        assertRuntimeError(expr(true, plus, false), "Operands must be two numbers or two strings");

        assertRuntimeError(expr("foo", plus, 1), "Operands must be two numbers or two strings");
        assertRuntimeError(expr(1, plus, "bar"), "Operands must be two numbers or two strings");
    }

    @Test
    public void evaluatesBinaryEqual() {
        assertEquals(true, interpreter.evaluate(expr(null, doubleEqual, null)));
        assertEquals(true, interpreter.evaluate(expr(true, doubleEqual, true)));
        assertEquals(true, interpreter.evaluate(expr(false, doubleEqual, false)));
        assertEquals(true, interpreter.evaluate(expr(7, doubleEqual, 7)));
        assertEquals(true, interpreter.evaluate(expr("test", doubleEqual, "test")));

        assertEquals(false, interpreter.evaluate(expr(null, doubleEqual, 1)));
        assertEquals(false, interpreter.evaluate(expr(null, doubleEqual, "anything")));
        assertEquals(false, interpreter.evaluate(expr(7, doubleEqual, null)));
        assertEquals(false, interpreter.evaluate(expr("anything", doubleEqual, null)));

        assertEquals(false, interpreter.evaluate(expr(true, doubleEqual, false)));
        assertEquals(false, interpreter.evaluate(expr(false, doubleEqual, true)));
        assertEquals(false, interpreter.evaluate(expr(7, doubleEqual, 14)));
        assertEquals(false, interpreter.evaluate(expr("foo", doubleEqual, "bar")));

        assertEquals(false, interpreter.evaluate(expr(false, doubleEqual, "")));
        assertEquals(false, interpreter.evaluate(expr(false, doubleEqual, "false")));
        assertEquals(false, interpreter.evaluate(expr(true, doubleEqual, 14)));
        assertEquals(false, interpreter.evaluate(expr(false, doubleEqual, 15)));
        assertEquals(false, interpreter.evaluate(expr(1, doubleEqual, "1")));
    }

    @Test
    public void evaluatesBinaryNotEqual() {
        assertEquals(false, interpreter.evaluate(expr(null, bangEqual, null)));
        assertEquals(false, interpreter.evaluate(expr(true, bangEqual, true)));
        assertEquals(false, interpreter.evaluate(expr(false, bangEqual, false)));
        assertEquals(false, interpreter.evaluate(expr(7, bangEqual, 7)));
        assertEquals(false, interpreter.evaluate(expr("test", bangEqual, "test")));

        assertEquals(true, interpreter.evaluate(expr(null, bangEqual, 1)));
        assertEquals(true, interpreter.evaluate(expr(null, bangEqual, "anything")));
        assertEquals(true, interpreter.evaluate(expr(7, bangEqual, null)));
        assertEquals(true, interpreter.evaluate(expr("anything", bangEqual, null)));

        assertEquals(true, interpreter.evaluate(expr(false, bangEqual, true)));
        assertEquals(true, interpreter.evaluate(expr(true, bangEqual, false)));
        assertEquals(true, interpreter.evaluate(expr(7, bangEqual, 14)));
        assertEquals(true, interpreter.evaluate(expr("foo", bangEqual, "bar")));

        assertEquals(true, interpreter.evaluate(expr(false, bangEqual, "")));
        assertEquals(true, interpreter.evaluate(expr(false, bangEqual, "false")));
        assertEquals(true, interpreter.evaluate(expr(true, bangEqual, 14)));
        assertEquals(true, interpreter.evaluate(expr(false, bangEqual, 15)));
        assertEquals(true, interpreter.evaluate(expr(1, bangEqual, "1")));
    }

    @Test
    public void evaluatesBinaryGreaterThan() {
        assertEquals(true, interpreter.evaluate(expr(99, greater, 1)));
        assertEquals(false, interpreter.evaluate(expr(1, greater, 99)));
    }

    @Test
    public void runtimeErrorForBinaryGreaterThan() {
        assertRuntimeError(expr(null, greater, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, greater, null), "Operands must be numbers");
        assertRuntimeError(expr(null, greater, null), "Operands must be numbers");

        assertRuntimeError(expr(false, greater, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, greater, false), "Operands must be numbers");
        assertRuntimeError(expr(true, greater, false), "Operands must be numbers");

        assertRuntimeError(expr("foo", greater, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, greater, "bar"), "Operands must be numbers");
    }

    @Test
    public void evaluatesBinaryGreaterThanOrEqualTo() {
        assertEquals(true, interpreter.evaluate(expr(99, greaterEqual, 1)));
        assertEquals(true, interpreter.evaluate(expr(99, greaterEqual, 99)));
        assertEquals(false, interpreter.evaluate(expr(1, greaterEqual, 99)));
    }

    @Test
    public void runtimeErrorForBinaryGreaterThanOrEqualTo() {
        assertRuntimeError(expr(null, greaterEqual, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, greaterEqual, null), "Operands must be numbers");
        assertRuntimeError(expr(null, greaterEqual, null), "Operands must be numbers");

        assertRuntimeError(expr(false, greaterEqual, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, greaterEqual, false), "Operands must be numbers");
        assertRuntimeError(expr(true, greaterEqual, false), "Operands must be numbers");

        assertRuntimeError(expr("foo", greaterEqual, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, greaterEqual, "bar"), "Operands must be numbers");
    }

    @Test
    public void evaluatesBinaryLessThan() {
        assertEquals(true, interpreter.evaluate(expr(1, less, 99)));
        assertEquals(false, interpreter.evaluate(expr(99, less, 1)));
    }

    @Test
    public void runtimeErrorForBinaryLessThan() {
        assertRuntimeError(expr(null, less, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, less, null), "Operands must be numbers");
        assertRuntimeError(expr(null, less, null), "Operands must be numbers");

        assertRuntimeError(expr(false, less, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, less, false), "Operands must be numbers");
        assertRuntimeError(expr(true, less, false), "Operands must be numbers");

        assertRuntimeError(expr("foo", less, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, less, "bar"), "Operands must be numbers");
    }

    @Test
    public void evaluatesBinaryLessThanOrEqualTo() {
        assertEquals(true, interpreter.evaluate(expr(1, lessEqual, 99)));
        assertEquals(true, interpreter.evaluate(expr(1, lessEqual, 1)));
        assertEquals(false, interpreter.evaluate(expr(99, lessEqual, 1)));
    }

    @Test
    public void runtimeErrorForBinaryLessThanOrEqualTo() {
        assertRuntimeError(expr(null, lessEqual, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, lessEqual, null), "Operands must be numbers");
        assertRuntimeError(expr(null, lessEqual, null), "Operands must be numbers");

        assertRuntimeError(expr(false, lessEqual, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, lessEqual, false), "Operands must be numbers");
        assertRuntimeError(expr(true, lessEqual, false), "Operands must be numbers");

        assertRuntimeError(expr("foo", lessEqual, 1), "Operands must be numbers");
        assertRuntimeError(expr(1, lessEqual, "bar"), "Operands must be numbers");
    }

    /* ---------- HELPER METHODS ---------- */

    private Object evaluateLiteral(Object expression) {
        Expr e = new Expr.Literal(expression);
        return interpreter.evaluate(e);
    }

    private Expr.Binary expr(int left, Token token, int r) {
        return new Expr.Binary(new Expr.Literal(Double.valueOf(left)), token, new Expr.Literal(Double.valueOf(r)));
    }

    private Expr.Binary expr(String left, Token token, int r) {
        return new Expr.Binary(new Expr.Literal(left), token, new Expr.Literal(Double.valueOf(r)));
    }

    private Expr.Binary expr(int left, Token token, String r) {
        return new Expr.Binary(new Expr.Literal(Double.valueOf(left)), token, new Expr.Literal(r));
    }

    private Expr.Binary expr(String left, Token token, String r) {
        return new Expr.Binary(new Expr.Literal(left), token, new Expr.Literal(r));
    }

    private Expr.Binary expr(boolean left, Token token, boolean r) {
        return new Expr.Binary(new Expr.Literal(left), token, new Expr.Literal(r));
    }

    private Expr.Binary expr(int left, Token token, boolean r) {
        return new Expr.Binary(new Expr.Literal(left), token, new Expr.Literal(r));
    }

    private Expr.Binary expr(boolean left, Token token, int r) {
        return new Expr.Binary(new Expr.Literal(left), token, new Expr.Literal(r));
    }

    private Expr.Binary expr(String left, Token token, boolean r) {
        return new Expr.Binary(new Expr.Literal(left), token, new Expr.Literal(r));
    }

    private Expr.Binary expr(boolean left, Token token, String r) {
        return new Expr.Binary(new Expr.Literal(left), token, new Expr.Literal(r));
    }

    private void assertRuntimeError(Expr expr, String message) {
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr));
        assertTrue(error.getMessage().contains(message), "Got " + error.getMessage());
    }
}
