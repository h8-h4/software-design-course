package visitor;

import tokenizer.token.Brace;
import tokenizer.token.NumberToken;
import tokenizer.token.Operation;
import tokenizer.token.Token;

import java.util.List;

public interface TokenVisitor {
    void visit(NumberToken token);

    void visit(Brace token);

    void visit(Operation token);

    default void visitAll(List<Token> tokens) {
        for (Token token : tokens) {
            token.accept(this);
        }
    }
}
