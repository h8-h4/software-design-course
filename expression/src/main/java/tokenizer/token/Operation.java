package tokenizer.token;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import tokenizer.TokenizerException;
import visitor.TokenVisitor;

import java.util.Map;

import static tokenizer.token.Operation.OperationType.*;

@EqualsAndHashCode
@Getter
public final class Operation implements Token {
    public static final Map<Character, OperationType> OPERATIONS = Map.of(
            '+', PLUS,
            '-', MINUS,
            '/', DIV,
            '*', MULT
    );

    private final OperationType type;
    private final int priority;

    public Operation(char character) {
        if (!OPERATIONS.containsKey(character)) {
            throw new TokenizerException("Invalid character \"%c\"".formatted(character));

        }

        type = OPERATIONS.get(character);
        priority = switch (type) {
            case PLUS, MINUS -> 1;
            case DIV, MULT -> 2;
        };
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return type.name();
    }

    public enum OperationType {
        PLUS, MINUS, MULT, DIV
    }
}
