import tokenizer.Tokenizer;
import tokenizer.token.Token;
import visitor.calculator.CalculatorVisitor;
import visitor.parser.ParserVisitor;
import visitor.printer.PrintVisitor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        String expression = "1 + 2 + (3 / 3) * 2 - 1";

        PrintVisitor printVisitor = new PrintVisitor();
        InputStream inputStream = new ByteArrayInputStream(expression.getBytes());

        Tokenizer tokenizer = new Tokenizer(inputStream);
        List<Token> tokens = tokenizer.tokenize();
        printVisitor.visitAll(tokens);
        System.out.println();

        ParserVisitor parser = new ParserVisitor();
        parser.visitAll(tokens);
        List<Token> polishTokens = parser.result();
        printVisitor.visitAll(polishTokens);
        System.out.println();

        CalculatorVisitor calculatorVisitor = new CalculatorVisitor();
        calculatorVisitor.visitAll(polishTokens);
        System.out.println(calculatorVisitor.result());
    }
}
