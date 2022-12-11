package visitor.printer;

import tokenizer.token.Brace;
import tokenizer.token.NumberToken;
import tokenizer.token.Operation;
import tokenizer.token.Token;
import visitor.TokenVisitor;

public class PrintVisitor implements TokenVisitor {
    @Override
    public void visit(NumberToken token) {
        print(token);
    }

    @Override
    public void visit(Brace token) {
        print(token);
    }

    @Override
    public void visit(Operation token) {
        print(token);
    }

    private static void print(Token token) {
        System.out.printf("%s ", token);
    }
}
