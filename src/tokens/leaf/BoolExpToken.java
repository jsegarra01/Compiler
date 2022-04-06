package tokens.leaf;

import tokens.CCodeToken;
import tokens.IfToken;
import tokens.Token;
import tokens.terminal.*;

import java.util.ArrayList;

public class BoolExpToken extends Token {
    private Token left;
    private Token op;
    private Token right;

    public BoolExpToken() {
        super.name="bool_exp";
    }

    public void replace(Token trg, Token value){
        if(this.left == trg){
            this.left = value;
        }
        else{
            this.right = value;
        }
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(left);
        tmp.add(op);
        tmp.add(right);
        return tmp;
    }

    @Override
    public Token insert(Token in) {
        try{
            if(left == null){
                if(in instanceof IdenToken || in instanceof LitToken){
                    left = in;
                    in.setParent(this);
                    return this;
                }
            }
            else if (op == null){
                if(in instanceof BoolToken){
                    op = in;
                    in.setParent(this);
                    return this;
                }
            }
            else if(right == null){
                if(in instanceof IdenToken || in instanceof LitToken){
                    right = in;
                    in.setParent(this);
                    return this;
                }
                else if(in instanceof BoolExpToken){
                    this.right = in;
                    in.setParent(this);
                    return in;
                }
            }
            else{
                if(in instanceof CCodeToken){
                    return this.parent.insert(in);
                }
                else if(in instanceof BoolChainToken){
                    BoolExpToken tmp = new BoolExpToken();
                    tmp.left = this;
                    tmp.setParent(this.parent);
                    if(this.parent instanceof BoolExpToken){
                        ((BoolExpToken) this.parent).right = tmp;
                    }
                    else if (this.parent instanceof IfToken){
                        ((IfToken) this.parent).setCondition(tmp);
                    }
                    this.parent = tmp;
                    tmp.op = in;
                    in.setParent(tmp);
                    return tmp;
                }
                else{
                    return null;
                }
            }
        }
        catch (ClassCastException e){
            return null;
        }
        return null;
    }
}
