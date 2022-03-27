import tokens.SymbolTable;

public class Main {
    public static void main(String[] args) {
        SymbolTable st = new SymbolTable();
        Parser parser = new Parser("res/test1.ap");
        Object obj;
        do{
            obj = parser.read();
        } while (obj != null);
    }
}
