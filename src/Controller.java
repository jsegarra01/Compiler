public class Controller {
    private SymbolTable st;
    private Parser parser;
    private TreeBuilder tb;
    private SemanticAnalyzer semanticAnalyzer;
    public Controller() {
        this.st = new SymbolTable();
        this.parser = new Parser("res/test7.ap");
        this.tb = new TreeBuilder(parser, st);
        this.semanticAnalyzer = new SemanticAnalyzer();
    }

    public void compile(){
        tb.run();
        tb.printTree();
        tb.semAnalysis(semanticAnalyzer);
    }
}
