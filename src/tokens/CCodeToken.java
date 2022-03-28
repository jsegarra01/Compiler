package tokens;

import java.util.ArrayList;

public class CCodeToken extends Token{
    private ArrayList<Token> code;

    public CCodeToken() {
        super.name = "ccode";
    }
}
