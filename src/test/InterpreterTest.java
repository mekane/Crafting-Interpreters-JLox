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
    }

    @Test
    public void evaluatesUnaryMinus() {
        Expr.Unary notFalse = new Expr.Unary(minus, new Expr.Literal(50.0));
        assertEquals(-50.0, interpreter.evaluate(notFalse));
    }

    @Test
    public void evaluatesBinaryMinus() {
        Expr.Binary subtract = new Expr.Binary(new Expr.Literal(10.0), minus, new Expr.Literal(2.0));
        assertEquals(8.0, interpreter.evaluate(subtract));
    }

    @Test
    public void evaluatesBinaryStar() {
        Expr.Binary multiply = new Expr.Binary(new Expr.Literal(10.0), star, new Expr.Literal(2.0));
        assertEquals(20.0, interpreter.evaluate(multiply));
    }

    @Test
    public void evaluatesBinarySlash() {
        Expr.Binary divide = new Expr.Binary(new Expr.Literal(10.0), slash, new Expr.Literal(2.0));
        assertEquals(5.0, interpreter.evaluate(divide));
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

    private Object evaluateLiteral(Object expression) {
        Expr e = new Expr.Literal(expression);
        return interpreter.evaluate(e);
    }
}
