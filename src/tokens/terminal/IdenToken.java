package tokens.terminal;

import tokens.Token;

public class IdenToken extends Token {
    public IdenToken() {
        super.name = "id";
    }

    @Override
    public Object getChild() {
        return null;
    }
}
