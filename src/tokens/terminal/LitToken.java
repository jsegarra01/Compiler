package tokens.terminal;

import tokens.Token;

import java.io.PrintWriter;

public class LitToken extends Token {
    public LitToken() {
        super.name = "lit";
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
