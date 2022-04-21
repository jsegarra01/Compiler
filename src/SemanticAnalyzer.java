import tokens.Token;
import tokens.terminal.IdenToken;
import tokens.terminal.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;


public class SemanticAnalyzer {
    private HashMap<String , Token> semanticTable;
    private String[][] mapTypes = {
            {"int", "+", "-", "*", "/"},
            {"char", },
            {"float","+", "-", "*", "/"},
            {"bool", "==", "<", ">", "!=", "||", "&&"}
    };


    public SemanticAnalyzer() {
        semanticTable = new HashMap<>();
    }

    public boolean varCreated(String id, TypeToken token) {

        if(semanticTable.containsKey(id)) return false;

        semanticTable.put(id, token);
        return true;
    }

    public boolean exists(String id) {
        return semanticTable.containsKey(id);
    }

    public void printAll() {
        semanticTable.forEach((key, token) -> {
            System.out.println("Variable " + key + ": has value -> " + token.getRaw());
        });

    }

    public Token getVar(String key) {
        return semanticTable.get(key);

    }

}
