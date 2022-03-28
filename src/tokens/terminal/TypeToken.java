package tokens.terminal;

import tokens.Token;

public class TypeToken extends Token {
    public TypeToken() {
        super.name = "type";
    }

    @Override
    public Object getChild() {
        return null;
    }
}
