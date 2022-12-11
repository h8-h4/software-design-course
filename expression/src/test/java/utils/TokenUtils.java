package utils;

import tokenizer.token.Brace;
import tokenizer.token.NumberToken;
import tokenizer.token.Operation;
import tokenizer.token.Token;

public class TokenUtils {
    public static final Token PLUS = new Operation('+');
    public static final Token MINUS = new Operation('-');
    public static final Token DIV = new Operation('/');
    public static final Token MUL = new Operation('*');
    public static final Token LEFT = new Brace('(');
    public static final Token RIGHT = new Brace(')');

    public static Token number(long number) {
        return new NumberToken(number);
    }
}
