package tokens.terminal;

import tokens.Token;

public class OpToken extends Token {
    public OpToken() {
        super.name="math_op1 math_op2";
    }

    @Override
    public Object getChild() {
        return null;
    }
}
