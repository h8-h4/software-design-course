package tokenizer.token;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import tokenizer.TokenizerException;
import visitor.TokenVisitor;

import java.util.Map;

import static tokenizer.token.Brace.BraceType.LEFT;
import static tokenizer.token.Brace.BraceType.RIGHT;

@EqualsAndHashCode
@Getter
public final class Brace implements Token {
    public static final Map<Character, BraceType> BRACES = Map.of(
            '(', LEFT,
            ')', RIGHT
    );
    private final BraceType type;

    public Brace(char character) {
        if (!BRACES.containsKey(character)) {
            throw new TokenizerException("Invalid character \"%c\". Brace was expected.".formatted(character));

        }

        type = BRACES.get(character);
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return type.name();
    }

    public enum BraceType {
        LEFT, RIGHT
    }
}
