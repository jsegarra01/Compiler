import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class TAC {
    public void generate(TreeBuilder treeBuilder) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("res/tac.txt", "UTF-8");
        treeBuilder.getRoot().getTac(writer);
        writer.close();
    }
}
