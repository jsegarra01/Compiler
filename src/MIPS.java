import tokens.terminal.BoolChainToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class MIPS {

    private int currentT = 8;
    ArrayList<String> translate;

    public void read() throws FileNotFoundException, UnsupportedEncodingException {
        String lineRead;
        Scanner scanner = new Scanner(new File("res/finalTac.txt"));
        PrintWriter writer = new PrintWriter("res/mips.txt", "UTF-8");
        translate = new ArrayList<>();

        while (scanner.hasNextLine()) {
            lineRead = scanner.nextLine();
            System.out.println(lineRead);
            if(lineRead.contains(":=")) {
                writeDec(writer, lineRead);
            } else if(lineRead.endsWith(":")) { //Labels
                writeLabel(writer, lineRead);
            } else if(lineRead.startsWith("GOTO")) { //GOTO labels
                writeGoto(writer, lineRead);
            } else if(lineRead.startsWith("IF")) { //IF STATEMENT + GOTO
                writeIf(writer, lineRead);
            } else if(lineRead.contains("+") || lineRead.contains("-") || lineRead.contains("*") || lineRead.contains("/")) { //MATH
                writeMath(writer, lineRead);
            } else if(lineRead.startsWith("call")) {
                System.out.println(lineRead);
                writeFuncCall(writer, lineRead);
            } else if(lineRead.startsWith("return")) {
                writeFuncReturn(writer, lineRead);
            } else if(lineRead.contains(" = ")){ //Var assignation
                writeAssign(writer, lineRead);
            }
        }
        writer.close();
    }

    public void writeDec(PrintWriter writer, String lineRead) {
        String[] linePart;
        linePart = lineRead.split(" ");
        translate.add(linePart[0]);
        String tVar = "$t" + currentT;
        translate.add(tVar);
        currentT--;
        writer.println("#" + linePart[0] + " --> " + tVar);
        if(!linePart[2].equals("0")) {
            if(isNumeric(linePart[2])) {
                writer.println("li " + tVar + ", " + linePart[2]);
            }
            else {
                writer.println("li " + tVar + ", '" + linePart[2] + "'");
            }
        }
    }

    public void writeLabel(PrintWriter writer, String lineRead) {
        writer.println(lineRead);
    }

    public void writeGoto(PrintWriter writer, String lineRead) {
        String[] linePart = lineRead.split(" ");
        if(linePart[0].equals("GOTO")) {
            writer.println("j " + linePart[1]);
        }
    }

    public void writeFuncCall(PrintWriter writer, String lineRead) {
        String[] linePart = lineRead.split(" ");
        writer.println("jal " + linePart[1]);
    }

    public void writeFuncReturn(PrintWriter writer, String lineRead) {
        String[] linePart = lineRead.split(" ");
        lineRead = "$v0 = " + linePart[1];
        writeAssign(writer, lineRead);
        writer.println("jr $ra");
    }

    public void writeIf(PrintWriter writer, String lineRead) {
        String[] linePart = lineRead.split(" ");
        if(!linePart[2].startsWith("$")) {
            linePart[2] = translateToVar(writer, linePart[2]);
        }
        if(!linePart[4].startsWith("$")) {
            linePart[4] = translateToVar(writer, linePart[4]);
        }
        switch (linePart[3]) {
            case "<" -> writer.println("blt " + linePart[2] + ", " + linePart[4] + ", " + linePart[7]);
            case "==" -> writer.println("beq " + linePart[2] + ", " + linePart[4] + ", " + linePart[7]);
            case ">" -> writer.println("bgt " + linePart[2] + ", " + linePart[4] + ", " + linePart[7]);
            case "!=" -> writer.println("bne " + linePart[2] + ", " + linePart[4] + ", " + linePart[7]);
            case "<=" -> writer.println("ble " + linePart[2] + ", " + linePart[4] + ", " + linePart[7]);
            default -> writer.println("bge " + linePart[2] + ", " + linePart[4] + ", " + linePart[7]);
        }
    }

    public void writeMath(PrintWriter writer, String lineRead) {
        String[] linePart = lineRead.split(" ");
        String result = linePart[0];
        String var1 = linePart[2];

        if(linePart[0].startsWith("$")) {
            result = linePart[0];
        }
        else {
            for (int i = 0; i < translate.size(); i++) {
                if (translate.get(i).equals(linePart[0])) {
                    i++;
                    result = translate.get(i);
                }
                i++;
            }
        }

        if(linePart[2].startsWith("$")) {
            var1 = linePart[2];
        } else if(isNumeric(linePart[2])) {
            writer.println("li $t9, " + linePart[2]);
            var1 = "$t9";
        } else {
            for (int i = 0; i < translate.size(); i++) {
                if (translate.get(i).equals(linePart[2])) {
                    i++;
                    var1 = translate.get(i);
                }
                i++;
            }
        }
        String var2 = linePart[4];
        if(linePart[4].startsWith("$")) {
            var2 = linePart[4];
        } else if(isNumeric(linePart[4])) {
            writer.println("li $t9, " + linePart[4]);
            var2 = "$t9";
        } else {
            for (int i = 0; i < translate.size(); i++) {
                if (translate.get(i).equals(linePart[4])) {
                    i++;
                    var2 = translate.get(i);
                }
                i++;
            }
        }
        switch (linePart[3]) {
            case "+" -> writer.println("add " + result + ", " + var1 + ", " + var2);
            case "-" -> writer.println("sub " + result + ", " + var1 + ", " + var2);
            case "*" -> writer.println("mul " + result + ", " + var1 + ", " + var2);
            default -> writer.println("div " + result + ", " + var1 + ", " + var2);
        }
    }

    public void writeAssign(PrintWriter writer, String lineRead) {
        String[] linePart = lineRead.split(" ");
        String translate0 = linePart[0];
        String translate2 = linePart[2];
        Boolean translated2 = false;
        Boolean translated0 = false;
        if (linePart[2].startsWith("$")) {
            for (int i = 0; i < translate.size(); i++) {
                if (translate.get(i).equals(linePart[0])) {
                    i++;
                    writer.println("move " + translate.get(i) + ", " + linePart[2]);
                }
                i++;
            }
        } else if (isNumeric(linePart[2])) {
            if (linePart[0].startsWith("$")) {
                writer.println("li " + linePart[0] + ", " + linePart[2]);
            } else {
                for (int i = 0; i < translate.size(); i++) {
                    if (translate.get(i).equals(linePart[0])) {
                        i++;
                        writer.println("li " + translate.get(i) + ", " + linePart[2]);
                    }
                    i++;
                }
            }
        } else {
            for (int i = 0; i < translate.size(); i++) {
                if (translate.get(i).equals(linePart[0])) {
                    translate0 = translate.get(i+1);
                    translated0 = true;
                }
                else if(translate.get(i).equals(linePart[2])) {
                    translate2 = translate.get(i+1);
                    translated2 = true;
                }
                i++;
            }
            if(translated2) {
                writer.println("move " + translate0 + ", " + translate2);
            }
            else if(translated0) {
                writer.println("li " + translate0 + ", '" + translate2 + "'");
            }

        }
    }

    public String translateToVar(PrintWriter writer, String rawVar){
        if(isNumeric(rawVar)) {
            writer.println("li $t9, " + rawVar);
            return "$t9";
        }
        else {
            for (int i = 0; i < translate.size(); i++) {
                if (translate.get(i).equals(rawVar)) {
                    i++;
                    return translate.get(i);
                }
                i++;
            }
            return "ERROR TRANSLATING";
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int check = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
