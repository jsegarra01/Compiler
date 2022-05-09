package tokens;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CodeToken extends Token{
    protected VarDecsToken varDecs;
    protected CCodeToken code;

    public CodeToken() {
        super.name = "code";
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(varDecs);
        tmp.add(code);
        return tmp;
    }

    @Override
    public Token insert(Token in) {
        try {
            if(this.varDecs == null && in instanceof VarDecsToken){
                this.varDecs = (VarDecsToken) in;
                this.varDecs.setParent(this);
                return this.varDecs;
            }
            else if(this.code == null && in instanceof CCodeToken){
                this.code = (CCodeToken) in;
                this.code.setParent(this);
                return this.code;
            }
            else{
                return this.parent.insert(in);
            }
        }
        catch (ClassCastException e){
            return null;
        }
    }

    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        return varDecs.getTac(writer) + code.getTac(writer);
    }
}
