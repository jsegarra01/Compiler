import tokens.terminal.*;
import tokens.Token;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {

    private Scanner sc;
    private String[] typeTokens = {"int", "char", "float", "bool"};
    private String[] opTokens = {"+", "-", "*", "/"};
    private String[] boolOps = {"==", "<", ">", "!="};
    private String[] boolChains = {"||", "&&"};
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
            //content = sc.useDelimiter(" |\\n").next();
            content = sc.next();
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

    private boolean isLiteral(String word, Token token) {
        if(word.equals("true") || word.equals("false")) token.setRaw(word);
        else {
            if(word.charAt(0) == '\'') token.setRaw(word.substring(1, word.length() -1));
            else {
                try {
                    double d = Double.parseDouble(word);
                    token.setRaw(word);
                } catch (NumberFormatException nfe) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isIdentifer(String word, Token token) {
        if('$' == word.charAt(0) && word.length() > 1) {
            token.setRaw(word.substring(1));
            return true;
        } else return false;
    }
    private boolean isFIdentifer(String word, Token token) {
        if('#' == word.charAt(0) && word.length() > 1) {
            token.setRaw(word.substring(1));
            return true;
        } else return false;
    }

    private boolean compareToKeyword(String word,String[] keywords, Token token) {
        for (String keyword: keywords) {
            if(word.equals(keyword)) {
                token.setRaw(word);
                return true;
            }
        }
        return false;
    }

    private boolean isBoolOp(String word){
        return Arrays.asList(boolOps).contains(word);
    }
    private boolean isBoolChain(String word){
        return Arrays.asList(boolChains).contains(word);
    }

    private Object identifyString(String undefined) {
        TypeToken typeToken = new TypeToken();
        OpToken opToken = new OpToken();
        IdenToken idenToken = new IdenToken();
        FIdenToken fIdenToken = new FIdenToken();
        LitToken litToken = new LitToken();


        if (compareToKeyword(undefined, typeTokens, typeToken)) return typeToken;
        if (compareToKeyword(undefined, opTokens, opToken)) return opToken;
        if (isBoolOp(undefined)) return new BoolToken(undefined);
        if (isBoolChain(undefined)) return new BoolChainToken(undefined);
        if (isIdentifer(undefined, idenToken)) return idenToken;
        if (isFIdentifer(undefined, fIdenToken)) return fIdenToken;
        if (isLiteral(undefined, litToken)) return litToken;

        return undefined;
    }
}
