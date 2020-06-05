import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File dataFile = new File("C:\\_bblProjects\\fontToSupport\\src\\main\\resources\\_BibleTEXT.txt");
        PrintWriter writer = new PrintWriter("C:\\_bblProjects\\fontToSupport\\src\\main\\resources\\_BibleTEXT_MAKEDONCKI.txt", "UTF-8");
        Scanner fileReader = new Scanner(dataFile);

        while (fileReader.hasNext()) {
            String line = fileReader.nextLine();
            writer.println(convertLine(line));
        }
        writer.close();
    }

    private static String convertLine(String line) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : line.toCharArray()) {
            switch (c) {
                case '@':
                    stringBuilder.append("Ж");
                    break;
                case 'A':
                    stringBuilder.append("А");
                    break;
                case 'B':
                    stringBuilder.append("Б");
                    break;
                case 'C':
                    stringBuilder.append("Ц");
                    break;
                case 'D':
                    stringBuilder.append("Д");
                    break;
                case 'E':
                    stringBuilder.append("Е");
                    break;
                case 'F':
                    stringBuilder.append("Ф");
                    break;
                case 'G':
                    stringBuilder.append("Г");
                    break;
                case 'H':
                    stringBuilder.append("Х");
                    break;
                case 'I':
                    stringBuilder.append("И");
                    break;
                case 'J':
                    stringBuilder.append("Ј");
                    break;
                case 'K':
                    stringBuilder.append("К");
                    break;
                case 'L':
                    stringBuilder.append("Л");
                    break;
                case 'M':
                    stringBuilder.append("М");
                    break;
                case 'N':
                    stringBuilder.append("Н");
                    break;
                case 'O':
                    stringBuilder.append("О");
                    break;
                case 'P':
                    stringBuilder.append("П");
                    break;
                case 'Q':
                    stringBuilder.append("Љ");
                    break;
                case 'R':
                    stringBuilder.append("Р");
                    break;
                case 'S':
                    stringBuilder.append("С");
                    break;
                case 'T':
                    stringBuilder.append("Т");
                    break;
                case 'U':
                    stringBuilder.append("У");
                    break;
                case 'V':
                    stringBuilder.append("В");
                    break;
                case 'W':
                    stringBuilder.append("Њ");
                    break;
                case 'X':
                    stringBuilder.append("Џ");
                    break;
                case 'Y':
                    stringBuilder.append("Ѕ");
                    break;
                case 'Z':
                    stringBuilder.append("З");
                    break;
                case '[':
                    stringBuilder.append("Ш");
                    break;
                case '\\':
                    stringBuilder.append("Ѓ");
                    break;
                case ']':
                    stringBuilder.append("Ќ");
                    break;
                case '^':
                    stringBuilder.append("Ч");
                    break;
                case '`':
                    stringBuilder.append("ж");
                    break;
                case 'a':
                    stringBuilder.append("а");
                    break;
                case 'b':
                    stringBuilder.append("б");
                    break;
                case 'c':
                    stringBuilder.append("ц");
                    break;
                case 'd':
                    stringBuilder.append("д");
                    break;
                case 'e':
                    stringBuilder.append("е");
                    break;
                case 'f':
                    stringBuilder.append("ф");
                    break;
                case 'g':
                    stringBuilder.append("г");
                    break;
                case 'h':
                    stringBuilder.append("х");
                    break;
                case 'i':
                    stringBuilder.append("и");
                    break;
                case 'j':
                    stringBuilder.append("ј");
                    break;
                case 'k':
                    stringBuilder.append("к");
                    break;
                case 'l':
                    stringBuilder.append("л");
                    break;
                case 'm':
                    stringBuilder.append("м");
                    break;
                case 'n':
                    stringBuilder.append("н");
                    break;
                case 'o':
                    stringBuilder.append("о");
                    break;
                case 'p':
                    stringBuilder.append("п");
                    break;
                case 'q':
                    stringBuilder.append("љ");
                    break;
                case 'r':
                    stringBuilder.append("р");
                    break;
                case 's':
                    stringBuilder.append("с");
                    break;
                case 't':
                    stringBuilder.append("т");
                    break;
                case 'u':
                    stringBuilder.append("у");
                    break;
                case 'v':
                    stringBuilder.append("в");
                    break;
                case 'w':
                    stringBuilder.append("њ");
                    break;
                case 'x':
                    stringBuilder.append("џ");
                    break;
                case 'y':
                    stringBuilder.append("ѕ");
                    break;
                case 'z':
                    stringBuilder.append("з");
                    break;
                case '{':
                    stringBuilder.append("ш");
                    break;
                case '|':
                    stringBuilder.append("ѓ");
                    break;
                case '}':
                    stringBuilder.append("ќ");
                    break;
                case '~':
                    stringBuilder.append("ч");
                    break;
                case '$':
                    stringBuilder.append("ѝ");
                    break;
                case '#':
                    stringBuilder.append("ѐ");
                    break;
                default:
                    stringBuilder.append(c);
            }
        }

        return stringBuilder.toString();
    }


}
