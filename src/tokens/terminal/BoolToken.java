package tokens.terminal;

import tokens.Token;

public class BoolToken extends Token {
    public BoolToken() {
        super.name="bool_op";
    }

    @Override
    public Object getChild() {
        return null;
    }
}
