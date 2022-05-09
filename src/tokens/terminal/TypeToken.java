package tokens.terminal;

import tokens.Token;

import java.io.PrintWriter;

public class TypeToken extends Token {
    public TypeToken() {
        super.name = "type";
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
