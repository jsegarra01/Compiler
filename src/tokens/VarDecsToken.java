package tokens;

import tokens.leaf.VarDecToken;

import java.util.ArrayList;

public class VarDecsToken extends Token{
    protected ArrayList<VarDecToken> code;

    public VarDecsToken() {
        super.name = "var_decs";
        this.code = new ArrayList<>();
    }

    @Override
    public Object getChild() {
        return code;
    }

    @Override
    public Token insert(Token in) {
        try {
            if(in instanceof VarDecToken){
                this.code.add((VarDecToken) in);
                in.setParent(this);
                return in;
            }
            else if (in instanceof VarDecsToken){
                return this;
            }
            else if(in instanceof CCodeToken){
                return this.parent.insert(in);
            }
            else{
                return null;
            }
        }
        catch (ClassCastException e){
            return null;
        }
    }
}
