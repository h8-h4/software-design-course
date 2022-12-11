package tokenizer;

import tokenizer.state.EndState;
import tokenizer.state.InitialState;
import tokenizer.state.TokenizerState;
import tokenizer.token.Token;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private TokenizerState state;

    public Tokenizer(InputStream input) {
        this.state = new InitialState(input);
    }

    public List<Token> tokenize() {
        ArrayList<Token> tokens = new ArrayList<>();

        while (!(state instanceof EndState)) {
            state = state.next(tokens);
        }

        return tokens;
    }
}
