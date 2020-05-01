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

        //assertTokensMatch(asList(EOF), scanner.scanTokens());
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

    private void assertTokensMatch(List<TokenType> expectedTokens, List<Token> actualTokens) {
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
}
