import tokens.FuncToken;
import tokens.Token;
import tokens.leaf.*;
import tokens.terminal.BoolToken;
import tokens.terminal.IdenToken;
import tokens.terminal.LitToken;
import tokens.terminal.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class SemanticAnalyzer {
    private HashMap<String, ArrayList<String>> semanticTable;
    private HashMap<String, FuncToken> functionsTable;


    public SemanticAnalyzer() {
        semanticTable = new HashMap<>();
        functionsTable = new HashMap<>();
    }

    public boolean varCreated(String type, String id, String scope) {

        if (semanticTable.containsKey(id) && semanticTable.get(id).get(1).equals(scope)) return false;

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
        if (!varCreated(token.getType().getRaw(), token.getIdentifier().getRaw(), scope)) return false;

        if(token.getValue() == null) {
            return true;
        }

        switch (token.getType().getRaw()) {
            case "char":
                if (Character.isDigit(token.getValue().getRaw().charAt(0))) return false;
                if ((token.getValue().getRaw().equals("true") || token.getValue().getRaw().equals("false")))
                    return false;

                break;
            case "bool":
                if (!(token.getValue().getRaw().equals("true") || token.getValue().getRaw().equals("false")))
                    return false;

                break;
            default:
                if (!Character.isDigit(token.getValue().getRaw().charAt(0))) return false;

                break;
        }
        return true;
    }

    public boolean varAssValidate(VarAssToken token, String scope) {
        if (!semanticTable.containsKey(token.getId().getRaw())) return false;
        if( !semanticTable.get(token.getId().getRaw()).get(1).equals(scope)) return false;

        if(token.getValue() instanceof MathToken) return mathExpValidate((MathToken) token.getValue(), semanticTable.get(token.getId().getRaw()).get(0), scope);
        if(token.getValue() instanceof FCallToken) return funcCallValidate((FCallToken) token.getValue(), semanticTable.get(token.getId().getRaw()).get(0), scope);
        return true;
    }

    private boolean mathExpValidate(MathToken token, String type, String scope) {

        switch (type) {
            case "char":
                if (token.getLeft() instanceof IdenToken) {
                    if (!semanticTable.containsKey(token.getLeft().getRaw())) return false;
                    if (!semanticTable.get(token.getLeft().getRaw()).get(0).equals(type)) return false;
                    if (!semanticTable.get(token.getLeft().getRaw()).get(1).equals(scope)) return false;
                } else {
                    if (!(token.getLeft() instanceof LitToken)) return false;
                    if (Character.isDigit(token.getLeft().getRaw().charAt(0))) return false;
                    if ((token.getLeft().getRaw().equals("true") || token.getLeft().getRaw().equals("false")))
                        return false;
                }
                if (token.getOp() != null) return false;
                if (token.getRight() != null) return false;

                break;
            case "bool":
                if (token.getLeft() instanceof IdenToken) {
                    if (!semanticTable.containsKey(token.getLeft().getRaw())) return false;
                    if (!semanticTable.get(token.getLeft().getRaw()).get(0).equals(type)) return false;
                    if (!semanticTable.get(token.getLeft().getRaw()).get(1).equals(scope)) return false;
                } else {
                    if (!(token.getLeft() instanceof LitToken)) return false;
                    if (!(token.getLeft().getRaw().equals("true") || token.getLeft().getRaw().equals("false")))
                        return false;
                }
                if (token.getOp() != null) return false;
                if (token.getRight() != null) return false;

                break;
            default:
                if (token.getLeft() instanceof IdenToken) {
                    if (!semanticTable.containsKey(token.getLeft().getRaw())) return false;
                    if (!semanticTable.get(token.getLeft().getRaw()).get(0).equals(type)) return false;
                    if (!semanticTable.get(token.getLeft().getRaw()).get(1).equals(scope)) return false;
                } else {
                    if (!Character.isDigit(token.getLeft().getRaw().charAt(0))) return false;

                }

                if (token.getRight() instanceof MathToken) {
                    return mathExpValidate((MathToken) token.getRight(), type, scope);
                } else {
                    if (token.getRight() instanceof IdenToken) {
                        if (!semanticTable.containsKey(token.getRight().getRaw())) return false;
                        if (!semanticTable.get(token.getRight().getRaw()).get(0).equals(type)) return false;
                        if (!semanticTable.get(token.getLeft().getRaw()).get(1).equals(scope)) return false;
                    } else {
                        if(token.getRight() == null) {
                            break;
                        }
                        if (!Character.isDigit(token.getRight().getRaw().charAt(0))) return false;
                    }
                }

                break;
        }
        return true;
    }

    public boolean boolExpValidate(BoolExpToken token, String scope, String type) {

        switch (type) {
            case "char":
                if (token.getLeft() instanceof BoolExpToken) {
                    if(!boolExpValidate((BoolExpToken) token.getLeft(), scope, type)) return false;
                } else if (token.getLeft() instanceof IdenToken) {
                    if (!semanticTable.containsKey(token.getLeft().getRaw())) return false;
                    if (!semanticTable.get(token.getLeft().getRaw()).get(0).equals(type)) return false;
                } else {
                    if (!(token.getLeft() instanceof LitToken)) return false;
                    if (Character.isDigit(token.getLeft().getRaw().charAt(0))) return false;
                    if ((token.getLeft().getRaw().equals("true") || token.getLeft().getRaw().equals("false")))
                        return false;
                }

                if(token.getRight() instanceof BoolExpToken){
                    if(!boolExpValidate((BoolExpToken) token.getRight(), scope, type)) return false;
                }
                else if(token.getRight() != null){
                    if (token.getRight() instanceof IdenToken) {
                        if (!semanticTable.containsKey(token.getRight().getRaw())) return false;
                        if (!semanticTable.get(token.getRight().getRaw()).get(0).equals(type)) return false;
                    } else {
                        if (!(token.getRight() instanceof LitToken)) return false;
                        if (Character.isDigit(token.getRight().getRaw().charAt(0))) return false;
                        if ((token.getRight().getRaw().equals("true") || token.getRight().getRaw().equals("false")))
                            return false;
                    }
                }


                break;
            case "bool":
                if (token.getLeft() instanceof BoolExpToken) {
                    if(!boolExpValidate((BoolExpToken) token.getLeft(), scope, type)) return false;
                } else if (token.getLeft() instanceof IdenToken) {
                    if (!semanticTable.containsKey(token.getLeft().getRaw())) return false;
                    if (!semanticTable.get(token.getLeft().getRaw()).get(0).equals(type)) return false;
                } else {
                    if (!(token.getLeft() instanceof LitToken)) return false;
                    if (!(token.getLeft().getRaw().equals("true") || token.getLeft().getRaw().equals("false"))) return false;
                }

                if(token.getRight() instanceof BoolExpToken) {
                    if(!boolExpValidate((BoolExpToken) token.getRight(), scope, type)) return false;
                }
                else if(token.getRight() != null){
                    if (token.getRight() instanceof IdenToken) {
                        if (!semanticTable.containsKey(token.getLeft().getRaw())) return false;
                        if (!semanticTable.get(token.getLeft().getRaw()).get(0).equals(type)) return false;
                    } else {
                        if (!(token.getRight() instanceof LitToken)) return false;
                        if (!(token.getRight().getRaw().equals("true") || token.getRight().getRaw().equals("false"))) return false;
                    }
                }


                break;
            default:
                if (token.getLeft() instanceof BoolExpToken) {
                    if(!boolExpValidate((BoolExpToken) token.getLeft(), scope, type)) return false;
                } else if (token.getLeft() instanceof IdenToken) {
                    if (!semanticTable.containsKey(token.getLeft().getRaw())) return false;
                    if (!semanticTable.get(token.getLeft().getRaw()).get(0).equals("int") && !semanticTable.get(token.getLeft().getRaw()).get(0).equals("float")) return false;
                } else {
                    if (!Character.isDigit(token.getLeft().getRaw().charAt(0))) return false;
                }

                if (token.getRight() instanceof BoolExpToken) {
                    if(!boolExpValidate((BoolExpToken) token.getRight(), scope, type)) return false;
                } else {
                    if (token.getRight() instanceof IdenToken) {
                        if (!semanticTable.containsKey(token.getRight().getRaw())) return false;
                        if (!semanticTable.get(token.getRight().getRaw()).get(0).equals("int") && !semanticTable.get(token.getRight().getRaw()).get(0).equals("float")) return false;
                    } else {
                        if (!Character.isDigit(token.getRight().getRaw().charAt(0)) && token.getRight() != null) return false;
                    }
                }
                break;
        }
        return true;
    }

    public boolean funcDecValidate(FuncToken token, String scope) {
        if (functionsTable.containsKey(token.getId().getRaw())) return false;
        for (ArgsToken argsToken: token.getArgs()) {
            if (semanticTable.containsKey(token.getId().getRaw()) && semanticTable.get(token.getId().getRaw()).get(1).equals(scope)) return false;

            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(argsToken.getType().getRaw());
            tmp.add(scope);
            semanticTable.put(argsToken.getId().getRaw(), tmp);
        }

        functionsTable.put(token.getId().getRaw(), token);
        return true;
    }

    private boolean funcCallValidate(FCallToken token, String type, String scope) {
        if(!functionsTable.containsKey(token.getId().getRaw())) return false;
        if(!functionsTable.get(token.getId().getRaw()).getType().getRaw().equals(type)) return false;
        int i = 0;

        for (ArgsToken argsToken: functionsTable.get(token.getId().getRaw()).getArgs()) {
            Token auxToken = token.getArgs().get(i);
            if(auxToken instanceof IdenToken) {
                if(!semanticTable.get(auxToken.getRaw()).get(0).equals(argsToken.getType().getRaw())) return false;
            }
            else if(auxToken instanceof LitToken){
                switch (argsToken.getType().getRaw()) {
                    case "char":
                        if (Character.isDigit(auxToken.getRaw().charAt(0))) return false;
                        if ((auxToken.getRaw().equals("true") || auxToken.getRaw().equals("false")))
                            return false;

                        break;
                    case "bool":
                        if (!(auxToken.getRaw().equals("true") || auxToken.getRaw().equals("false")))
                            return false;

                        break;
                    default:
                        if (!Character.isDigit(auxToken.getRaw().charAt(0))) return false;

                        break;
                }
            }

            i++;
        }

        return true;
    }

}
