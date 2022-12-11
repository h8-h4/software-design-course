package tokenizer.state;

import tokenizer.token.Token;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class WhitespaceState extends TokenizerState {
    public WhitespaceState(InputStream input) {
        super(input);
    }

    @Override
    protected TokenizerState nextImpl(char curChar, List<Token> tokens) throws IOException {
        if (Character.isWhitespace(curChar)) {
            return this;
        }
        input.reset();
        return new InitialState(input);
    }
}
