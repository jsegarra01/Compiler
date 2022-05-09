package tokens.leaf;

import tokens.terminal.IdenToken;
import tokens.Token;
import tokens.terminal.LitToken;
import tokens.terminal.TypeToken;

import java.io.PrintWriter;
import java.util.ArrayList;

public class VarDecToken extends Token {
    protected TypeToken type;
    protected IdenToken identifier;
    protected LitToken value;

    public VarDecToken() {
        super.name = "var_dec";
        this.type = null;
        this.identifier = null;
        this.value = null;
    }

    public TypeToken getType() {
        return type;
    }

    public IdenToken getIdentifier() {
        return identifier;
    }

    public LitToken getValue() {
        return value;
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(type);
        tmp.add(identifier);
        tmp.add(value);
        return tmp;
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
                    return this.parent;
                }
                else{
                    return this.parent;
                }
            }
        }
        catch (ClassCastException e){
            return null;
        }
        return null;
    }
    @Override
    public String getTac(PrintWriter writer) {
        String assignation;
        if(value == null) {
            assignation = "0";
        }
        else{
            assignation = value.getRaw();
        }
        String finalDeclaration = identifier.getRaw() + " = " + assignation + " ;";
        writer.println(finalDeclaration);
        return finalDeclaration;
    }
}
