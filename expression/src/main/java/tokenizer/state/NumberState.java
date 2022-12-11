package tokenizer.state;

import tokenizer.token.NumberToken;
import tokenizer.token.Token;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class NumberState extends TokenizerState {
    private long number;

    public NumberState(InputStream input, int number) {
        super(input);
        this.number = number;
    }

    @Override
    protected void onStateEnd(List<Token> tokens) {
        tokens.add(new NumberToken(number));
    }

    @Override
    protected TokenizerState nextImpl(char curChar, List<Token> tokens) throws IOException {
        if (!Character.isDigit(curChar)) {
            tokens.add(new NumberToken(number));
            input.reset();
            return new InitialState(input);
        }

        number = number * 10 + Character.digit(curChar, 10);
        return this;
    }

}
