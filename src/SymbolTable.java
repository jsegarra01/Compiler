import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class SymbolTable {
    private static final String PATH = "res/grammar.txt";
    private HashMap<String, ArrayList<String>> grammar;
    private ArrayList<String> terminals;
    private HashMap<String, ArrayList<String>> firsts;
    private HashMap<String, ArrayList<String>> follows;
    private HashMap<String, HashMap<String, String>> parseTable;

    public SymbolTable() {
        //Use: https://mikedevice.github.io/first-follow/
        //To validate results
        grammar = new HashMap<>();
        try {
            Scanner sc = new Scanner(new File(PATH));
            //Reading first line which contains all valid terminals
            this.terminals = new ArrayList<>();
            String[] termData = sc.nextLine().split(" ");
            this.terminals.addAll(Arrays.asList(termData));

            ArrayList<String> tmpVars = new ArrayList<>();
            //Loads the grammar
            while (sc.hasNextLine()){
                String curr = sc.nextLine();
                String var = curr.split(" ::= ")[0];
                tmpVars.add(var);
                String[] productions = curr.split(" ::= ")[1].split(" \\| ");
                grammar.put(var, new ArrayList<>());
                for (int i = 0; i < productions.length; i++) {
                    grammar.get(var).add(productions[i]);
                }
            }

            //Calculate the firsts
            firsts = new HashMap<>();
            for (int i = 0; i < tmpVars.size(); i++) {
                if(firsts.containsKey(tmpVars.get(i))){
                    nonRepetitionJoin(firsts.get(tmpVars.get(i)), findFirsts(tmpVars.get(i)));
                }
                else{
                    firsts.put(tmpVars.get(i), findFirsts(tmpVars.get(i)));
                }
            }

            //Calculate the follows
            follows = new HashMap<>();
            follows.put(tmpVars.get(0), new ArrayList<>());
            follows.get(tmpVars.get(0)).add("$");
            for (int i = 1; i < tmpVars.size(); i++) {
                follows.put(tmpVars.get(i), findFollow(tmpVars.get(i), tmpVars));
            }

            parseTable = new HashMap<>();
            for (int i = 0; i < tmpVars.size(); i++) {
                String currVar = tmpVars.get(i);
                parseTable.put(currVar, new HashMap<>());
                for (String production : grammar.get(currVar)){
                    String[] chunks = production.split(" ");
                    if(isVariable(chunks[0])){
                        //For null firsts of a production we much check what comes after
                        ArrayList<String> tmpArr = new ArrayList<>(firsts.get(cleanVar(chunks[0])));
                        if(tmpArr.contains("null")){
                            if(chunks.length > 1){
                                for (int j = 1; j < chunks.length && tmpArr.contains("null"); j++) {
                                    tmpArr.remove("null");
                                    if (isTerminal(chunks[j])){
                                        tmpArr.add(chunks[j]);
                                    }
                                    else{
                                        tmpArr.addAll(findFirsts(cleanVar(chunks[j])));
                                    }
                                }
                            }
                        }
                        //End
                        for (String term : tmpArr){
                            if(term.equals("null")){
                                for (String followed : follows.get(currVar)){
                                    parseTable.get(currVar).put(followed, production);
                                }
                            }
                            else {
                                parseTable.get(currVar).put(term, production);
                            }
                        }
                    }
                    else if(chunks[0].equals("null")){
                        for (String followed : follows.get(currVar)){
                            parseTable.get(currVar).put(followed, "");
                        }
                    }
                    else{
                        parseTable.get(currVar).put(chunks[0], production);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> findFirsts(String in){
        if(firsts.containsKey(in)){
            return firsts.get(in);
        }
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < grammar.get(in).size(); i++) {
            String first = grammar.get(in).get(i).split(" ")[0];
            if(isVariable(first)){
                first = cleanVar(first);
                if(!firsts.containsKey(first)){
                    firsts.put(first, findFirsts(first));
                }
                //Null in first set
                ArrayList<String> tmpArr = new ArrayList<>(firsts.get(first));
                if(tmpArr.contains("null")){
                    String[] prods = grammar.get(in).get(i).split(" ");
                    if(prods.length > 1){
                        for (int j = 1; j < prods.length && tmpArr.contains("null"); j++) {
                            tmpArr.remove("null");
                            if (isTerminal(prods[j])){
                                tmpArr.add(prods[j]);
                            }
                            else{
                                tmpArr.addAll(findFirsts(cleanVar(prods[j])));
                            }
                        }
                    }
                }
                nonRepetitionJoin(out, tmpArr);

            }
            else{
                if(!out.contains(first)){
                    out.add(first);
                }
            }
        }
        return out;
    }

    private ArrayList<String> findFollow(String in, ArrayList<String> vars){
        ArrayList<String> tmp = new ArrayList<>();
        return findFollow(in, vars, tmp);
    }

    private ArrayList<String> findFollow(String in, ArrayList<String> vars, ArrayList<String> visited){
        ArrayList<String> out = new ArrayList<>();
        visited.add(in);
        String trg = dirtyVar(in);
        for (String currVar: vars) {
            if (!currVar.equals(in) && !visited.contains(currVar)) {
                for (String currProd : grammar.get(currVar)) {
                    if (currProd.contains(trg)) {
                        String[] seq = currProd.split(" ");
                        for (int i = 0; i < seq.length; i++) {
                            if (seq[i].equals(trg)) {
                                boolean placed = false;
                                while (!placed) {
                                    if (i + 1 >= seq.length) {
                                        if (follows.containsKey(currVar)) {
                                            nonRepetitionJoin(out, follows.get(currVar));
                                        } else {
                                            follows.put(currVar, findFollow(currVar, vars, visited));
                                            nonRepetitionJoin(out, follows.get(currVar));
                                        }
                                        placed = true;
                                    } else {
                                        String next = seq[i + 1];
                                        if (isVariable(next)) {
                                            //get the firsts and add them and in case of null look for the next
                                            String cNext = cleanVar(next);
                                            ArrayList<String> possibles = firsts.get(cNext);
                                            if (possibles.contains("null")) {
                                                //special recursion case
                                                nonRepetitionJoin(out, possibles);
                                                out.remove("null");
                                                i++;
                                            } else {
                                                nonRepetitionJoin(out, possibles);
                                                placed = true;
                                            }
                                        } else {
                                            out.add(next);
                                            placed = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return out;
    }

    private void nonRepetitionJoin(ArrayList<String> trg, ArrayList<String> adders){
        for (String curr: adders) {
            if(!trg.contains(curr)){
                trg.add(curr);
            }
        }
    }

    private boolean isVariable(String in){
        return in.contains("<") && in.contains(">");
    }

    public String cleanVar(String in){
        return in.replace("<", "").replace(">", "");
    }

    private String dirtyVar(String in){
        return "<" + in + ">";
    }

    public boolean isTerminal(String in){
        return this.terminals.contains(in);
    }

    public String getProduction(String var, String terminal){
        if(parseTable.containsKey(var)){
            return parseTable.get(var).getOrDefault(terminal, null);
        }
        else{
            return null;
        }
    }
}
