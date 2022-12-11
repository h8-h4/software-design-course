package visitor.parser;

import tokenizer.token.Brace;
import tokenizer.token.NumberToken;
import tokenizer.token.Operation;
import tokenizer.token.Token;
import visitor.TokenVisitor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;

public class ParserVisitor implements TokenVisitor {
    private final List<Token> tokens = new ArrayList<>();
    private final Deque<Token> stack = new ArrayDeque<>();

    @Override
    public void visit(NumberToken token) {
        tokens.add(token);
    }

    @Override
    public void visit(Brace token) {
        switch (token.getType()) {
            case LEFT -> stack.addFirst(token);
            case RIGHT -> {
                popWhile(t -> !(t instanceof Brace));
                Token leftBrace = stack.pollFirst();

                if (leftBrace == null) {
                    throw new ParserException("Invalid expression, missing left brace");
                }
            }
        }
    }

    @Override
    public void visit(Operation token) {
        popWhile(t -> t instanceof Operation && ((Operation) t).getPriority() >= token.getPriority());
        stack.addFirst(token);
    }

    private void popWhile(Predicate<Token> predicate) {
        while (!stack.isEmpty() && predicate.test(stack.peekFirst())) {
            tokens.add(stack.removeFirst());
        }
    }

    public List<Token> result() {
        tokens.addAll(stack);
        return tokens;
    }
}
