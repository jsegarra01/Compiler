package tokens.terminal;

import tokens.Token;

public class BoolToken extends Token {
    public BoolToken() {
        super.name="bool_op";
    }

    public  BoolToken(String raw){
        super.name = "bool_op";
        super.raw = raw;
    }

    @Override
    public Object getChild() {
        return null;
    }
}
