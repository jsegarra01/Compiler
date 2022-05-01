package tokens;

import tokens.leaf.BoolExpToken;
import tokens.leaf.MathToken;
import tokens.leaf.VarAssToken;
import tokens.leaf.VarDecToken;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Token {
    protected String name;
    protected String raw;
    protected Token parent;
    protected Token child;
    private static int iteration;
    private static int labelIteration;
    static {
        iteration = 0;
        labelIteration = 0;
    }

    public static void increaseIteration() {
        iteration++;
    }

    public static void increaseLabelIteration() {
        labelIteration++;
    }

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

    public String getTac(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        return child.getTac(writer);
    }

    public static int getIteration() {
        return iteration;
    }
    public static int getLabelIteration() {
        return labelIteration;
    }
}
