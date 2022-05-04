package tokens;

import tokens.leaf.ArgsToken;
import tokens.terminal.IdenToken;
import tokens.terminal.TypeToken;

import java.util.ArrayList;

public class FuncToken extends Token{
    protected TypeToken type;
    protected IdenToken id;
    protected ArrayList<ArgsToken> args;
    protected CCodeToken code;
    private boolean argsDone;

    public FuncToken() {
        super.name = "func_dec";
        args = new ArrayList<>();
        this.argsDone = false;
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(type);
        tmp.add(id);
        tmp.addAll(args);
        tmp.add(code);
        return tmp;
    }

    @Override
    public Token insert(Token in) {
        try {
            if (in instanceof TypeToken && this.type == null){
                this.type = (TypeToken) in;
                in.setParent(this);
                return this;
            }
            if (in instanceof IdenToken && this.id == null){
                this.id = (IdenToken) in;
                in.setParent(this);
                return this;
            }
            else if (in instanceof ArgsToken && !argsDone){
                args.add((ArgsToken) in);
                in.setParent(this);
                return in;
            }
            else if(in instanceof CCodeToken){
                argsDone = true;
                this.code = (CCodeToken) in;
                in.setParent(this);
                return in;
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
