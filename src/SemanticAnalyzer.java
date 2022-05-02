import tokens.Token;
import tokens.leaf.BoolExpToken;
import tokens.leaf.MathToken;
import tokens.leaf.VarAssToken;
import tokens.leaf.VarDecToken;
import tokens.terminal.IdenToken;
import tokens.terminal.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;


public class SemanticAnalyzer {
    private HashMap<String , ArrayList<String>> semanticTable;


    public SemanticAnalyzer() {
        semanticTable = new HashMap<>();
    }

    public boolean varCreated(String type, String id, String scope) {

        if(semanticTable.containsKey(id)) return false;

        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(type);
        tmp.add(scope);
        semanticTable.put(id, tmp);
        return true;
    }

    public boolean exists(String id) {
        return semanticTable.containsKey(id);
    }

    public void printAll() {
        semanticTable.forEach((key, token) -> {
            System.out.println("Variable " + key + ": has value -> " + token.get(0));
        });

    }

    public ArrayList<String> getVar(String key) {
        return semanticTable.get(key);

    }

    public boolean varDecValidate(VarDecToken token, String scope) {
        if(!varCreated(token.getType().getRaw(), token.getIdentifier().getRaw(), scope)) return false;

        switch (token.getType().getRaw()) {
            case "char":
                if (Character.isDigit(token.getValue().getRaw().charAt(0))) return false;
                if ((token.getValue().getRaw().equals("true") || token.getValue().getRaw().equals("false"))) return false;

                break;
            case "bool":
                if (!(token.getValue().getRaw().equals("true") || token.getValue().getRaw().equals("false"))) return false;

                break;
            default:
                if (!Character.isDigit(token.getValue().getRaw().charAt(0))) return false;

                break;
        }
        return true;
    }

    public boolean varAssValidate(VarAssToken token, String scope) {
        if(!semanticTable.containsKey(token.getId().getRaw())) return false;

        return mathExpValidate(token.getValue(), semanticTable.get(token.getId().getRaw()).get(0));
    }

    private boolean mathExpValidate(MathToken token, String type) {
        if(token.getLeft() instanceof IdenToken) {
            if(!semanticTable.containsKey(token.getLeft().getRaw())) return false;
            if(!semanticTable.get(token.getLeft().getRaw()).get(0).equals(type)) return false;
        }
        if(token.getRight() instanceof IdenToken) {
            if(!semanticTable.containsKey(token.getRight().getRaw())) return false;
            if(!semanticTable.get(token.getRight().getRaw()).get(0).equals(type)) return false;
        }
        switch (type) {
            case "char":
                if (Character.isDigit(token.getValue().getRaw().charAt(0))) return false;
                if ((token.getValue().getRaw().equals("true") || token.getValue().getRaw().equals("false"))) return false;

                break;
            case "bool":
                if (!(token.getValue().getRaw().equals("true") || token.getValue().getRaw().equals("false"))) return false;

                break;
            default:
                if (!Character.isDigit(token.getValue().getRaw().charAt(0))) return false;

                break;
        }
        return true;
    }

    public boolean boolExpValidate(BoolExpToken token, String scope) {

    }

    }
