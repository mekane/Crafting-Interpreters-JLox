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
        Expr.Unary negateNonNumber = new Expr.Unary(minus, new Expr.Literal("muffin"));
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(negateNonNumber));

        assertTrue(error.getMessage().contains("Operand must be a number"));
    }

    @Test
    public void evaluatesBinaryMinus() {
        assertEquals(8.0, interpreter.evaluate(expr(10, minus, 2)));
    }

    @Test
    public void runtimeErrorsForBinaryMinus() {
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", minus, 1)));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr(1, minus, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", minus, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void evaluatesBinaryStar() {
        assertEquals(20.0, interpreter.evaluate(expr(10, star, 2)));
    }

    @Test
    public void runtimeErrorsForBinaryStarWithOneNonNumber() {
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", star, 1)));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr(1, star, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", star, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void evaluatesBinarySlash() {
        assertEquals(5.0, interpreter.evaluate(expr(10, slash, 2)));
    }

    @Test
    public void runtimeErrorForBinarySlashWithOneNonNumber() {
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", slash, 1)));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr(1, slash, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", slash, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));
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
    public void runtimeErrorForBinaryPlusWithMixedTypes() {
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", plus, 1)));
        assertTrue(error.getMessage().contains("Operands must be two numbers or two strings"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr(1, plus, "bar")));
        assertTrue(error.getMessage().contains("Operands must be two numbers or two strings"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr(true, plus, false)));
        assertTrue(error.getMessage().contains("Operands must be two numbers or two strings"));
    }

    @Test
    public void evaluatesBinaryEqual() {
        assertEquals(true, interpreter.evaluate(expr(7, doubleEqual, 7)));
        assertEquals(false, interpreter.evaluate(expr(7, doubleEqual, 14)));
    }

    //TODO: lots more cases with different types
    //TODO: runtime errors(?)

    @Test
    public void evaluatesBinaryNotEqual() {
        assertEquals(true, interpreter.evaluate(expr(7, bangEqual, 14)));
        assertEquals(false, interpreter.evaluate(expr(7, bangEqual, 7)));
    }

    //TODO: lots more cases with different types
    //TODO: runtime errors(?)

    @Test
    public void evaluatesBinaryGreaterThan() {
        assertEquals(true, interpreter.evaluate(expr(99, greater, 1)));
        assertEquals(false, interpreter.evaluate(expr(1, greater, 99)));
    }

    @Test
    public void runtimeErrorForBinaryGreaterThanWithNonNumbers() {
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", greater, 1)));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr(1, greater, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", greater, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void evaluatesBinaryGreaterThanOrEqualTo() {
        assertEquals(true, interpreter.evaluate(expr(99, greaterEqual, 1)));
        assertEquals(true, interpreter.evaluate(expr(99, greaterEqual, 99)));
        assertEquals(false, interpreter.evaluate(expr(1, greaterEqual, 99)));
    }

    @Test
    public void runtimeErrorForBinaryGreaterThanOrEqualToWithNonNumbers() {
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", greaterEqual, 1)));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr(1, greaterEqual, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", greaterEqual, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void evaluatesBinaryLessThan() {
        assertEquals(true, interpreter.evaluate(expr(1, less, 99)));
        assertEquals(false, interpreter.evaluate(expr(99, less, 1)));
    }

    @Test
    public void runtimeErrorForBinaryLessThanWithNonNumbers() {
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", less, 1)));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr(1, less, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", less, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void evaluatesBinaryLessThanOrEqualTo() {
        assertEquals(true, interpreter.evaluate(expr(1, lessEqual, 99)));
        assertEquals(true, interpreter.evaluate(expr(1, lessEqual, 1)));
        assertEquals(false, interpreter.evaluate(expr(99, lessEqual, 1)));
    }

    @Test
    public void runtimeErrorForBinaryLessThanOrEqualToWithNonNumbers() {
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", lessEqual, 1)));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr(1, lessEqual, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(expr("foo", lessEqual, "bar")));
        assertTrue(error.getMessage().contains("Operands must be numbers"));
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
}
