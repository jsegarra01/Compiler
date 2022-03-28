
public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser("res/test2.ap");
        Object obj;
        do{
            obj = parser.read();
        } while (obj != null);
    }
}
