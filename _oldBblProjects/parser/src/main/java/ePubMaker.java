import biblija.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ePubMaker {
    public static final String PATH = "C:\\_bblProjects\\parser\\src\\main\\resources\\";
    public static final String PATH_XMLI = "C:\\_bblProjects\\parser\\src\\main\\resources\\xmli\\";

    static PrintWriter pisalko;
    private static Directives directives;
    private static String htmlString;
    private static List<Directives> dirs;

    static Map<String, Integer> kratenki = new HashMap<String, Integer>();

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        try {
            //writer = new PrintWriter(PATH + "_BibleTEXT_MAKEDONCKI.txt", "UTF-8");
            pisalko = new PrintWriter(PATH + "_Pisalko.txt", "UTF-8");
            // JSON file to Java object
            Biblija biblija = mapper.readValue(new File(PATH + "prototypePrime.json"), Biblija.class);

            File htmlTemplateFile = new File(PATH + "template.html");
            htmlString = FileUtils.readFileToString(htmlTemplateFile);
            /*String title = "New Page";
            String body = "This is Body";
            htmlString = htmlString.replace("$title", title);
            htmlString = htmlString.replace("$body", body);
            File newHtmlFile = new File("path/new.html");
            FileUtils.writeStringToFile(newHtmlFile, htmlString);*/
            /*String[] names = new String[] {
                    "Jeffrey Lebowski",
                    "Walter Sobchak",
                    "Theodore Donald 'Donny' Kerabatsos",
            };
            Directives directives = new Directives().add("actors");
            for (String name : names) {
                directives.add("actor").set(name).up();
            }
            pisalko.println(new Xembler(directives).xml());*/
            for (Kniga k : biblija.getKnigi()) {
                kratenki.put(k.getKratenka().trim(), biblija.getKnigi().indexOf(k));
            }

            bookWriter(biblija);
            pisalko.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void bookWriter(Biblija biblija) throws FileNotFoundException, UnsupportedEncodingException, ImpossibleModificationException {
        Directives futnoti = new Directives().add("body");
        dirs = new ArrayList<>();
        PrintWriter printerFutnoti = new PrintWriter(PATH_XMLI + "Futnoti.xhtml", "UTF-8");

        for (Kniga kniga : biblija.getKnigi()) {
            System.out.println(kniga.getIme() + " | (" + kniga.getKratenka()+")");
            String knigaName = "Kniga" + (biblija.getKnigi().indexOf(kniga) + 1) + "_" + convertCyrilic(kniga.getKratenka()).replace(" ", "") + ".xhtml";
            PrintWriter printer = new PrintWriter(PATH_XMLI + knigaName, "UTF-8");
            directives = new Directives().add("body");
            Directives h1 = new Directives().add("h1");
            h1.set(kniga.getNaslov().getNaslov());
            Futnota futnota1 = kniga.getNaslov().getFutnota();
            if(futnota1 !=null) {
                h1.add("sup").attr("class", "futnota_naslov").attr("id", "n"+ futnota1.getRedenBroj()).add("a").attr("epubtype", "noteref").attr("href", "../Text/Futnoti.xhtml#n" + futnota1.getRedenBroj()).set(futnota1.getRedenBroj()).up().up();

                Directives aside = new Directives().add("aside");
                Directives a = new Directives().add("a");
                //System.out.println(futnota.getRedenBroj());
                a.add("span").attr("class", "futnota_tekst_broj").set(futnota1.getRedenBroj()).up();
                a.attr("href", "../Text/" + knigaName + "#n" + futnota1.getRedenBroj());
                //System.out.println(a);
                aside.append(a).up();
                System.out.println(futnota1);
                aside.add("span").set(futnota1.getTekst()).up();
                aside.attr("epubtype", "footnote").attr("class", "futnota_tekst").attr("id", "n" + futnota1.getRedenBroj());
                dirs.add(aside);
            }
            directives.append(h1).up();
            //printer.println(kniga.getNaslov().getNaslov());
            if (kniga.getNaslov().getPodnaslov() != null)
                directives.add("h2").set(kniga.getNaslov().getPodnaslov()).up();
            //printer.println(kniga.getNaslov().getPodnaslov());
            //printer.println("***" + kniga.getIme() + " | " + kniga.getKratenka() + "***");
            //printer.println("");
            //printer.println("Вовед");

            //directives.add("div").attr("class", "ilustracii").add("img").attr("class", "segmenter").attr("src", "../Images/segmenter.png").up().up();
            directives.add("h3").set("Вовед").up();
            Directives voved = new Directives().add("div");
            for (String s : kniga.getVoved().getParagrafi()) {
                voved.add("p").attr("class", "voved_tekst").set(s).up();
                //printer.println(s);
            }
            voved.attr("class", "voved");
            directives.append(voved).up();
            //printer.println("");
            if (kniga.getPredgovor() != null) {
                directives.add("h3").set("Предговор").up();
                Directives predgovor = new Directives().add("div");
                //printer.println("Предговор");
                for (String s : kniga.getPredgovor().getParagrafi()) {
                    predgovor.add("p").attr("class", "voved_tekst").set(s).up();
                    //printer.println(s);
                }
                predgovor.attr("class", "voved");
                directives.append(predgovor).up();
            }
            //printer.println("");
            //directives.add("div").attr("class", "ilustracii").add("img").attr("class", "clipart").attr("src", "../Images/angel.png").up().up();
            for (Glava glava : kniga.getGlavi()) {
                Directives g = new Directives().add("p");
                //g.add("h5").set(glava.getRedenBroj()).up();
                //printer.println("Глава" + glava.getRedenBroj());
                for (Stih stih : glava.getStihovi()) {
                    Directives s = new Directives().add("span").attr("class", "stih");
                    if (stih.getPodnaslov().getMegunaslov() != null)
                        g.add("h4").set(stih.getPodnaslov().getMegunaslov()).attr("class", "megunaslov").up();
                    if (stih.getPodnaslov().getPsalm() != null)
                        g.add("h4").set(stih.getPodnaslov().getPsalm()).attr("class", "psalm").up();
                    //printer.println(stih.getPodnaslov().getMegunaslov());
                    if (stih.getPodnaslov().getPodnaslov() != null) {
                        Directives h4 = new Directives().add("h4");
                        h4.set(stih.getPodnaslov().getPodnaslov()).attr("class", "podnaslov");
                        //g.add("h4").set(stih.getPodnaslov().getPodnaslov()).attr("class", "podnaslov").up();
                        Futnota futnota2 = stih.getPodnaslov().getFutnota();
                        if(futnota2 !=null) {
                            h4.add("sup").attr("class", "futnota").attr("id", "n"+ futnota2.getRedenBroj()).add("a").attr("epubtype", "noteref").attr("href", "../Text/Futnoti.xhtml#n" + futnota2.getRedenBroj()).set(futnota2.getRedenBroj()).up().up();

                            Directives aside = new Directives().add("aside");
                            Directives a = new Directives().add("a");
                            //System.out.println(futnota.getRedenBroj());
                            a.add("span").attr("class", "futnota_tekst_broj").set(futnota2.getRedenBroj()).up();
                            a.attr("href", "../Text/" + knigaName + "#n" + futnota2.getRedenBroj());
                            //System.out.println(a);
                            aside.append(a).up();
                            aside.add("span").set(futnota2.getTekst()).up();
                            aside.attr("epubtype", "footnote").attr("class", "futnota_tekst").attr("id", "n" + futnota2.getRedenBroj());
                            dirs.add(aside);
                        }
                        g.append(h4).up();
                    }
                    //printer.println(stih.getPodnaslov().getPodnaslov());
                    if (stih.getPodnaslov().getReferenci() != null)
                        g.add("h4").set(stih.getPodnaslov().getReferenci()).attr("class", "podnaslovReferenca").up();
                    //printer.println(stih.getPodnaslov().getReferenci());
                    if (stih.getFutnoti().size() > 0) {
                        if (stih.getRedenBroj() == 1 && !kniga.getKratenka().equals("Пс")) {
                            s.add("span").set(glava.getRedenBroj()).attr("class", "glava_broj").attr("id","g"+glava.getRedenBroj()).up();
                        } else {
                            s.add("sup").set(stih.getRedenBroj()).attr("class", "stih_broj").attr("id","g"+glava.getRedenBroj()+"s"+stih.getRedenBroj()).up();
                        }
                        stihWithFutnota(stih, s, futnoti, knigaName);
                        //s.add("span").set().up();
                        //printer.println("Stih" + stih.getRedenBroj() + " | " + stihWithFutnota(stih));
                        for (Integer key : stih.getFutnoti().keySet()) {
                            Futnota futnota = stih.getFutnoti().get(key);
                            //printer.println("Futnota" + futnota.getRedenBroj() + " | " + futnota.getTekst());
                        }
                        printReferenci(s, stih);
                    } else {
                        if (stih.getDodatok() != null) {
                            s.add("sup").set(stih.getRedenBroj() + " " + stih.getDodatok()).attr("class", "stih_broj").up();
                            //pisalko.println(stih.getRedenBroj() + stih.getDodatok());
                        }
                        if (stih.getRedenBroj() == 1 && !kniga.getKratenka().equals("Пс")) {
                            s.add("span").set(glava.getRedenBroj()).attr("class", "glava_broj").attr("id","g"+glava.getRedenBroj()).up();
                        } else {
                            s.add("sup").set(stih.getRedenBroj()).attr("class", "stih_broj").attr("id","g"+glava.getRedenBroj()+"s"+stih.getRedenBroj()).up();
                        }
                        s.add("span").set(stih.getTekst()).attr("class", "stih_tekst").up();
                        //printer.println("Stih" + stih.getRedenBroj() + " | " + stih.getTekst());
                        printReferenci(s, stih);
                    }
                    g.append(s).up();
                }
                g.attr("class", "glava");
                directives.append(g).up();
            }
            printer.println(htmlString.replace("$body", new Xembler(directives).xml().replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "")));
            printer.close();
        }

        for(Directives d: dirs){
            //printerFutnoti.println(d);
            futnoti.append(d).up();
        }
        printerFutnoti.println(htmlString.replace("$body", new Xembler(futnoti).xml().replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "")));
        //printerFutnoti.flush();
        printerFutnoti.close();
    }

    private static void printReferenci(Directives st, Stih stih) {
        for (Referenca r : stih.getReferenci()) {
            StringBuilder sb = new StringBuilder();
            for (Link l : r.getLinkoj()) {
                int i = 0;
                if(kratenki.containsKey(l.getKniga().trim()))
                    i = kratenki.get(l.getKniga().trim()) + 1;
                String knigaName2 = "Kniga" + i + "_" + convertCyrilic(l.getKniga()).replace(" ", "") + ".xhtml";

                Directives a = new Directives().add("a");
                //System.out.println(futnota.getRedenBroj());
                a.add("sub").attr("class", "referenci").set(l.getKniga() + " " + l.getTekst()+" ").up();
                if(l.getGlava()!=0 && l.getStih()!=0) {
                    a.attr("epubtype", "noteref").attr("href", "" + knigaName2 + "#g" + l.getGlava() + "s" + l.getStih());
                } else {
                    a.attr("epubtype", "noteref").attr("href", "" + knigaName2 + "#g" + l.getGlava());
                }
                st.append(a).up();
            }
            pisalko.println("Referenca: " + cleanMeta(r.getOriginalenStih()).trim() + " | referenci: " + sb.toString().trim());
            //st.add("sub").attr("class","referenci").set("("+sb.toString().trim()+")").up();
            //printer.println("Referenca: " + cleanMeta(r.getOriginalenStih()).trim() + " | referenci: " + sb.toString().trim());
        }
    }

    private static String stihWithFutnota(Stih stih, Directives s, Directives futnoti, String s1) {
        StringBuilder sb = new StringBuilder();
        int last = 0;
        for (Integer key : stih.getFutnoti().keySet()) {
            Futnota futnota = stih.getFutnoti().get(key);
            //if (key > last)
            //System.out.println(futnota);
            s.add("span").set(stih.getTekst().substring(last, key)).attr("class", "stih_tekst").up();
            //sb.append(stih.getTekst(), last, key);
            s.add("sup").attr("class", "futnota").attr("id", "n"+futnota.getRedenBroj()).add("a").attr("epubtype", "noteref").attr("href", "../Text/Futnoti.xhtml#n" + futnota.getRedenBroj()).set(futnota.getRedenBroj()).up().up();
            //sb.append(());
            if (futnota != null && futnota.getTekst() != null) {
                Directives aside = new Directives().add("aside");
                Directives a = new Directives().add("a");
                //System.out.println(futnota.getRedenBroj());
                a.add("span").attr("class", "futnota_tekst_broj").set(futnota.getRedenBroj()).up();
                a.attr("href", "../Text/"+s1+"#n"+futnota.getRedenBroj());
                //System.out.println(a);
                aside.append(a).up();
                aside.add("span").set(futnota.getTekst()).up();
                aside.attr("epubtype", "footnote").attr("class", "futnota_tekst").attr("id", "n" + futnota.getRedenBroj());
                dirs.add(aside);
            }
            last = key;
        }
        s.add("span").set(stih.getTekst().substring(last)).attr("class", "stih_tekst").up();
        //sb.append(stih.getTekst().substring(last));
        return sb.toString();
    }

    private static String cleanMeta(String value) {
        String v = value.replace("***+++", "");
        v = v.replace("*+*+*+", "");
        v = v.replace("*-*-*-", "");
        v = v.replace("***---", "");
        return v;
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
}
