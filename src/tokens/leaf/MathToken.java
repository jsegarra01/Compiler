package tokens.leaf;

import tokens.CCodeToken;
import tokens.terminal.IdenToken;
import tokens.terminal.LitToken;
import tokens.terminal.OpToken;
import tokens.Token;

import java.util.ArrayList;

public class MathToken extends Token {
    private Token left;
    private OpToken op;
    private Token right;

    public MathToken() {
        super.name = "math";
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
                if(in instanceof MathToken){
                    this.left = in;
                    this.left.setParent(this);
                    return this.left;
                }
                else if (in instanceof IdenToken || in instanceof LitToken){
                    this.left = in;
                    this.left.setParent(this);
                    return this;
                }
                else{
                    return null;
                }
            }
            else if(op == null){
                if(in instanceof OpToken){
                    this.op = (OpToken) in;
                    this.op.setParent(this);
                    return this;
                }
                else if (in instanceof CCodeToken){
                    return this.parent.insert(in);
                }
                else{
                    return null;
                }
            }
            else if(this.right == null){
                if(in instanceof MathToken){
                    this.right = in;
                    this.right.setParent(this);
                    return this.right;
                }
                else if (in instanceof IdenToken || in instanceof LitToken){
                    this.right = in;
                    this.right.setParent(this);
                    return this.parent;
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
