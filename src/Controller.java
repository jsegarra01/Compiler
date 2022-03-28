public class Controller {
    private SymbolTable st;
    private Parser parser;
    private TreeBuilder tb;
    public Controller() {
        this.st = new SymbolTable();
        this.parser = new Parser("res/test3.ap");
        this.tb = new TreeBuilder(parser, st);
    }

    public void compile(){
        tb.run();
        tb.printTree();
    }
}
