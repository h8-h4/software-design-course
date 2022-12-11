package tokenizer.state;

import tokenizer.TokenizerException;
import tokenizer.token.Token;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract sealed class TokenizerState permits InitialState, EndState, NumberState, WhitespaceState {
    protected final InputStream input;

    public TokenizerState(InputStream input) {
        this.input = input;
    }

    public TokenizerState next(List<Token> tokens) {
        try {
            input.mark(2);
            int c = input.read();
            if (c == -1) {
                onStateEnd(tokens);
                return new EndState(input);
            }

            char curChar = (char) c;
            if (Character.isWhitespace(curChar)) {
                onStateEnd(tokens);
                return new WhitespaceState(input);
            }

            return nextImpl(curChar, tokens);
        } catch (IOException e) {
            throw new TokenizerException(e);
        }
    }

    protected void onStateEnd(List<Token> tokens) {
    }

    protected abstract TokenizerState nextImpl(char curChar, List<Token> tokens) throws IOException;
}
