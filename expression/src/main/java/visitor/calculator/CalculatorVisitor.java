package visitor.calculator;

import tokenizer.token.Brace;
import tokenizer.token.NumberToken;
import tokenizer.token.Operation;
import visitor.TokenVisitor;

import java.util.ArrayDeque;
import java.util.Deque;

public class CalculatorVisitor implements TokenVisitor {
    private final Deque<Double> stack = new ArrayDeque<>();

    @Override
    public void visit(NumberToken token) {
        stack.addFirst((double) token.getNumber());
    }

    @Override
    public void visit(Brace token) {
        throw new CalculatorException("Invalid token %s".formatted(token));
    }

    @Override
    public void visit(Operation token) {
        if (stack.size() < 2) {
            throw new CalculatorException("Not enough operands for binary operation %s".formatted(token.getType()));
        }

        double b = stack.removeFirst();
        double a = stack.removeFirst();

        double res = switch (token.getType()) {
            case PLUS -> a + b;
            case MINUS -> a - b;
            case DIV -> a / b;
            case MULT -> a * b;
        };
        stack.addFirst(res);
    }

    public double result() {
        Double result = stack.pollFirst();

        if (result == null) {
            throw new CalculatorException("Invalid expression");
        }
        return result;
    }
}
