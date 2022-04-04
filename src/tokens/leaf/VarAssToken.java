package tokens.leaf;

import tokens.CCodeToken;
import tokens.Token;
import tokens.terminal.IdenToken;
import tokens.terminal.LitToken;
import tokens.terminal.TypeToken;

import java.util.ArrayList;

public class VarAssToken extends Token {
    private IdenToken id;
    private MathToken value;

    public VarAssToken() {
        super.name = "var_ass";
    }

    public void setValue(MathToken value) {
        this.value = value;
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(id);
        tmp.add(value);
        return tmp;
    }

    @Override
    public Token insert(Token in) {
        try {
            if(this.id == null){
                this.id = (IdenToken) in;
                this.id.setParent(this);
                return this;
            }
            else if(this.value == null){
                if(in instanceof MathToken){
                    this.value = (MathToken) in;
                    this.value.setParent(this);
                    return this.value;
                }
                else{
                    return null;
                }
            }
            else if (in instanceof CCodeToken){
                return this.parent.insert(in);
            }
        }
        catch (ClassCastException e){
            return null;
        }
        return null;
    }
}
