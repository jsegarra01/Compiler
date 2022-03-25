import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Parser parser = new Parser();
        try {
            parser.reader();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
