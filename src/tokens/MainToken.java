package tokens;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class MainToken extends Token{
    protected CodeToken code;

    public MainToken() {
        super.name = "main_space";
    }

    @Override
    public Object getChild() {
        return this.code;
    }

    @Override
    public Token insert(Token in) {
        try {
            this.code = (CodeToken) in;
            this.code.setParent(this);
            return this.code;
        }
        catch (ClassCastException e){
            return null;
        }
    }

    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        writer.println("MAIN:");
        return code.getTac(writer);
    }
}
