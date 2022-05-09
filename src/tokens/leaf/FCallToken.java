package tokens.leaf;

import tokens.CCodeToken;
import tokens.Token;
import tokens.terminal.FIdenToken;
import tokens.terminal.IdenToken;
import tokens.terminal.LitToken;

import java.util.ArrayList;

public class FCallToken extends Token {
    protected FIdenToken id;
    protected ArrayList<Token> args;

    public FCallToken() {
        super.name = "func_call";
        this.args = new ArrayList<>();
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(id);
        tmp.addAll(args);
        return tmp;
    }

    @Override
    public Token insert(Token in) {
        try {
            if (id == null && in instanceof FIdenToken){
                this.id = (FIdenToken) in;
                in.setParent(this);
                return this;
            }
            else if (in instanceof IdenToken || in instanceof LitToken){
                this.args.add(in);
                in.setParent(this);
                return this;
            }
            else if (in instanceof CCodeToken){
                return this.parent.insert(in);
            }
            else {
                return null;
            }
        }
        catch (ClassCastException e){
            return null;
        }
    }
}
