import tokens.Token;
import tokens.leaf.BoolExpToken;
import tokens.leaf.MathToken;
import tokens.leaf.VarAssToken;
import tokens.leaf.VarDecToken;
import tokens.terminal.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class TreeBuilder {
    private Parser parser;
    private SymbolTable st;
    private Stack<String> stack;
    private Token root;
    private boolean error;

    public TreeBuilder(Parser parser, SymbolTable st) {
        this.parser = parser;
        this.st = st;
        this.stack = new Stack();
        this.stack.push("$");
        this.stack.push("program");
        this.root = new Token();
        this.error = false;
    }

    public void run(){
        Object curr = null;
        Token pointerT = this.root;
        String topStack;
        while (!this.stack.empty() && !error){
            if(curr == null){
                curr = parser.read();
            }
            do {
                topStack = stack.pop();
            }while (topStack.equals(""));

            System.out.print("");

            if(st.isTerminal(topStack)){
                if(!topStack.equals("null")){
                    String tmpCmp;
                    if(curr instanceof String){
                        if(curr.equals("}")){
                            pointerT = pointerT.getParent();
                        }
                        tmpCmp = (String) curr;
                    }
                    else{
                        tmpCmp = ((Token) curr).getName() + " " + ((Token) curr).getRaw();
                        pointerT = pointerT.insert((Token) curr);
                    }
                    if (!Arrays.asList(tmpCmp.split(" ")).contains(topStack)) {
                        System.out.println("Grammar Error");
                        error = true;
                    }
                    curr = null;
                }
            }
            else if(topStack.equals("$") && curr.equals("$")){
                System.out.println("Grammar successful");
                return;
            }
            else if(topStack.equals("$") || curr.equals("$")){
                System.out.println("Grammar Error");
                error = true;
            }
            else{
                //Case top of stack is a variable
                String tmpToSplit;
                if(curr instanceof String){
                    tmpToSplit = st.getProduction(topStack, (String) curr);
                }
                else{
                    tmpToSplit = null;
                    for (String prod: ((Token) curr).getName().split(" ")) {
                        tmpToSplit = st.getProduction(topStack, ((Token) curr).getName());
                        if(tmpToSplit != null){
                            break;
                        }
                    }
                    if(tmpToSplit == null){
                        tmpToSplit = st.getProduction(topStack, ((Token) curr).getRaw());
                    }
                }
                if(tmpToSplit == null){
                    if(curr instanceof String){
                        System.out.println("Found non expected value at:" + (String) curr);
                    }
                    else{
                        System.out.println("Found non expected value at: " + ((Token) curr).getRaw());
                    }

                    error = true;
                    return;
                }
                String[] prod = tmpToSplit.split(" ");
                if(Token.genToken(topStack) != null ){
                    pointerT = pointerT.insert(Token.genToken(topStack));
                    if (pointerT == null){
                        System.out.println("Parsing Error");
                        error = true;
                        return;
                    }
                }

                for (int i = prod.length-1; i >= 0; i--) {
                    if(st.isTerminal(prod[i])){
                        stack.push(prod[i]);
                    }
                    else{
                        stack.push(st.cleanVar(prod[i]));
                    }
                }
            }
            System.out.print("");
        }
    }

    public void printTree(){
        if(error){
            return;
        }
        ArrayList<ArrayList<Token>> disp = parseTree();

        for (ArrayList<Token> curr: disp){
            for (Token tmp : curr){
                if(tmp != null){
                    System.out.print(tmp.getName() + " ");
                }
            }
            System.out.println();
        }
    }

    public void semAnalysis(SemanticAnalyzer semanticAnalyzer) {
        if(error){
            return;
        }
        ArrayList<ArrayList<Token>> disp = parseTree();

        for (ArrayList<Token> curr: disp){
            for (Token tmp : curr){
                if(tmp instanceof VarDecToken) {
                    if(!semanticAnalyzer.varDecValidate((VarDecToken) tmp, "0")) {
                        System.out.println("Semantic error");
                        return;
                    }
                }

                if(tmp instanceof VarAssToken) {
                    if(!semanticAnalyzer.varAssValidate((VarAssToken) tmp, "0")) {
                        System.out.println("Semantic error");
                        return;
                    }
                }

                if(tmp instanceof BoolExpToken) {
                    String type = "int";
                    Token aux = ((BoolExpToken) tmp).getLeft();
                    if(aux instanceof IdenToken) type = aux.getRaw();
                    if(aux instanceof LitToken) {
                        if(!Character.isDigit(aux.getRaw().charAt(0))) type = "char";
                        if(aux.getRaw().equals("true") || aux.getRaw().equals("false")) type = "bool";
                    }
                    if(!semanticAnalyzer.boolExpValidate((BoolExpToken) tmp, "0", type)) {
                        System.out.println("Semantic error");
                        return;
                    }
                }

                /*if (boolOp) {
                    if (tmp instanceof IdenToken) {
                        if (!semanticAnalyzer.getVar(aux.getRaw()).getRaw().equals(semanticAnalyzer.getVar(tmp.getRaw()).getRaw())) {
                            System.out.println("Semantic error");
                            return;
                        }
                    } else if (semanticAnalyzer.getVar(aux.getRaw()).getRaw().equals("char")) {
                        if (tmp instanceof LitToken) {
                            if (Character.isDigit(tmp.getRaw().charAt(0))) {
                                System.out.println("Semantic error"); //TODO fer que pasi amb '' aixi nomes haure de mirar si charAt(0) es '
                                return;
                            }
                            if ((tmp.getRaw().equals("true") || tmp.getRaw().equals("false"))) {
                                System.out.println("Semantic error");
                                return;
                            }
                        }
                    }
                    else if (semanticAnalyzer.getVar(aux.getRaw()).getRaw().equals("bool")) {
                        if (tmp instanceof LitToken) {
                            if (!(tmp.getRaw().equals("true") || tmp.getRaw().equals("false"))) {
                                System.out.println("Semantic error");
                                return;
                            }
                        }
                    }
                    else if (semanticAnalyzer.getVar(aux.getRaw()).getRaw().equals("int") || semanticAnalyzer.getVar(aux.getRaw()).getRaw().equals("float")) {
                        if (tmp instanceof LitToken) {
                            if (!Character.isDigit(tmp.getRaw().charAt(0))) {
                                System.out.println("Semantic error");
                                return;
                            }
                        }
                    }

                }
                else if(tmp instanceof IdenToken && !isVar && !auxCall) {
                    aux = tmp;
                    if(semanticAnalyzer.getVar(tmp.getRaw()) == null) {
                        System.out.println("Semantic error");
                        return;
                    }
                    auxCall = true;
                } else if (calling || auxCall2) {
                    auxCall2 = true;
                    calling = false;
                    if (tmp instanceof IdenToken) {
                        if (!semanticAnalyzer.getVar(aux.getRaw()).getRaw().equals(semanticAnalyzer.getVar(tmp.getRaw()).getRaw())) {
                            System.out.println("Semantic error");
                            return;
                        }
                    } else if (semanticAnalyzer.getVar(aux.getRaw()).getRaw().equals("char")) {
                        if (tmp instanceof OpToken || tmp instanceof BoolToken || tmp instanceof BoolExpToken || tmp instanceof BoolChainToken) {
                            System.out.println("Semantic error");
                            return;
                        }
                        if (tmp instanceof LitToken) {
                            if (Character.isDigit(tmp.getRaw().charAt(0))) {
                                System.out.println("Semantic error"); //TODO fer que pasi amb '' aixi nomes haure de mirar si charAt(0) es '
                                return;
                            }
                        }
                    }
                    else if (semanticAnalyzer.getVar(aux.getRaw()).getRaw().equals("bool")) {
                        if (tmp instanceof OpToken) {
                            System.out.println("Semantic error");
                            return;
                        }
                        if (tmp instanceof LitToken) {
                            if (!(tmp.getRaw().equals("true") || tmp.getRaw().equals("false"))) {
                                System.out.println("Semantic error");
                                return;
                            }
                        }
                    }
                    else if (semanticAnalyzer.getVar(aux.getRaw()).getRaw().equals("int") || semanticAnalyzer.getVar(aux.getRaw()).getRaw().equals("float")) {
                        if (tmp instanceof BoolToken || tmp instanceof BoolExpToken || tmp instanceof BoolChainToken) {
                            System.out.println("Semantic error");
                            return;
                        }
                        if (tmp instanceof LitToken) {
                            if (!Character.isDigit(tmp.getRaw().charAt(0))) {
                                System.out.println("Semantic error");
                                return;
                            }
                        }
                    }
                }
                else if (tmp instanceof MathToken && auxCall){
                    calling = true;
                } else if (auxCall){
                    if (!(tmp instanceof BoolToken)) {
                        auxCall = false;
                    } else {
                        boolOp = true;
                    }
                }
                if (isVar){
                    if(once) key = tmp.getRaw();


                    if (!(tmp instanceof LitToken) && !(tmp instanceof IdenToken)) { //TODO: fer be aquest if
                        if(!semanticAnalyzer.varCreated(key, (TypeToken) aux)) {
                            System.out.println("Variable " + key + " already declared");
                            return;
                        }
                        isVar = false;
                    } else {
                        if(tmp instanceof IdenToken  && !once) {
                            if (!semanticAnalyzer.getVar(tmp.getRaw()).getRaw().equals(aux.getRaw()))   {
                                 System.out.println("Semantic error" + tmp.getRaw() + aux.getRaw());
                                 return;
                            }
                        }
                        if(aux.getRaw().equals("bool")) {
                            if(tmp instanceof LitToken && !(tmp.getRaw().equals("true") || tmp.getRaw().equals("false"))) {
                                System.out.println("Semantic error");
                                return;
                            }
                        } else if(aux.getRaw().equals("char")){
                            if(tmp instanceof LitToken) {
                                if (Character.isDigit(tmp.getRaw().charAt(0))) {
                                    System.out.println("Semantic error");
                                    return;
                                }
                            }
                        } else {
                            if(tmp instanceof LitToken) {
                                if (!Character.isDigit(tmp.getRaw().charAt(0))) {
                                    System.out.println("Semantic error");
                                    return;
                                }
                            }
                        }
                    }
                    once = false;
                }
                if(tmp instanceof TypeToken){
                    aux = tmp;
                    isVar = true;
                    once = true;
                }*/
            }
        }
        semanticAnalyzer.printAll();
    }

    private ArrayList<ArrayList<Token>> parseTree() {

        ArrayList<ArrayList<Token>> disp = new ArrayList<>();
        int lvl = 0;
        boolean flag = true;
        disp.add(new ArrayList<>());
        disp.get(0).add(root);
        while (flag){
            disp.add(new ArrayList<>());
            for (int i = 0; i < disp.get(lvl).size(); i++) {
                if(disp.get(lvl).get(i) != null){
                    Object obj = disp.get(lvl).get(i).getChild();
                    if (obj != null){
                        if(obj instanceof Token){
                            disp.get(lvl+1).add((Token) obj);
                        }
                        else if(obj instanceof ArrayList){
                            disp.get(lvl+1).addAll((ArrayList<Token>)obj);
                        }
                    }
                }

            }
            if(disp.get(lvl+1).size() == 0){
                flag = false;
            }
            else{
                lvl++;
            }
        }
        return disp;
    }


}
