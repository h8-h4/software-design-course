package tokenizer;

import org.junit.jupiter.api.Test;
import tokenizer.token.Token;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.TokenUtils.*;

class TokenizerTest {
    @Test
    public void testSimple() {
        assertEquals(
                List.of(number(1), PLUS, number(2)),
                tokenize("1 + 2")
        );
    }

    @Test
    public void testAllTokens() {
        assertEquals(
                List.of(
                        LEFT, number(1), PLUS, number(2), RIGHT, MINUS,
                        LEFT, number(2), PLUS, number(3), RIGHT,
                        DIV, number(3), MUL, number(1)
                ),
                tokenize("(1 + 2) - (2+3) / 3 * 1")
        );
    }

    @Test
    public void testNested() {
        assertEquals(
                List.of(LEFT, LEFT, RIGHT, RIGHT, RIGHT),
                tokenize("(()))")
        );
    }

    @Test
    public void testBigNumber() {
        assertEquals(
                List.of(number(12345678910L)),
                tokenize("12345678910")
        );
    }

    @Test
    public void testIncorrectChars() {
        assertThrows(TokenizerException.class, () -> tokenize("1 + %"));
        assertThrows(TokenizerException.class, () -> tokenize("asdfas"));
        assertThrows(TokenizerException.class, () -> tokenize("(223!4234)"));
    }


    public List<Token> tokenize(String s) {
        return new Tokenizer(new ByteArrayInputStream(s.getBytes())).tokenize();
    }
}