package tokens.terminal;

import tokens.Token;

import java.io.PrintWriter;

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

    @Override
    public String getTac(PrintWriter writer) {
        return super.getRaw();
    }
}
