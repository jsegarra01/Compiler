package tokens;

import tokens.leaf.ArgsToken;
import tokens.terminal.FIdenToken;
import tokens.terminal.IdenToken;
import tokens.terminal.LitToken;
import tokens.terminal.TypeToken;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class FuncToken extends Token{
    protected TypeToken type;
    protected FIdenToken id;
    protected ArrayList<ArgsToken> args;
    protected CodeToken code;
    protected Token returnToken;
    private boolean argsDone;

    public FuncToken() {
        super.name = "func_dec";
        args = new ArrayList<>();
        this.argsDone = false;
    }

    public TypeToken getType() {
        return type;
    }

    public void setType(TypeToken type) {
        this.type = type;
    }

    public FIdenToken getId() {
        return id;
    }

    public void setId(FIdenToken id) {
        this.id = id;
    }

    public ArrayList<ArgsToken> getArgs() {
        return args;
    }

    public void setArgs(ArrayList<ArgsToken> args) {
        this.args = args;
    }

    public CodeToken getCode() {
        return code;
    }

    public void setCode(CodeToken code) {
        this.code = code;
    }

    public Token getReturnToken() {
        return returnToken;
    }

    public void setReturnToken(Token returnToken) {
        this.returnToken = returnToken;
    }

    public boolean isArgsDone() {
        return argsDone;
    }

    public void setArgsDone(boolean argsDone) {
        this.argsDone = argsDone;
    }

    @Override
    public Object getChild() {
        ArrayList<Token> tmp = new ArrayList<>();
        tmp.add(type);
        tmp.add(id);
        tmp.addAll(args);
        tmp.add(code);
        tmp.add(returnToken);
        return tmp;
    }

    @Override
    public Token insert(Token in) {
        try {
            if (in instanceof TypeToken && this.type == null){
                this.type = (TypeToken) in;
                in.setParent(this);
                return this;
            }
            if (in instanceof FIdenToken && this.id == null){
                this.id = (FIdenToken) in;
                in.setParent(this);
                return this;
            }
            else if (in instanceof ArgsToken && !argsDone){
                args.add((ArgsToken) in);
                in.setParent(this);
                return in;
            }
            else if(in instanceof CodeToken){
                argsDone = true;
                this.code = (CodeToken) in;
                in.setParent(this);
                return in;
            }
            else if (returnToken == null){
                if (in instanceof IdenToken || in instanceof LitToken){
                    this.returnToken = in;
                    in.parent = this;
                    return this;
                }
                else{
                    return null;
                }
            }
            else {
                return null;
            }
        }
        catch (ClassCastException e){
            return null;
        }
    }

    @Override
    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        writer.println(id.getRaw() + ":");
        for (ArgsToken arg : args) {
            arg.getTac(writer);
        }
        resetVarPassedIteration();
        code.getTac(writer);
        writer.println("return " + returnToken.getRaw());
        writer.println();
        return null;
    }
}
