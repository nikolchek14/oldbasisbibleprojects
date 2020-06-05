import bblpars.Referenca;
import bblpars.*;
import biblija.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Trimmer {
    public static final String PATH = "C:\\_bblProjects\\parser\\src\\main\\resources\\";
    public static final String PATH_KNIGI = "C:\\_bblProjects\\parser\\src\\main\\resources\\knigi\\";
    public static final String triseska = "„Двајца ќе бидат на нива: едниот ќе го земат, а другиот ќе го остават.“";

    static PrintWriter writer;
    static PrintWriter pisalko;

    static Map<String, Integer> mapa = new HashMap<String, Integer>();
    static Map<String, Integer> kratenki = new HashMap<String, Integer>();
    static List<Referenca> refs = new ArrayList<>();
    static Map<Integer, List<Referenca>> refMapa = new HashMap<Integer, List<Referenca>>();

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        try {
            //writer = new PrintWriter(PATH + "_BibleTEXT_MAKEDONCKI.txt", "UTF-8");
            pisalko = new PrintWriter(PATH + "_Pisalko.txt", "UTF-8");
            // JSON file to Java object
            BBLPars bbl = mapper.readValue(new File(PATH + "parsJSON3.json"), BBLPars.class);

            Biblija biblija = new Biblija();
            for (BBLParsBook bpb : bbl.getBblBooks()) {
                Kniga kniga = new Kniga();
                populateKniga(kniga, bpb);
                biblija.getKnigi().add(kniga);
                //pisalko.println(kniga);
                for (BBLParsChapter bpc : bpb.getBblChapters()) {
                    Glava glava = new Glava();
                    populateGlava(glava, bpc);
                    kniga.getGlavi().add(glava);
                    for (BBLParsVerse bpv : bpc.getBblVerses()) {
                        Stih stih = new Stih();
                        populateStih(stih, bpv);
                        glava.getStihovi().add(stih);
                    }
                }
            }

            bookWriter(biblija);
            pisalko.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void populateStih(Stih stih, BBLParsVerse bpv) {
        Podnaslov podnaslov = new Podnaslov();
        podnaslov.setPodnaslov(lineMelter(bpv.getPodnaslov()));
        podnaslov.setMegunaslov(lineMelter(bpv.getMegunaslov()));
        podnaslov.setReferenci(lineMelter(bpv.getPodnaslovReferenca()));
        stih.setPodnaslov(podnaslov);
        stih.setTekst(lineMelter(bpv.getText()));
        stih.setRedenBroj(bpv.getVerseNo());
    }

    private static String lineMelter(List<Linija> li) {
        if (li != null && li.size() > 0) {
            lineSort(li);
            StringBuilder sb = new StringBuilder();
            for (Linija l : li) {
                sb.append(lineBinder(cleanMeta(l.getValue())));
            }
            return sb.toString();
        }
        return null;
    }

    private static void lineSort(List<Linija> li) {
        Collections.sort(li,new Comparator<Linija>(){
            @Override
            public int compare(final Linija lhs,Linija rhs) {
                //TODO return 1 if rhs should be before lhs
                //     return -1 if lhs should be before rhs
                //     return 0 otherwise (meaning the order stays the same)
                if(lhs.getTop() == rhs.getTop()){
                    return lhs.getLeft()-rhs.getLeft() < 0 ? -1 : 1;
                }
                return lhs.getTop()-rhs.getTop() < 0 ? -1 : 1;
            }
        });
    }

    private static void populateGlava(Glava glava, BBLParsChapter bpc) {
        glava.setRedenBroj(bpc.getBroj());
    }

    private static void populateKniga(Kniga kniga, BBLParsBook bpb) {
        kniga.setIme(bpb.getNaslov());
        kniga.setKratenka(bpb.getKratenka());
        Naslov naslov = new Naslov();
        StringBuilder sb = new StringBuilder();
        for (Linija l : bpb.getTitle()) {
            if (l.getValue().contains("(")) {
                naslov.setPodnaslov(cleanMeta(l.getValue()));
            } else {
                sb.append(lineBinder(cleanMeta(l.getValue())));
            }
        }
        naslov.setNaslov(sb.toString().trim());
        //System.out.println(naslov);
        kniga.setNaslov(naslov);

        List<Linija> predgovor2 = bpb.getBblIntro().getPredgovor();
        List<Linija> voved2 = bpb.getBblIntro().getVoved();
        Voved voved = populateVoved(voved2);
        Voved predgovor = populateVoved(predgovor2);

        kniga.setVoved(voved);
        //System.out.println(kniga.getKratenka() + " | " + voved);
        kniga.setPredgovor(predgovor);
        //System.out.println(predgovor);
    }

    private static Voved populateVoved(List<Linija> iz) {
        if (iz != null && iz.size() > 0) {
            Voved v = new Voved();
            StringBuilder sb = new StringBuilder();
            for (Linija l : iz) {
                if (l.getLeft() < 102 || l.getDif() == 0) {
                    sb.append(lineBinder(cleanMeta(l.getValue())));
                } else {
                    if (sb.length() > 0)
                        v.getParagrafi().add(sb.toString().trim());
                    sb = new StringBuilder();
                    sb.append(lineBinder(cleanMeta(l.getValue())));
                }
            }
            v.getParagrafi().add(sb.toString().trim());
            return v;
        }
        return null;
    }

    private static String lineBinder(String str) {
        if (str.endsWith(" - ") || str.endsWith(" -"))
            return str;
        if (str.endsWith("-") || str.endsWith("- "))
            return str.replace("-", "").trim();
        if (str.endsWith(" "))
            return str;
        return str + " ";
    }

    private static String cleanMeta(String value) {
        String v = value.replace("***+++", "");
        v = v.replace("*+*+*+", "");
        v = v.replace("*-*-*-", "");
        v = v.replace("***---", "");
        return v;
    }

    private static void bookWriter(Biblija biblija) throws FileNotFoundException, UnsupportedEncodingException {
        for (Kniga kniga : biblija.getKnigi()) {
            PrintWriter printer = new PrintWriter(PATH_KNIGI + "Kniga" + (biblija.getKnigi().indexOf(kniga) + 1) + "_" + kniga.getKratenka() + ".txt", "UTF-8");
            printer.println(kniga.getNaslov().getNaslov());
            if (kniga.getNaslov().getPodnaslov() != null)
                printer.println(kniga.getNaslov().getPodnaslov());
            printer.println("***" + kniga.getIme() + " | " + kniga.getKratenka() + "***");
            printer.println("");
            printer.println("Вовед");
            for (String s : kniga.getVoved().getParagrafi()) {
                printer.println(s);
            }
            printer.println("");
            if (kniga.getPredgovor() != null) {
                printer.println("Предговор");
                for (String s : kniga.getPredgovor().getParagrafi()) {
                    printer.println(s);
                }
            }
            printer.println("");
            for (Glava glava : kniga.getGlavi()) {
                printer.println("Глава" + glava.getRedenBroj());
                for (Stih stih : glava.getStihovi()) {
                    if (stih.getPodnaslov().getMegunaslov() != null)
                        printer.println(stih.getPodnaslov().getMegunaslov());
                    if (stih.getPodnaslov().getPodnaslov() != null)
                        printer.println(stih.getPodnaslov().getPodnaslov());
                    if (stih.getPodnaslov().getReferenci() != null)
                        printer.println(stih.getPodnaslov().getReferenci());
                    printer.println("Stih" + stih.getRedenBroj() + " | " + stih.getTekst());
                }
            }
            printer.close();
        }
    }
}
