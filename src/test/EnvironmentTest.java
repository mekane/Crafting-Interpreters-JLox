package test;

import com.craftinginterpreters.lox.Environment;
import com.craftinginterpreters.lox.RuntimeError;
import com.craftinginterpreters.lox.Token;
import com.craftinginterpreters.lox.TokenType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentTest {
    static Token variable = new Token(TokenType.IDENTIFIER, "name", null, 0);
    static Token undefined = new Token(TokenType.IDENTIFIER, "undefined", null, 0);

    @Test
    public void itSupportSettingAndSettingValues() {
        Environment e = new Environment();
        e.define(variable.lexeme, "value");

        assertEquals("value", e.get(variable));
    }

    @Test
    public void itThrowsRuntimeErrorWhenAccessingUndefined() {
        Environment e = new Environment();
        RuntimeError error = assertThrows(RuntimeError.class, () -> e.get(undefined));
        assertTrue(error.getMessage().contains("Undefined variable"), "Got " + error.getMessage());
    }

    @Test
    public void itSupportsReAssigningValuesThatHaveBeenSet() {
        Environment e = new Environment();
        e.define(variable.lexeme, "value");
        e.assign(variable, "newValue");

        assertEquals("newValue", e.get(variable));
    }

    @Test
    void itThrowsRuntimeErrorWhenAssigningUndefined() {
        Environment e = new Environment();
        RuntimeError error = assertThrows(RuntimeError.class, () -> e.assign(undefined, true));
        assertTrue(error.getMessage().contains("Undefined variable"), "Got " + error.getMessage());
    }

    @Test
    public void itSupportsGettingValuesFromParentEnvironment() {
        Environment parent = new Environment();
        parent.define(variable.lexeme, "value");

        Environment e = new Environment(parent);
        assertEquals("value", e.get(variable));
    }

    @Test
    public void itSupportsReAssigningValuesThatHaveBeenSetOnParent() {
        Environment parent = new Environment();
        parent.define(variable.lexeme, "value");

        Environment e = new Environment(parent);
        e.assign(variable, "newValue");

        assertEquals("newValue", e.get(variable));
        assertEquals("newValue", parent.get(variable));
    }
}