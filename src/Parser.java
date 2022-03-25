import tokens.MainToken;
import tokens.OpToken;
import tokens.Token;
import tokens.VarDecsToken;
import tokens.leaf.VarDecToken;
import tokens.terminal.IdenToken;
import tokens.terminal.LitToken;
import tokens.terminal.TypeToken;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Parser {

    public Parser() {
    }

    Object[] obs = new Object[15];

    private boolean isIdentifer = false;
    private boolean isLiteral = false;

    public void reader() throws IOException {
        File myFile = new File("res/test2.ap");
        Scanner sc = new Scanner(myFile, StandardCharsets.UTF_8);
        String content;
        int i = 0;
        while (sc.hasNext()){
            content = sc.useDelimiter(" ").next();
            System.out.println(content);
            while (content.equals("")){
                content = sc.useDelimiter(" ").next();
            }
            content = content.replace("\n", "").replace("\r","");
            //content.replace("\n", "");
            obs[i] = identifyString(content);
            i++;
        }
    /* if (sc.hasNext()){
            content = sc.useDelimiter(" ").next();
            while (content == " ") content = sc.useDelimiter(" ").next();
            identifyString(content);
        }*/
        //identifyString(sc.useDelimiter(" ").next());
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
        MainToken mainToken = new MainToken();
        TypeToken typeToken = new TypeToken();
        OpToken opToken = new OpToken();
        IdenToken idenToken = new IdenToken();
        LitToken litToken = new LitToken();

        if (isIdentifer) {
            idenToken.setRaw(undefined);
            isIdentifer = false;
            return idenToken;
        }

        if (isLiteral) {
            litToken.setRaw(undefined);
            isLiteral = false;
            return litToken;
        }


        if (compareToKeyword(undefined, "start", mainToken)) return mainToken;
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
