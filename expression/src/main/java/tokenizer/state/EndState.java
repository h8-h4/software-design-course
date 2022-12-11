package tokenizer.state;

import tokenizer.token.Token;

import java.io.InputStream;
import java.util.List;

public final class EndState extends TokenizerState {
    public EndState(InputStream input) {
        super(input);
    }

    @Override
    protected TokenizerState nextImpl(char curChar, List<Token> tokens) {
        return this;
    }
}
