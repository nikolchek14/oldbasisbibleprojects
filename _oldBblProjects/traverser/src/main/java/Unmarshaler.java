import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

public class Unmarshaler {
    public static void main(String[] args) {

        try {
            File file = new File("src/main/resources/BblXMLMakedonckiKol1b.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(BBL.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BBL bbl = (BBL) jaxbUnmarshaller.unmarshal(file);
            Map<String,Integer> l = new HashMap<>();
            for (BBLPage p : bbl.getPage()) {
                if (p.getText() != null) {
                    //p.getText().removeIf(b -> b.getLeft() < 360);
                    Collections.sort(p.getText(), new Comparator<BBLText>() {
                        @Override
                        public int compare(BBLText lhs, BBLText rhs) {
                            return lhs.getTop() > rhs.getTop() ? 1 : (lhs.getTop() < rhs.getTop()) ? -1 : 0;
                        }
                    });
                    int diffToPrev = 0;
                    for (BBLText t : p.getText()) {
                        t.setDiff(t.getTop() - diffToPrev);
                        diffToPrev = t.getTop();
                        t.setValue(convertLine(t.getValue()));
                    }
                    List<BBLText> arr = new ArrayList<>();

                    /*for (BBLText t : p.getText()) {
                        if (StringUtils.containsAny(t.getValue(),'0','1','2','3','4','5','6','7','8','9') && StringUtils.isAlphanumeric(t.getValue())) {
                            arr.add(t);
                        }
                    }*/

                    //font="16" height="18"
                    for (BBLText t : p.getText()) {
                        if(t.getHeight()<30 && !l.containsKey(t.getFont()+"h"+t.getHeight())){
                            l.put(t.getFont()+"h"+t.getHeight(),1);
                        }else if(t.getHeight()<30){
                            Integer c = l.get(t.getFont() + "h" + t.getHeight());
                            l.put(t.getFont()+"h"+t.getHeight(),++c);
                        }
                        if (((t.getFont() == 9 && t.getHeight() == 18) || (t.getFont() == 14 && t.getHeight() == 18)
                            || (t.getFont() == 23 && t.getHeight() == 11) || (t.getFont() == 19 && t.getHeight() == 14))) {
                            arr.add(t);
                        }
                    }
                    /*
                    for (BBLText t : p.getText()) {
                        if (!p.getNumber().equals("1052") && StringUtils.isAllUpperCase(StringUtils.deleteWhitespace(t.getValue()))) {
                            arr.add(t);
                        } else if (!p.getNumber().equals("1052") && StringUtils.isAllUpperCase(StringUtils.deleteWhitespace(t.getValue()).replaceAll("[0-9]",""))) {
                            arr.add(t);
                        }
                    }*/
                    /*for (BBLText t : p.getText()) {
                        if ((t.getFont() == 12 && t.getHeight() == 19 && t.getTop()==t.getDiff()) || (t.getFont() == 12 && t.getHeight() == 14 && t.getTop()==t.getDiff())) {
                            arr.add(t);
                        }
                    }*/
                    /*for (BBLText t : p.getText()) {
                        if ((t.getFont() == 25 && t.getHeight() == 22) || (t.getFont() == 11 && t.getHeight() == 32) || (t.getFont() == 12 && t.getHeight() == 19 && t.getTop()!=t.getDiff())) {
                            arr.add(t);
                        }
                    }*/
                    /*for (BBLText t : p.getText()) {
                        if (Integer.parseInt(p.getNumber()) < 9 || p.getNumber().equals("1185") || p.getNumber().equals("1186") || p.getNumber().equals("1187") || p.getNumber().equals("1188")) {
                            arr.add(t);
                        }
                    }*/
                    /*List<BBLText> toSave = new ArrayList<>();
                    boolean prevWasVoved = false;
                    boolean prevOk = false;
                    for (BBLText t : p.getText()) {
                        //font="14" height="18"
                        /*if (t.getDiff() < 16 || t.getDiff() > 17) {
                            toRemove.add(t);
                        }
                        if (p.getNumber().equals("1095") || p.getNumber().equals("1283") || p.getNumber().equals("199")) {
                        } else if (prevWasVoved) {
                            toSave.add(t);
                            prevWasVoved = false;
                            prevOk = true;
                        } else if ((t.getLeft() == 350 || t.getLeft() == 352) || (t.getLeft() == 326 && p.getNumber().equals("873"))) {
                            toSave.add(t);
                            prevWasVoved = true;
                            prevOk = true;
                        } else if (t.getDiff() == t.getTop() && p.getNumber().equals("874")) {
                            prevWasVoved = true;
                            prevOk = true;
                        } else if ((t.getDiff() == 29 || t.getDiff() == 22) && (p.getNumber().equals("919") || p.getNumber().equals("1295"))) {
                            toSave.add(t);
                            prevWasVoved = true;
                            prevOk = true;
                        } else if (((t.getDiff() == 15 && p.getNumber().equals("1444")) || t.getDiff() == 16 || t.getDiff() == 17 || t.getDiff() == 18)
                                &&((t.getFont() == 9 && t.getHeight() == 18)
                                || (t.getFont() == 23 && t.getHeight() == 11) || (t.getFont() == 23 && t.getHeight() == 14)
                                || (t.getFont() == 14 && t.getHeight() == 18)) && (t.getLeft() == 98
                                || t.getLeft() == 110 || t.getLeft() == 97 || t.getLeft() == 100 || t.getLeft() == 113 || t.getLeft() == 119))
                        {
                            toSave.add(t);
                            prevOk = true;
                        } else if (prevOk && t.getDiff() == 0) {
                            toSave.add(t);
                            prevOk = true;
                        } else {
                            prevOk = false;
                        }
                        /*if (t.getLeft() != 350 && t.getLeft() != 352) {
                            toRemove.add(t);
                        }
                       /* if ((t.getFont()!=14 || t.getHeight()!=18) && (t.getFont()!=9 || t.getHeight()!=18)
                                && (t.getFont()!=23 || t.getHeight()!=11) && (t.getFont()!=23 || t.getHeight()!=14)){
                            toRemove.add(t);
                        }
                        // 98 110 97 100 113
                        if (t.getDiff()!=0 && (t.getLeft()!=98 && t.getLeft()!=110 && t.getLeft()!=97
                                && t.getLeft()!=100 && t.getLeft()!=113)){
                            toRemove.add(t);
                        }
                    }*/
                    //p.getText().removeAll(p.getText());

                    p.getText().removeAll(arr);
                    //p.getText().addAll(arr);
                    //System.out.println(toRemove.size());
                }
            }
            System.out.println(l);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //Marshal the employees list in file
            jaxbMarshaller.marshal(bbl, new File("src/main/resources/BblXMLMakedonckiRand2.xml"));

            ObjectMapper mapper = new ObjectMapper();

            // Java objects to JSON file
            mapper.writeValue(new File("src/main/resources/BblRand2.json"), bbl);

            // Java objects to JSON string - compact-print
            String jsonString = mapper.writeValueAsString(bbl);

            //System.out.println(jsonString);

            // Java objects to JSON string - pretty-print
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bbl);

            //System.out.println(jsonInString2);


        } catch (Exception e) {
            e.printStackTrace();
        }
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
