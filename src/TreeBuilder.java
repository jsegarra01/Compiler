import tokens.Token;

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
                        tmpCmp = ((Token) curr).getName();
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
                String tmpToSplit = st.getProduction(topStack, (String) curr);
                if(tmpToSplit == null){
                    System.out.println("Found non expected value at:" + (String) curr);
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
        for (ArrayList<Token> curr: disp){
            for (Token tmp : curr){
                if(tmp != null){
                    System.out.print(tmp.getName() + " ");
                }
            }
            System.out.println();
        }
    }


}
