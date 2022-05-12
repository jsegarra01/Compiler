package tokens.leaf;

import tokens.Token;
import tokens.terminal.IdenToken;
import tokens.terminal.TypeToken;

import java.util.ArrayList;

public class ArgsToken extends Token {
    protected TypeToken type;
    protected IdenToken id;

    public ArgsToken() {
        super.name = "func_term";
    }

    public TypeToken getType() {
        return type;
    }

    public void setType(TypeToken type) {
        this.type = type;
    }

    public IdenToken getId() {
        return id;
    }

    public void setId(IdenToken id) {
        this.id = id;
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
}
