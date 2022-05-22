import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Controller {
    private SymbolTable st;
    private Parser parser;
    private TreeBuilder tb;
    private SemanticAnalyzer semanticAnalyzer;
    private TAC tac;
    private MIPS mips;

    private Optimization optimization;

    public Controller() {
        this.st = new SymbolTable();
        this.parser = new Parser("res/factorial.ap");
        this.tb = new TreeBuilder(parser, st);
        this.semanticAnalyzer = new SemanticAnalyzer();
        this.tac = new TAC();
        this.mips = new MIPS();
        this.optimization = new Optimization();
    }

    public void compile() throws IOException {
        tb.run();
        tb.printTree();
        tb.semAnalysis(semanticAnalyzer);
        tac.generate(tb);
        optimization.optimize();
        System.out.println("TAC file successfully created.");
        mips.read();
    }
}
