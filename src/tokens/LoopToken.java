package tokens;

import tokens.leaf.BoolExpToken;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class LoopToken extends Token{
    protected BoolExpToken condition;
    protected CCodeToken code;

    public LoopToken() {
        super.name = "loop_stat";
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(condition);
        tmp.add(code);
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
            else if(code == null){
                if(in instanceof CCodeToken){
                    code = (CCodeToken) in;
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

    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        condition.setIsLoop(true);
        int labelUsed = condition.getLabel();
        writer.println("L" + labelUsed + ":");
        increaseLabelIteration();
        String print = condition.getTac(writer);
        code.getTac(writer);
        writer.println("GOTO L" + labelUsed);
        increaseLabelIteration();
        writer.println("L" + (labelUsed + 3) + ":");
        return print;
    }
}
