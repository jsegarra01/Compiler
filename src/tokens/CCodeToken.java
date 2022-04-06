package tokens;

import tokens.leaf.VarAssToken;

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
            if (in instanceof VarAssToken || in instanceof IfToken){
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
        }
        catch (ClassCastException e){
            return null;
        }
        return null;
    }
}
