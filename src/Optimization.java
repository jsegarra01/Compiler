import java.io.*;
import java.util.Objects;

public class Optimization {
    public void optimize() throws IOException {
        PrintWriter writer = new PrintWriter("res/finalTac.txt", "UTF-8");
        BufferedReader reader = new BufferedReader(new FileReader("res/tac.txt"));

        String tempAux;
        String label1;
        String[] linePart;
        String[] linePart2;
        String lineRead = reader.readLine();
        while(lineRead != null) {
            //OPTIMIZE REGISTERS?
            if(lineRead.startsWith("$")){
                tempAux = lineRead;
                linePart = tempAux.split(" ");
                lineRead = reader.readLine();
                linePart2 = lineRead.split(" ");
                if(linePart2.length == 3) {
                    if(linePart2[2].equals(linePart[0])){
                        String finalAssign = linePart2[0];
                        for (int i = 1; i < linePart.length; i++) {
                            finalAssign = finalAssign.concat(" " + linePart[i]);
                        }
                        writer.println(finalAssign);
                        lineRead = reader.readLine();
                    }
                }
                else{
                    writer.println(tempAux);
                    writer.println(lineRead);
                    lineRead = reader.readLine();
                }

            }

            //IF STATEMENTS > || ==
            else if(lineRead.startsWith("IF")) {
                tempAux = lineRead;
                linePart = lineRead.split(" ");
                label1 = linePart[7];
                lineRead = reader.readLine();
                if(lineRead.equals("||")) {
                    lineRead = reader.readLine();
                    linePart2 = lineRead.split(" ");
                    if(linePart2[2].equals(linePart[2]) && linePart2[4].equals(linePart[4])) {
                        if(linePart[3].equals(">=") && linePart2[3].equals("!=") || linePart[3].equals("!=") && linePart2[3].equals(">=")){
                            writer.println("IF ( " + linePart[2] + " > " + linePart[4] + " ) GOTO " + label1);
                            lineRead = reader.readLine();
                        }
                        else if(linePart[3].equals("<=") && linePart2[3].equals("!=") || linePart[3].equals("!=") && linePart2[3].equals("<=")){
                            writer.println("IF ( " + linePart[2] + " > " + linePart[4] + " ) GOTO " + label1);
                            lineRead = reader.readLine();
                        }
                    }
                }
                else {
                    writer.println(tempAux);
                    writer.println(lineRead);
                    lineRead = reader.readLine();
                }
            }
            else {
                writer.println(lineRead);
                lineRead = reader.readLine();
            }
        }
        writer.close();
    }
}
