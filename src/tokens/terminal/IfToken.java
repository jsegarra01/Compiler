package tokens.terminal;

import tokens.CCodeToken;
import tokens.Token;
import tokens.leaf.BoolExpToken;

public class IfToken extends Token {
    protected BoolExpToken condition;
    protected CCodeToken ifCode;
    protected CCodeToken elseCode;

    public IfToken() {
        super.name = "if_stat";
    }

    public void setCondition(BoolExpToken condition) {
        this.condition = condition;
    }
}
