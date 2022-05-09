package tokens;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ProgramToken extends Token{
    protected FunctionsToken functions;
    protected MainToken main;

    public ProgramToken() {
        super.name = "program";
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(functions);
        tmp.add(main);
        return tmp;
    }

    public String getName() {
        return name;
    }

    @Override
    public Token insert(Token in) {
        try {
            if (functions == null && in instanceof FunctionsToken){
                this.functions = (FunctionsToken) in;
                this.functions.setParent(this);
                return this.functions;
            }
            else if (this.main == null && in instanceof MainToken) {
                this.main = (MainToken) in;
                this.main.setParent(this);
                return this.main;
            }
            else{
                return null;
            }
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
