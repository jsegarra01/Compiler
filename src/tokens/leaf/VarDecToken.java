package tokens.leaf;

import tokens.terminal.IdenToken;
import tokens.Token;
import tokens.terminal.LitToken;
import tokens.terminal.TypeToken;

public class VarDecToken extends Token {
    protected TypeToken type;
    protected IdenToken identifier;
    protected LitToken value;

    public VarDecToken() {
        //TODO: Figure out what to do with the extra part
        super.name = "var_dec";
        this.type = null;
        this.identifier = null;
        this.value = null;
    }

    @Override
    public Token insert(Token in) {
        try {
            if(this.type == null){
                this.type = (TypeToken) in;
                this.type.setParent(this);
                return this;
            }
            else if(this.identifier == null){
                this.identifier = (IdenToken) in;
                this.identifier.setParent(this);
                return this;
            }
            else if(this.value == null){
                if(in instanceof LitToken){
                    this.value = (LitToken) in;
                    this.value.setParent(this);
                    return this.getParent();
                }
                else{
                    return this.getParent().insert(in);
                }
            }
        }
        catch (ClassCastException e){
            return null;
        }
        return null;
    }
}
