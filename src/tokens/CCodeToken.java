package tokens;

import tokens.leaf.VarAssToken;
import tokens.terminal.IdenToken;
import tokens.terminal.LitToken;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CCodeToken extends Token{
    private ArrayList<Token> code;

    public CCodeToken() {
        super.name = "ccode";
        this.code = new ArrayList<>();
    }

    @Override
    public Object getChild() {
        return code;
    }

    @Override
    public Token insert(Token in) {
        try {
            if (in instanceof VarAssToken || in instanceof IfToken || in instanceof LoopToken){
                code.add(in);
                in.setParent(this);
                return in;
            }
            else if (in instanceof ElseToken){
                return code.get(code.size()-1);
            }
            else if (in instanceof CCodeToken){
                return this;
            }
            else if (in instanceof IdenToken || in instanceof LitToken){
                return this.parent.insert(in);
            }
        }
        catch (ClassCastException e){
            return null;
        }
        return null;
    }
    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        StringBuilder finalTac = new StringBuilder();
        for (Token token : code) {
            finalTac.append(token.getTac(writer));
        }
        return finalTac.toString();
    }
}
