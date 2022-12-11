package tokenizer.state;

import tokenizer.TokenizerException;
import tokenizer.token.Brace;
import tokenizer.token.Operation;
import tokenizer.token.Token;

import java.io.InputStream;
import java.util.List;

public final class InitialState extends TokenizerState {
    public InitialState(InputStream input) {
        super(input);
    }

    @Override
    public TokenizerState nextImpl(char curChar, List<Token> tokens) {
        if (Character.isDigit(curChar)) {
            return new NumberState(input, Character.digit(curChar, 10));
        }

        if (Operation.OPERATIONS.containsKey(curChar)) {
            tokens.add(new Operation(curChar));
            return this;
        }

        if (Brace.BRACES.containsKey(curChar)) {
            tokens.add(new Brace(curChar));
            return this;
        }

        throw new TokenizerException("Invalid character \"%c\"".formatted(curChar));
    }
}
