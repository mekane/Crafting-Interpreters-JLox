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
        assertTrue((Boolean) interpreter.evaluate(notFalse));

        //TODO: notTrue
    }

    @Test
    public void evaluatesUnaryMinus() {
        Expr.Unary notFalse = new Expr.Unary(minus, new Expr.Literal(50.0));
        assertEquals(-50.0, interpreter.evaluate(notFalse));

        //TODO: -Negative
    }

    @Test
    public void runtimeErrorForUnaryMinusWithNonNumber() {
        Expr.Unary negateNonNumber = new Expr.Unary(minus, new Expr.Literal("muffin"));
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(negateNonNumber));

        assertTrue(error.getMessage().contains("Operand must be a number"));
    }

    @Test
    public void evaluatesBinaryMinus() {
        Expr.Binary subtract = new Expr.Binary(new Expr.Literal(10.0), minus, new Expr.Literal(2.0));
        assertEquals(8.0, interpreter.evaluate(subtract));
    }

    @Test
    public void runtimeErrorForBinaryMinusWithOneNonNumber() {
        Expr.Binary subtractFromNonNumber = new Expr.Binary(new Expr.Literal("foo"), minus, new Expr.Literal(1.0));
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(subtractFromNonNumber));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        Expr.Binary subtractNonNumber = new Expr.Binary(new Expr.Literal(1.0), minus, new Expr.Literal("bar"));
        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(subtractNonNumber));
        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void runtimeErrorForBinaryMinusWithTwoNonNumbers() {
        Expr.Binary subtractNonNumbers = new Expr.Binary(new Expr.Literal("foo"), minus, new Expr.Literal("bar"));
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(subtractNonNumbers));

        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void evaluatesBinaryStar() {
        Expr.Binary multiply = new Expr.Binary(new Expr.Literal(10.0), star, new Expr.Literal(2.0));
        assertEquals(20.0, interpreter.evaluate(multiply));
    }

    @Test
    public void runtimeErrorForBinaryStarWithOneNonNumber() {
        Expr.Binary multiplyNonNumberL = new Expr.Binary(new Expr.Literal("foo"), star, new Expr.Literal(1.0));
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(multiplyNonNumberL));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        Expr.Binary multiplyNonNumberR = new Expr.Binary(new Expr.Literal(1.0), star, new Expr.Literal("bar"));
        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(multiplyNonNumberR));
        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void runtimeErrorForBinaryStarWithTwoNonNumbers() {
        Expr.Binary multiplyNonNumbers = new Expr.Binary(new Expr.Literal("foo"), star, new Expr.Literal("bar"));
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(multiplyNonNumbers));

        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void evaluatesBinarySlash() {
        Expr.Binary divide = new Expr.Binary(new Expr.Literal(10.0), slash, new Expr.Literal(2.0));
        assertEquals(5.0, interpreter.evaluate(divide));
    }

    @Test
    public void runtimeErrorForBinarySlashWithOneNonNumber() {
        Expr.Binary divideNonNumberL = new Expr.Binary(new Expr.Literal("foo"), slash, new Expr.Literal(1.0));
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(divideNonNumberL));
        assertTrue(error.getMessage().contains("Operands must be numbers"));

        Expr.Binary divideNonNumberR = new Expr.Binary(new Expr.Literal(1.0), slash, new Expr.Literal("bar"));
        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(divideNonNumberR));
        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void runtimeErrorForBinarySlashWithTwoNonNumbers() {
        Expr.Binary multiplyNonNumbers = new Expr.Binary(new Expr.Literal("foo"), slash, new Expr.Literal("bar"));
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(multiplyNonNumbers));

        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void evaluatesBinaryPlusForNumbers() {
        Expr.Binary add = new Expr.Binary(new Expr.Literal(10.0), plus, new Expr.Literal(2.0));
        assertEquals(12.0, interpreter.evaluate(add));
    }

    @Test
    public void evaluatesBinaryPlusForStrings() {
        Expr.Binary concatenate = new Expr.Binary(new Expr.Literal("foo"), plus, new Expr.Literal("bar"));
        assertEquals("foobar", interpreter.evaluate(concatenate));
    }

    @Test
    public void runtimeErrorForBinaryPlusWithMixedTypes() {
        Expr.Binary addStringAndNumber = new Expr.Binary(new Expr.Literal("foo"), plus, new Expr.Literal(1.0));
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(addStringAndNumber));
        assertTrue(error.getMessage().contains("Operands must be two numbers or two strings"));

        Expr.Binary addNumberAndString = new Expr.Binary(new Expr.Literal(1.0), plus, new Expr.Literal("bar"));
        error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(addNumberAndString));
        assertTrue(error.getMessage().contains("Operands must be two numbers or two strings"));
    }

    @Test
    public void runtimeErrorForBinaryPlusWithNeitherNumbersOrStrings() {
        Expr.Binary multiplyNonNumbers = new Expr.Binary(new Expr.Literal(true), slash, new Expr.Literal(false));
        RuntimeError error = assertThrows(RuntimeError.class, () -> interpreter.evaluate(multiplyNonNumbers));

        assertTrue(error.getMessage().contains("Operands must be numbers"));
    }

    @Test
    public void evaluatesBinaryEqual() {
        Expr.Binary expr = new Expr.Binary(new Expr.Literal(7.0), doubleEqual, new Expr.Literal(7.0));
        assertEquals(true, interpreter.evaluate(expr));

        //TODO: false case
    }

    //TODO: runtime errors

    @Test
    public void evaluatesBinaryNotEqual() {
        Expr.Binary expr = new Expr.Binary(new Expr.Literal(7.0), bangEqual, new Expr.Literal(14.0));
        assertEquals(true, interpreter.evaluate(expr));

        //TODO: false case
    }

    //TODO: runtime errors

    @Test
    public void evaluatesBinaryGreaterThan() {
        Expr.Binary expr = new Expr.Binary(new Expr.Literal(99.0), greater, new Expr.Literal(1.0));
        assertEquals(true, interpreter.evaluate(expr));

        //TODO: false case
    }

    //TODO: runtime errors

    @Test
    public void evaluatesBinaryGreaterThanOrEqualTo() {
        Expr.Binary expr = new Expr.Binary(new Expr.Literal(99.0), greaterEqual, new Expr.Literal(1.0));
        assertEquals(true, interpreter.evaluate(expr));

        //TODO: false case

        expr = new Expr.Binary(new Expr.Literal(99.0), greaterEqual, new Expr.Literal(99.0));
        assertEquals(true, interpreter.evaluate(expr));

        //TODO: false case
    }

    //TODO: runtime errors

    @Test
    public void evaluatesBinaryLessThan() {
        Expr.Binary expr = new Expr.Binary(new Expr.Literal(1.0), less, new Expr.Literal(99.0));
        assertEquals(true, interpreter.evaluate(expr));

        //TODO: false case
    }

    //TODO: runtime errors

    @Test
    public void evaluatesBinaryLessThanOrEqualTo() {
        Expr.Binary expr = new Expr.Binary(new Expr.Literal(1.0), lessEqual, new Expr.Literal(99.0));
        assertEquals(true, interpreter.evaluate(expr));

        //TODO: false case

        expr = new Expr.Binary(new Expr.Literal(1.0), lessEqual, new Expr.Literal(1.0));
        assertEquals(true, interpreter.evaluate(expr));

        //TODO: false case
    }

    //TODO: runtime errors


    private Object evaluateLiteral(Object expression) {
        Expr e = new Expr.Literal(expression);
        return interpreter.evaluate(e);
    }
}
