package tokens.leaf;

import tokens.CCodeToken;
import tokens.terminal.IdenToken;
import tokens.terminal.LitToken;
import tokens.terminal.OpToken;
import tokens.Token;

import java.io.*;
import java.util.ArrayList;

public class MathToken extends Token {
    //TODO: Need to change grammar to be able to accept two multiplications or divs next to each other
    private Token left;
    private OpToken op;
    private Token right;


    public MathToken() {
        super.name = "math";
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
                    return this;
                }
                else{
                    return null;
                }
            }
            else if(in instanceof OpToken){
                if(this.op.getRaw().equals("*") || this.op.getRaw().equals("/")){
                    MathToken tmp = new MathToken();
                    tmp.setParent(this.getParent());
                    if(this.getParent() instanceof VarAssToken){
                        VarAssToken parent = (VarAssToken) this.getParent();
                        parent.setValue(tmp);
                    }
                    else {
                        MathToken parent = (MathToken) this.getParent();
                        parent.replace(this, tmp);
                    }
                    this.setParent(tmp);
                    tmp.left = this;
                    tmp.op = (OpToken) in;
                    return tmp;
                }
                //else if(in.getRaw().equals("*") || in.getRaw().equals("/")){
                else {
                    MathToken tmp = new MathToken();
                    tmp.left = this.right;
                    this.right = tmp;
                    tmp.setParent(this);
                    tmp.op = (OpToken) in;
                    return tmp;
                }
            }
            else if (in instanceof CCodeToken || in instanceof VarAssToken){
                return this.parent.insert(in);
            }
        }
        catch (ClassCastException e){
            return null;
        }
        return null;
    }

    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        String leftDisplay;
        String rightDisplay;
        leftDisplay = left.getRaw();
        rightDisplay = right.getRaw();
        if(left.getClass() == MathToken.class) {
            leftDisplay = left.getTac(writer);
        }
        if(right.getClass() == MathToken.class){
            rightDisplay = right.getTac(writer);
        }
        increaseIteration();

        String mathStep = "t" + getIteration() + " = " + leftDisplay + " " + op.getRaw() + " " + rightDisplay + " ;";
        writer.println(mathStep);
        return "t" + getIteration();
    }
}
