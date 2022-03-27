import tokens.Token;

import java.util.Stack;

public class TreeBuilder {
    private Parser parser;
    private SymbolTable st;
    private Stack<String> stack;
    private Token root;

    public TreeBuilder(Parser parser, SymbolTable st) {
        this.parser = parser;
        this.st = st;
        this.stack = new Stack();
        this.stack.push("$");
        this.stack.push("program");
        this.root = new Token();
    }

    public void run(){
        Object curr = null;
        Token pointerT = this.root;
        String topStack;
        while (!this.stack.empty()){
            if(curr == null){
                curr = parser.read();
            }
            do {
                topStack = stack.pop();
            }while (topStack.equals(""));

            if(st.isTerminal(topStack)){
                if(!topStack.equals("null")){
                    String tmpCmp;
                    if(curr instanceof String){
                        tmpCmp = (String) curr;
                    }
                    else{
                        tmpCmp = ((Token) curr).getName();
                        pointerT = pointerT.insert((Token) curr);
                    }
                    if (!tmpCmp.equals(topStack)) {
                        System.out.println("Grammar Error");
                    }
                    curr = null;
                }
            }
            else if(topStack.equals("$") && curr.equals("$")){
                System.out.println("Grammar successful");
                return;
            }
            else if(topStack.equals("$") || curr.equals("$")){
                System.out.println("1Grammar Error");
            }
            else{
                //Case top of stack is a variable
                String tmpToSplit = st.getProduction(topStack, (String) curr);
                if(tmpToSplit == null){
                    System.out.println("2 Grammar Error -> topStack:" + topStack + " curr: " + (String) curr);
                    return;
                }
                String[] prod = tmpToSplit.split(" ");
                if(Token.genToken(topStack) != null && !tmpToSplit.equals("")){
                    pointerT = pointerT.insert(Token.genToken(topStack));
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

}
