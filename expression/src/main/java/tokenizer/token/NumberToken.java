package tokenizer.token;

import lombok.Value;
import visitor.TokenVisitor;

@Value
public class NumberToken implements Token {
    long number;

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "NUMBER(%d)".formatted(number);
    }
}
