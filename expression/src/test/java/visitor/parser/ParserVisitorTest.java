package visitor.parser;

import org.junit.jupiter.api.Test;
import tokenizer.Tokenizer;
import tokenizer.token.Token;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.TokenUtils.*;

class ParserVisitorTest {
    @Test
    public void testMissingBrace() {
        assertThrows(ParserException.class, () -> parse("(2))"));
        assertThrows(ParserException.class, () -> parse(")"));
    }

    @Test
    public void testSimple() {
        assertEquals(
                List.of(number(1), number(2), PLUS),
                parse("1 + 2")
        );
    }

    @Test
    public void testPriorities() {
        assertEquals(
                List.of(number(3), number(1), number(5), MUL, number(4), DIV, PLUS, number(2), MINUS),
                parse("3 + 1 * 5 / 4 - 2")
        );
    }

    @Test
    public void testAllTokens() {
        assertEquals(
                List.of(number(3), number(4), number(2), MUL, number(1), number(5), MINUS, DIV, PLUS),
                parse("3 + 4 * 2 / (1 - 5)")
        );
    }

    public List<Token> parse(String expression) {
        List<Token> tokens = new Tokenizer(new ByteArrayInputStream(expression.getBytes())).tokenize();

        ParserVisitor parserVisitor = new ParserVisitor();
        parserVisitor.visitAll(tokens);

        return parserVisitor.result();
    }
}