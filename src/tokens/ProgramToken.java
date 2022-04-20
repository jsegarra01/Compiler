package tokens;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ProgramToken extends Token{
    protected MainToken main;

    public ProgramToken() {
        super.name = "program";
    }

    @Override
    public Object getChild() {
        return this.main;
    }

    public String getName() {
        return name;
    }

    @Override
    public Token insert(Token in) {
        try {
            this.main = (MainToken) in;
            this.main.setParent(this);
            return this.main;
        }
        catch (ClassCastException e){
            return null;
        }
    }

    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        return main.getTac(writer);
    }
}
