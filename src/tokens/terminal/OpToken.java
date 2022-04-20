package tokens.terminal;

import tokens.Token;

import java.io.PrintWriter;

public class OpToken extends Token {
    public OpToken() {
        super.name="math_op1 math_op2";
    }

    @Override
    public Object getChild() {
        return null;
    }

    @Override
    public String getTac(PrintWriter writer) {
        return super.getRaw();
    }
}
