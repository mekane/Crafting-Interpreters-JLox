package test;

import com.craftinginterpreters.lox.Token;
import com.craftinginterpreters.lox.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.craftinginterpreters.lox.Scanner;

import static com.craftinginterpreters.lox.TokenType.*;

import java.util.List;

import static java.util.Arrays.asList;

class ScannerTest {

    @Test
    public void addsEofToken() {
        String source = "";
        Scanner scanner = new Scanner(source);

        assertTokensMatch(asList(EOF), scanner.scanTokens());
    }

    @Test
    public void scansNumbers() {
        String source = "0 1 2 4";
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        assertTokensMatch(asList(NUMBER, NUMBER, NUMBER, NUMBER), tokens);
        assertEquals(0.0, tokens.get(0).literal);
        assertEquals(1.0, tokens.get(1).literal);
        assertEquals(2.0, tokens.get(2).literal);
        assertEquals(4.0, tokens.get(3).literal);
    }

    @Test
    public void scansStrings() {
        String source = "\"foo\" \"bar\"";
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        assertTokensMatch(asList(STRING, STRING), tokens);
        assertEquals("foo", tokens.get(0).literal);
        assertEquals("bar", tokens.get(1).literal);
    }

    @Test
    public void scansBooleans() {
        String source = "true false";
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        assertTokensMatch(asList(TRUE, FALSE), tokens);
    }

    @Test
    public void scansNil() {
        String source = "nil";
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        assertTokensMatch(asList(NIL), tokens);
    }

    @Test
    public void scansLeftParenthesis() {
        String source = "(";
        Scanner scanner = new Scanner(source);

        assertTokensMatch(asList(LEFT_PAREN), scanner.scanTokens());
    }

    @Test
    public void scansRightParenthesis() {
        String source = ")";
        Scanner scanner = new Scanner(source);

        assertTokensMatch(asList(RIGHT_PAREN), scanner.scanTokens());
    }

    @Test
    public void ignoresDoubleSlashStyleComments() {
        String source = "() // this comment is ignored";
        Scanner scanner = new Scanner(source);

        assertTokensMatch(asList(LEFT_PAREN, RIGHT_PAREN), scanner.scanTokens());
    }

    @Test
    public void ignoresDoubleSlashStyleCommentsMultipleLines() {
        String source = "(\n" +
                "// this comment is ignored\n" +
                ")\n";
        Scanner scanner = new Scanner(source);

        assertTokensMatch(asList(LEFT_PAREN, RIGHT_PAREN), scanner.scanTokens());
    }

    @Test
    public void ignoresBlockComments() {
        String source = "( /* this comment and all tokens (/*+-) are ignored */ )";
        Scanner scanner = new Scanner(source);

        assertTokensMatch(asList(LEFT_PAREN, RIGHT_PAREN), scanner.scanTokens());
    }

    @Test
    public void ignoresBlockCommentsMultipleLines() {
        String source = "(\n" +
                "/* these + tokens - are * / ignored */\n" +
                ")\n";
        Scanner scanner = new Scanner(source);

        assertTokensMatch(asList(LEFT_PAREN, RIGHT_PAREN), scanner.scanTokens());
    }

    @Test
    public void scansPlusSignAndNumbers() {
        Scanner scanner = new Scanner("2 + 3");

        assertTokensMatch(asList(NUMBER, PLUS, NUMBER), scanner.scanTokens());
    }

    @Test
    public void scansMinusSignAndNumbers() {
        Scanner scanner = new Scanner("2 - 3");

        assertTokensMatch(asList(NUMBER, MINUS, NUMBER), scanner.scanTokens());
    }

    @Test
    public void scansStarAndNumbers() {
        Scanner scanner = new Scanner("3 * 4");

        assertTokensMatch(asList(NUMBER, STAR, NUMBER), scanner.scanTokens());
    }

    @Test
    public void scansQuestionMarkAndColon() {
        Scanner scanner = new Scanner("true ? true : false");

        assertTokensMatch(asList(TRUE, QUESTION, TRUE, COLON, FALSE), scanner.scanTokens());
    }




    private void assertTokensMatch(List<TokenType> expectedTokens, List<Token> actualTokens) {
        //dumpTokens(actualTokens);

        int expectedCount = expectedTokens.size();
        int reportedExpectedCount = expectedTokens.size();
        int actualCount = actualTokens.size();

        TokenType lastToken = expectedTokens.get(expectedCount - 1);
        boolean userSpecifiedEofToken = (lastToken == EOF);

        /* If the test includes the EOF token in the end of the list then we should just compare the
         * expected tokens count and actual tokens count exactly as they are.
         * If they did not include the EOF we should implicitly add it to the expected list but not
         * increase the expected count so that if the list of expected tokens doesn't match, the
         * reported number of expected tokens will match what they specified in their list in the test.
         */
        if (!userSpecifiedEofToken) {
            expectedCount++; //because of implicit EOF
            //TODO: could lower reported actual count to match (?)
        }

        String expectedLengthMessage = String.format("Expected %d tokens (including EOF), got %d", reportedExpectedCount, actualCount);
        assertEquals(expectedCount, actualCount, expectedLengthMessage);

        for (int i = 0; i < reportedExpectedCount; i++) {
            String message = String.format("Comparing token %d", i);
            assertEquals(expectedTokens.get(i), actualTokens.get(i).type, message);
        }

        /* If the test did not include EOF go ahead and assert that it's the last token
         */
        if (!userSpecifiedEofToken) {
            String msg = "Expected list of tokens to end with EOF";
            assertEquals(EOF, actualTokens.get(actualCount - 1).type, msg);
        }
    }

    private void dumpTokens(List<Token> tokens) {
        for (Token t : tokens)
            System.out.println(t);
    }
}
