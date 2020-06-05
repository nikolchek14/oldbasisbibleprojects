import biblija.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xembly.Directives;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Unpacker {
    public static final String PATH = "C:\\_bblProjects\\parser\\src\\main\\resources\\";
    public static final String PATH_XMLI = "C:\\_bblProjects\\parser\\src\\main\\resources\\xmli2\\";
    private static List<String> lista = new ArrayList<>();

    static PrintWriter pisalko;
    static Map<String, String> atributi;
    private static Directives directives;
    private static String htmlString;
    private static List<Directives> dirs;

    static Map<Integer, String> kratenki = new HashMap<>();

    static Stih xStih;
    static Glava xGlava;
    static Referenca xReferenca;
    static Kniga xKniga;
    static Voved xVoved;
    static Naslov xNaslov;
    static Futnota xFutnota;
    static Podnaslov xPodnaslov;
    static Biblija xBiblija;

    static int counter = 0;
    static int psalmcount = 0;
    static int kcounter = -1;
    static String futid;
    static Map<String, Integer> mapa = new HashMap<>();
    static int stihCounter = 0;

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        try {
            //writer = new PrintWriter(PATH + "_BibleTEXT_MAKEDONCKI.txt", "UTF-8");
            pisalko = new PrintWriter(PATH + "_Pisalko.txt", "UTF-8");
            // JSON file to Java object
            Biblija biblija = mapper.readValue(new File(PATH + "Trim1.json"), Biblija.class);

            for (Kniga k : biblija.getKnigi()) {
                kratenki.put(biblija.getKnigi().indexOf(k), k.getKratenka());
            }
            xBiblija = new Biblija();
            File htmlTemplateFile = new File(PATH + "template.html");
            htmlString = FileUtils.readFileToString(htmlTemplateFile);

            for (Kniga k : biblija.getKnigi()) {
                String knigaName = "Kniga" + (biblija.getKnigi().indexOf(k) + 1) + "_" + convertCyrilic(k.getKratenka()).replace(" ", "") + ".xhtml";

                File file = new File(PATH_XMLI + knigaName);
                //writer = new PrintWriter("C:\\_bblProjects\\traverser\\src\\main\\resources\\BblXMLOutput.txt", "UTF-8");

                DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = dBuilder.parse(file);

                if (doc.hasChildNodes()) {
                    printNote(doc.getChildNodes());
                }
                //pisalko.println(lista);
                //writer.close();
                /*for (Glava g : k.getGlavi()) {
                    for (Stih s : g.getStihovi()) {

                    }
                }*/
            }
            String knigaName = "Futnoti.xhtml";

            File file = new File(PATH_XMLI + knigaName);
            //writer = new PrintWriter("C:\\_bblProjects\\traverser\\src\\main\\resources\\BblXMLOutput.txt", "UTF-8");

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            if (doc.hasChildNodes()) {
                printNote(doc.getChildNodes());
            }


            for (Kniga k : biblija.getKnigi()) {
                int kind = biblija.getKnigi().indexOf(k);
                Kniga xk = xBiblija.getKnigi().get(kind);
                xk.setIme(k.getIme());
                if (!k.equals(xk)) {
                    pisalko.println(k);
                    pisalko.println(xk);
                    pisalko.println("***");
                }
                for (Glava g : k.getGlavi()) {
                    int gind = k.getGlavi().indexOf(g);
                    Glava xg = xk.getGlavi().get(gind);
                    if (!g.equals(xg)) {
                        pisalko.println(g);
                        pisalko.println(xg);
                        pisalko.println("***");
                    }
                    for (Stih s : g.getStihovi()) {
                        int sind = g.getStihovi().indexOf(s);
                        //System.out.println(g.getStihovi());
                        Stih xs = xg.getStihovi().get(sind);
                        xs.setReferenci(s.getReferenci());
                        stihCounter++;
                        if (!s.equals(xs)) {
                            /*pisalko.println(s);
                            pisalko.println(xs);
                            pisalko.println("***");*/
                        }
                    }
                }
            }
            System.out.println(stihCounter);
            mapper.writeValue(new File(PATH + "prototype1.json"), xBiblija);
            //bookWriter(biblija);
            pisalko.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertCyrilic(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'ѓ', 'е', 'ж', 'з', 'ѕ', 'и', 'ј', 'к', 'л', 'љ', 'м', 'н', 'њ', 'о', 'п', 'р', 'с', 'т', 'ќ', 'у', 'ф', 'х', 'ц', 'ч', 'џ', 'ш', 'А', 'Б', 'В', 'Г', 'Д', 'Ѓ', 'Е', 'Ж', 'З', 'Ѕ', 'И', 'Ј', 'К', 'Л', 'Љ', 'М', 'Н', 'Њ', 'О', 'П', 'Р', 'С', 'Т', 'Ќ', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Џ', 'Ш', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/', '-'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "]", "e", "zh", "z", "y", "i", "j", "k", "l", "q", "m", "n", "w", "o", "p", "r", "s", "t", "'", "u", "f", "h", "c", "ch", "x", "{", "A", "B", "V", "G", "D", "}", "E", "Zh", "Z", "Y", "I", "J", "K", "L", "Q", "M", "N", "W", "O", "P", "R", "S", "T", "KJ", "U", "F", "H", "C", ":", "X", "{", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "/", "-"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

    private static void printNote(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                Map<String, String> attributes = getAttributes(tempNode.getAttributes());
                String claz = null;
                if (attributes.containsKey("class")) {
                    claz = attributes.get("class");
                }
                //System.out.println(tempNode.getNodeName() +  );
                if (tempNode.getNodeName().equals("h1")) {
                    xKniga = new Kniga();
                    kcounter++;
                    xBiblija.getKnigi().add(xKniga);
                    xNaslov = new Naslov();
                    xNaslov.setNaslov(tempNode.getTextContent());
                    xKniga.setNaslov(xNaslov);
                    xKniga.setKratenka(kratenki.get(kcounter));
                }
                if (tempNode.getNodeName().equals("h2")) {
                    xNaslov.setPodnaslov(tempNode.getTextContent());
                }
                if (tempNode.getNodeName().equals("h3")) {
                    xVoved = new Voved();
                    if (tempNode.getTextContent().contains("Вовед")) {
                        xKniga.setVoved(xVoved);
                    } else {
                        xKniga.setPredgovor(xVoved);
                    }
                }
                if (tempNode.getNodeName().equals("p") && claz != null && claz.contains("voved_tekst"))
                    xVoved.getParagrafi().add(tempNode.getTextContent());
                if (tempNode.getNodeName().equals("h4")) {
                    if (xPodnaslov == null)
                        xPodnaslov = new Podnaslov();
                    if (claz != null && claz.equals("megunaslov"))
                        xPodnaslov.setMegunaslov(tempNode.getTextContent());
                    if (claz != null && claz.equals("podnaslov")) {
                        //Psalmi i ona
                        if (tempNode.getTextContent().contains("ПСАЛМ")) {
                            psalmcount++;
                            xGlava = new Glava();
                            xKniga.getGlavi().add(xGlava);
                            xGlava.setRedenBroj(psalmcount);
                            xPodnaslov.setPsalm("ПСАЛМ " + psalmcount);
                            xPodnaslov.setPodnaslov(tempNode.getTextContent().replace("ПСАЛМ " + psalmcount, "").trim());
                        } else if (tempNode.getTextContent().contains("ЧЕТВРТА ЗБИРКА ОД ПОУКИ")) {
                            xPodnaslov.setMegunaslov("ЧЕТВРТА ЗБИРКА ОД ПОУКИ");
                            xPodnaslov.setPodnaslov(tempNode.getTextContent().replace("ЧЕТВРТА ЗБИРКА ОД ПОУКИ", "").trim());
                        } else {
                            xPodnaslov.setPodnaslov(tempNode.getTextContent());
                        }
                    }
                    if (claz != null && claz.equals("podnaslovReferenca"))
                        xPodnaslov.setReferenci(tempNode.getTextContent());
                }
                if (tempNode.getNodeName().equals("sup") && claz != null && claz.contains("stih_broj")) {
                    if (xStih.getDodatok() != null && tempNode.getTextContent().matches("-?\\d+") && xStih.getRedenBroj() == Integer.parseInt(tempNode.getTextContent()))
                        continue;
                    xStih = new Stih();
                    xGlava.getStihovi().add(xStih);
                    if (tempNode.getTextContent().matches("-?\\d+")) {
                        xStih.setRedenBroj(Integer.parseInt(tempNode.getTextContent()));
                    } else {
                        //pisalko.println(t.getValue() + p.getNumber());
                        String broj = "";
                        String flagche = "";
                        for (int z = 0; z < tempNode.getTextContent().length(); z++) {
                            if (StringUtils.isNumeric("" + tempNode.getTextContent().charAt(z))) {
                                broj += tempNode.getTextContent().charAt(z);
                            } else {
                                flagche += tempNode.getTextContent().charAt(z);
                            }
                        }
                        xStih.setRedenBroj(Integer.parseInt(broj));
                        xStih.setDodatok(flagche.trim());
                    }

                    if (xPodnaslov != null) {
                        xStih.setPodnaslov(xPodnaslov);
                    } else {
                        xStih.setPodnaslov(new Podnaslov());
                    }
                    xPodnaslov = null;
                }
                if (tempNode.getNodeName().equals("span") && claz != null && claz.contains("glava_broj")) {
                    xGlava = new Glava();
                    xKniga.getGlavi().add(xGlava);
                    //System.out.println(tempNode.getTextContent());
                    xGlava.setRedenBroj(Integer.parseInt(tempNode.getTextContent()));
                    xStih = new Stih();
                    xGlava.getStihovi().add(xStih);
                    xStih.setRedenBroj(1);

                    if (xPodnaslov != null) {
                        xStih.setPodnaslov(xPodnaslov);
                    } else {
                        xStih.setPodnaslov(new Podnaslov());
                    }
                    xPodnaslov = null;
                    //pisalko.println(tempNode.getTextContent() + " " + getAttributes(tempNode.getAttributes()));
                }
                if (tempNode.getNodeName().equals("span") && claz != null && claz.contains("stih_tekst")) {
                    if (xStih.getTekst() != null) {
                        xStih.setTekst(xStih.getTekst().trim() + tempNode.getTextContent());
                    } else {
                        xStih.setTekst(tempNode.getTextContent());
                    }
                }
                if (tempNode.getNodeName().equals("sup") && claz != null && claz.equals("futnota")) {
                    counter++;
                    xFutnota = new Futnota();
                    xFutnota.setRedenBroj(counter);
                    mapa.put(attributes.get("id"), counter);
                    xFutnota.setStih(kratenki.get(kcounter) + " " + xGlava.getRedenBroj() + "," + xStih.getRedenBroj());
                    xStih.getFutnoti().put(xStih.getTekst().trim().length(), xFutnota);
                }
                if (tempNode.getNodeName().equals("sup") && claz != null && claz.equals("futnota_naslov")) {
                    counter++;
                    xFutnota = new Futnota();
                    xFutnota.setRedenBroj(counter);
                    mapa.put(attributes.get("id"), counter);
                    xFutnota.setStih(null);
                    xNaslov.setFutnota(xFutnota);
                }
                if (tempNode.getNodeName().equals("sup") && claz != null && claz.equals("futnota_podnaslov")) {
                    counter++;
                    xFutnota = new Futnota();
                    xFutnota.setRedenBroj(counter);
                    mapa.put(attributes.get("id"), counter);
                    xFutnota.setStih(null);
                    xPodnaslov.setFutnota(xFutnota);
                }
                if (tempNode.getNodeName().equals("aside") && claz != null && claz.contains("futnota_tekst")) {
                    futid = attributes.get("id");
                }
                if (tempNode.getNodeName().equals("span") && claz != null && claz.contains("futnota_tekst")) {
                    Integer integer = mapa.get(futid);
                    for (Kniga xk : xBiblija.getKnigi()) {
                        if (xk.getNaslov().getFutnota() != null && xk.getNaslov().getFutnota().getRedenBroj() == integer) {
                            xk.getNaslov().getFutnota().setTekst(tempNode.getTextContent());
                        }
                        for (Glava xg : xk.getGlavi()) {
                            for (Stih xs : xg.getStihovi()) {
                                if (xs.getPodnaslov().getFutnota() != null && xs.getPodnaslov().getFutnota().getRedenBroj() == integer) {
                                    xs.getPodnaslov().getFutnota().setTekst(tempNode.getTextContent());
                                }
                                for (Integer xfid : xs.getFutnoti().keySet()) {
                                    if (xs.getFutnoti().get(xfid).getRedenBroj() == integer) {
                                        xs.getFutnoti().get(xfid).setTekst(tempNode.getTextContent());
                                    }
                                }
                            }
                        }
                    }
                }

                /*if (tempNode.hasAttributes()) {

                    // get attributes names and values
                    NamedNodeMap nodeMap = tempNode.getAttributes();

                    for (int i = 0; i < nodeMap.getLength(); i++) {

                        Node node = nodeMap.item(i);
                        pisalko.println("attr name : " + node.getNodeName());
                        pisalko.println("attr value : " + node.getNodeValue());

                    }

                }*/

                if (tempNode.hasChildNodes()) {

                    // loop again if has child nodes
                    printNote(tempNode.getChildNodes());

                }


            }

        }

    }

    private static Map<String, String> getAttributes(NamedNodeMap attributes) {
        atributi = new TreeMap<>();
        for (int i = 0; i < attributes.getLength(); i++) {

            Node node = attributes.item(i);
            atributi.put(node.getNodeName(), node.getNodeValue());
            /*sb.append("attr name : " + node.getNodeName());
            sb.append(" attr value : " + node.getNodeValue());*/

        }
        return atributi;
    }
}
