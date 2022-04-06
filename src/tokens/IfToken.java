package tokens;

import tokens.CCodeToken;
import tokens.Token;
import tokens.leaf.BoolExpToken;

import java.util.ArrayList;

public class IfToken extends Token {
    private boolean codeEnable;
    protected BoolExpToken condition;
    protected CCodeToken ifCode;
    protected CCodeToken elseCode;

    public IfToken() {
        super.name = "if_stat";
        this.codeEnable = false;
    }

    public void setCondition(BoolExpToken condition) {
        this.condition = condition;
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(condition);
        tmp.add(ifCode);
        tmp.add(elseCode);
        return tmp;
    }

    @Override
    public Token insert(Token in) {
        try{
            if(condition == null){
                if(in instanceof BoolExpToken){
                    condition = (BoolExpToken) in;
                    condition.setParent(this);
                    return condition;
                }
            }
            else if(ifCode == null){
                if(in instanceof CCodeToken){
                    ifCode = (CCodeToken) in;
                    in.setParent(this);
                    return in;
                }
            }
            else if (in instanceof ElseToken){
                this.codeEnable = true;
                return this;
            }
            else if(elseCode == null && codeEnable){
                if(in instanceof CCodeToken){
                    elseCode = (CCodeToken) in;
                    in.setParent(this);
                    return in;
                }
            }
            else{
                return this.parent.insert(in);
            }
        }
        catch (ClassCastException e){
            return null;
        }
        return null;
    }
}
