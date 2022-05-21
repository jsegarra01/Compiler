package tokens.terminal;

import tokens.Token;

import java.io.PrintWriter;

public class BoolChainToken extends Token {
    public BoolChainToken() {
        super.name="bool_chain";
    }

    public BoolChainToken(String raw) {
        super.name="bool_chain";
        super.raw = raw;
    }

    @Override
    public Object getChild() {
        return null;
    }

    @Override
    public String getTac(PrintWriter writer) {
        return super.getRaw();
    } //super.getRaw()
}
