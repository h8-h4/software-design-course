package visitor.integration;

import org.junit.jupiter.api.Test;
import tokenizer.Tokenizer;
import tokenizer.token.Token;
import visitor.calculator.CalculatorVisitor;
import visitor.parser.ParserVisitor;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorParserTest {

    @Test
    public void testSimple() {
        assertEquals(3, calculate("1 +  2"));
    }

    @Test
    public void testAllTokens() {
        assertEquals(
                -0.5,
                calculate("3 + 3 / 2 - (1 - 2) - 3 * 2")
        );
    }


    private static double calculate(String expression) {
        List<Token> tokens = new Tokenizer(new ByteArrayInputStream(expression.getBytes())).tokenize();

        ParserVisitor parserVisitor = new ParserVisitor();
        parserVisitor.visitAll(tokens);

        List<Token> polishTokens = parserVisitor.result();

        CalculatorVisitor calculatorVisitor = new CalculatorVisitor();
        calculatorVisitor.visitAll(polishTokens);

        return calculatorVisitor.result();
    }
}
