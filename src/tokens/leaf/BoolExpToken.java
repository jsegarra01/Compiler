package tokens.leaf;

import tokens.CCodeToken;
import tokens.IfToken;
import tokens.Token;
import tokens.terminal.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Objects;

public class BoolExpToken extends Token {
    private Token left;
    private Token op;
    private Token right;
    private static int conditionPart;
    private static boolean isLoop;

    private static boolean enteredParent;

    public BoolExpToken() {
        super.name="bool_exp";
    }

    public Token getLeft() {
        return left;
    }

    public Token getOp() {
        return op;
    }

    public Token getRight() {
        return right;
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
    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        String leftDisplay;
        String rightDisplay;
        leftDisplay = left.getRaw();
        rightDisplay = right.getRaw();

        if(left.getClass() == BoolExpToken.class) {
            leftDisplay = left.getTac(writer);
        }
        if(right.getClass() == BoolExpToken.class){
            rightDisplay = right.getTac(writer);
        }

        if(parent.getClass() == BoolExpToken.class && !enteredParent) {
            //writer.println("t" + conditionPart + " = (" + leftDisplay + op.getRaw() + rightDisplay + ")");
            //conditionPart++;
            BoolExpToken thisParent = (BoolExpToken) parent;
            /*while(thisParent.parent.getClass() == BoolExpToken.class && ) {

                BoolExpToken highestParent = (BoolExpToken) thisParent.parent;
            }trying to add more concatenations*/
            BoolExpToken concatLeft = (BoolExpToken) thisParent.getLeft();
            BoolChainToken concatOp = (BoolChainToken) thisParent.getOp();
            BoolExpToken concatRight = (BoolExpToken) thisParent.getRight();
            enteredParent = true;
            concatLeft.getTac(writer);
            writer.println(concatOp.getRaw());
            concatRight.getTac(writer);
            enteredParent = false;
            return "t" + (conditionPart - 1);
        }
        String invertedOp = invertOperation(op.getRaw());
        if(isLoop) {
            increaseLabelIteration();

            writer.println("IF ( " + leftDisplay + " " + invertedOp + " " + rightDisplay + " ) " + "GOTO L" + (getLabelIteration() + 1));
            return "L" + getLabelIteration() + ":";
        }
        else{
            increaseLabelIteration();
            writer.println("IF ( " + leftDisplay + " " + invertedOp + " " + rightDisplay + " ) " + "GOTO L" + getLabelIteration());
            return "L" + getLabelIteration() + ":";
        }

    }

    public String invertOperation(String op) {
        return switch (op) {
            case "<" -> ">=";
            case "==" -> "!=";
            case ">" -> "<=";
            case "!=" -> "==";
            case "<=" -> ">";
            default -> "<";
        };
    }

    public int getLabel() {
        return getLabelIteration();
    }

    public void setIsLoop(boolean input) {
        isLoop = input;
    }
}
