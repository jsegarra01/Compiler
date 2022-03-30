import tokens.terminal.OpToken;
import tokens.Token;
import tokens.terminal.IdenToken;
import tokens.terminal.LitToken;
import tokens.terminal.TypeToken;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Parser {

    private Scanner sc;
    private boolean isIdentifer = false;
    private boolean isLiteral = false;
    private boolean end;

    public Parser(String path) {
        File myFile = new File(path);
        try {
            sc = new Scanner(myFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            sc = null;
        }
        end = false;
    }

    public Object read(){
        String content;

        if (sc.hasNext()){
            content = sc.useDelimiter(" |\\n").next();
            while (content.equals("")){
                content = sc.useDelimiter(" ").next();
            }
            content = content.replace("\n", "").replace("\r","");
            return identifyString(content);
        }
        else{
            if(!end){
                end = true;
                return "$";
            }
            else{
                return null;
            }
        }
    }

    private boolean compareToKeyword(String word,String keyword, Token token) {
        if(word.equals(keyword)) {
            if(token.getClass().equals(TypeToken.class)) isIdentifer = true;
            if(token.getClass().equals(OpToken.class)) isLiteral = true;
            token.setRaw(word);
            return true;
        } else return false;
    }

    private Object identifyString(String undefined) {
        TypeToken typeToken = new TypeToken();
        OpToken opToken = new OpToken();
        IdenToken idenToken = new IdenToken();
        LitToken litToken = new LitToken();

        if (isIdentifer || undefined.contains("$")) {
            idenToken.setRaw(undefined.replace("$", ""));
            isIdentifer = false;
            return idenToken;
        }

        if (isLiteral) {
            litToken.setRaw(undefined);
            isLiteral = false;
            return litToken;
        }


        if (compareToKeyword(undefined, "int", typeToken)) return typeToken;
        if (compareToKeyword(undefined, "char", typeToken)) return typeToken;
        if (compareToKeyword(undefined, "bool", typeToken)) return typeToken;
        if (compareToKeyword(undefined, "float", typeToken)) return typeToken;

        if (compareToKeyword(undefined, "+", opToken)) return opToken;
        if (compareToKeyword(undefined, "-", opToken)) return opToken;
        if (compareToKeyword(undefined, "*", opToken)) return opToken;
        if (compareToKeyword(undefined, "/", opToken)) return opToken;


        if (undefined.equals("=")) isLiteral = true;

        return undefined;
    }
}
