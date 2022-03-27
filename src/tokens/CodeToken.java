package tokens;

public class CodeToken extends Token{
    protected VarDecsToken varDecs;
    protected CCodeToken code;

    public CodeToken() {
        super.name = "code";
    }

    @Override
    public Object getChild() {
        return this.varDecs;
    }

    @Override
    public Token insert(Token in) {
        try {
            if(this.varDecs == null){
                this.varDecs = (VarDecsToken) in;
                this.varDecs.setParent(this);
                return this.varDecs;
            }
            else{
                return this.parent;
            }
        }
        catch (ClassCastException e){
            return null;
        }
    }
}
