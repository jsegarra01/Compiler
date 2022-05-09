package tokens.terminal;

import tokens.Token;

import java.io.PrintWriter;

public class IdenToken extends Token {
    public IdenToken() {
        super.name = "id";
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
