package tokens;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class FunctionsToken extends Token{
    protected ArrayList<FuncToken> functions;

    public FunctionsToken() {
        super.name = "func_space";
        functions = new ArrayList<>();
    }

    @Override
    public Object getChild() {
        return functions;
    }

    @Override
    public Token insert(Token in) {
        try {
            if (in instanceof FuncToken){
                functions.add((FuncToken) in);
                in.setParent(this);
                return in;
            }
            else {
                return this.parent.insert(in);
            }
        }
        catch (ClassCastException e){
            return null;
        }
    }

    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        for (FuncToken function : functions) {
            return function.getTac(writer);
        }
        return null;
    }
}
