package tokens;

import tokens.leaf.BoolExpToken;
import tokens.leaf.MathToken;
import tokens.leaf.VarAssToken;
import tokens.leaf.VarDecToken;

public class Token {
    protected String name;
    protected String raw;
    protected Token parent;
    protected Token child;

    public Token() {
        this.parent = null;
        this.name = "token";
    }
    /**Getters*/
    public String getRaw() {
        return raw;
    }

    public Token getParent() {
        return parent;
    }

    public Object getChild() {
        return child;
    }

    public String getName() {
        return name;
    }

    /**Setters*/
    public void setRaw(String raw) {
        this.raw = raw;
    }

    public void setParent(Token parent) {
        this.parent = parent;
    }

    /**Static Methods*/
    public static Token genToken(String in){
        return switch (in) {
            case "program" -> new ProgramToken();
            case "main_space" -> new MainToken();
            case "code" -> new CodeToken();
            case "var_decs" -> new VarDecsToken();
            case "var_dec" -> new VarDecToken();
            case "ccode" -> new CCodeToken();
            case "var_ass" -> new VarAssToken();
            case "math_exp" -> new MathToken();
            case "if_stat" -> new IfToken();
            case "else_stat" -> new ElseToken();
            case "bool_exp" -> new BoolExpToken();
            default -> null;
        };
    }

    /**Object Methods*/
    public Token insert(Token in){
        this.child = in;
        this.child.setParent(this);
        return this.child;
    }

}
