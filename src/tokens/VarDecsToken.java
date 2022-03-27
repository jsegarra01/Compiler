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
            System.out.print("");
            this.code.add((VarDecToken) in);
            in.setParent(this);
            return in;
        }
        catch (ClassCastException e){
            return this.parent;
        }
    }
}
