import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class Controller {
    private SymbolTable st;
    private Parser parser;
    private TreeBuilder tb;
    private TAC tac;
    private MIPS mips;

    public Controller() {
        this.st = new SymbolTable();
        this.parser = new Parser("res/test11.ap");
        this.tb = new TreeBuilder(parser, st);
        this.tac = new TAC();
        this.mips = new MIPS();
    }

    public void compile() throws FileNotFoundException, UnsupportedEncodingException {
        tb.run();
        tb.printTree();
        tac.generate(tb);
        System.out.println("TAC file successfully created.");
        mips.read();
    }
}
