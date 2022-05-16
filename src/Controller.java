import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Controller {
    private SymbolTable st;
    private Parser parser;
    private TreeBuilder tb;
    private SemanticAnalyzer semanticAnalyzer;
    private TAC tac;

    public Controller() {
        this.st = new SymbolTable();
        this.parser = new Parser("res/test11.ap");
        this.tb = new TreeBuilder(parser, st);
        this.semanticAnalyzer = new SemanticAnalyzer();
        this.tac = new TAC();
    }

    public void compile() throws FileNotFoundException, UnsupportedEncodingException {
        tb.run();
        tb.printTree();
        tac.generate(tb);
        System.out.println("TAC file successfully created.");
    }
}
