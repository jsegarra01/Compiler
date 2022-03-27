public class Main {
    public static void main(String[] args) {
        SymbolTable st = new SymbolTable();
        Parser parser = new Parser("res/test1.ap");
        TreeBuilder tb = new TreeBuilder(parser, st);
        tb.run();
        tb.printTree();
    }
}
