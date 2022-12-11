package visitor.calculator;

import org.junit.jupiter.api.Test;
import tokenizer.Tokenizer;
import tokenizer.token.Token;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorVisitorTest {
    @Test
    public void testIllegalInput() {
        assertThrows(CalculatorException.class, () -> calculate("()"));
        assertThrows(CalculatorException.class, () -> calculate("1 +"));
        assertThrows(CalculatorException.class, () -> calculate("+"));
    }

    @Test
    public void testSimple() {
        assertEquals(3, calculate("2 1 +"));
    }

    @Test
    public void testAllTokens() {
        assertEquals(1, calculate("3 4 2 * 1 5 - / +"));
    }

    private static double calculate(String expression) {
        List<Token> tokens = new Tokenizer(new ByteArrayInputStream(expression.getBytes())).tokenize();

        CalculatorVisitor calculatorVisitor = new CalculatorVisitor();
        calculatorVisitor.visitAll(tokens);

        return calculatorVisitor.result();
    }
}