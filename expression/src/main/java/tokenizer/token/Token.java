package tokenizer.token;

import visitor.TokenVisitor;

public sealed interface Token permits Brace, NumberToken, Operation {
    void accept(TokenVisitor visitor);
}
