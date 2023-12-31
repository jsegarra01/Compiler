package tokens.leaf;

import tokens.CCodeToken;
import tokens.Token;
import tokens.terminal.IdenToken;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class VarAssToken extends Token {
    private IdenToken id;
    private Token value;

    public VarAssToken() {
        super.name = "var_ass";
    }

    //TODO: This is used for the tac it will need to be changed to deal with a function call
    public IdenToken getId() {
        return id;
    }

    public Token getValue() {
        return value;
    }

    public void setValue(Token value) {
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
                    this.value = in;
                    this.value.setParent(this);
                    return this.value;
                }
                else if (in instanceof FCallToken){
                    this.value = in;
                    in.setParent(this);
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

    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        if(value.getClass() == MathToken.class) {
            String finalAssignation = id.getTac(writer) + " = " + value.getTac(writer);
            writer.println(finalAssignation);
            return finalAssignation;
        }
        else {
            FCallToken fCallToken = (FCallToken) value;
            ArrayList<Token> arguments = fCallToken.getArgs();
            for (Token argument : arguments) {
                writer.println("$a" + getVarPassedIteration() + " = " + argument.getRaw());
                increaseVarPassedIteration();
            }
            writer.println("call " + fCallToken.id.getRaw());
            writer.println(id.getRaw() + " = $v0");
            resetVarPassedIteration();
            return null;
        }
    }
}
