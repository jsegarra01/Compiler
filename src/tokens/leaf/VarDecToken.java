package tokens.leaf;

import tokens.terminal.IdenToken;
import tokens.Token;
import tokens.terminal.TypeToken;

public class VarDecToken extends Token {
    protected TypeToken type;
    protected IdenToken identifier;
    protected String value;
}
