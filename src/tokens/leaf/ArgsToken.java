package tokens.leaf;

import tokens.Token;
import tokens.terminal.IdenToken;
import tokens.terminal.TypeToken;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ArgsToken extends Token {
    protected TypeToken type;
    protected IdenToken id;

    public ArgsToken() {
        super.name = "func_term";
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(type);
        tmp.add(id);
        return tmp;
    }

    @Override
    public Token insert(Token in) {
        try {
            if (type == null){
                if (in instanceof TypeToken){
                    this.type = (TypeToken) in;
                    in.setParent(this);
                    return this;
                }
                else {
                    return null;
                }
            }
            else if (id == null){
                if (in instanceof IdenToken){
                    this.id = (IdenToken) in;
                    in.setParent(this);
                    return this.parent;
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        catch (ClassCastException e){
            return null;
        }
    }

    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        String finalDeclaration = id.getRaw() + " := $a" + getVarPassedIteration();
        increaseVarPassedIteration();
        writer.println(finalDeclaration);
        return finalDeclaration;
    }
}
