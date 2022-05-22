import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Controller {
    private SymbolTable st;
    private Parser parser;
    private TreeBuilder tb;
    private TAC tac;
    private MIPS mips;

    private Optimization optimization;

    public Controller() {
        this.st = new SymbolTable();
        this.parser = new Parser("res/factorial.ap");
        this.tb = new TreeBuilder(parser, st);
        this.tac = new TAC();
        this.mips = new MIPS();
        this.optimization = new Optimization();
    }

    public void compile() throws IOException {
        tb.run();
        tb.printTree();
        tac.generate(tb);
        optimization.optimize();
        System.out.println("TAC file successfully created.");
        mips.read();
    }
}
