package tokens.terminal;

import tokens.Token;

public class BoolChainToken extends Token {
    public BoolChainToken() {
        super.name="bool_chain";
    }

    @Override
    public Object getChild() {
        return null;
    }
}
