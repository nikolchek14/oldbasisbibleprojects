import bblpars.Reference;
import bblpars.*;
import biblija.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Trimmer {
    public static final String PATH = "C:\\_bblProjects\\parser\\src\\main\\resources\\";
    public static final String PATH_KNIGI = "C:\\_bblProjects\\parser\\src\\main\\resources\\knigi\\";
    public static final String triseska = "„Двајца ќе бидат на нива: едниот ќе го земат, а другиот ќе го остават.“";
    public static final String tire = "–";
    public static final String crta = "-";

    static int futnotaCounter = 0;
    static PrintWriter writer;
    static PrintWriter pisalko;

    static Map<String, Integer> kratenki = new HashMap<String, Integer>();
    static List<Reference> refs = new ArrayList<>();
    static Map<Integer, List<Reference>> refMapa = new HashMap<Integer, List<Reference>>();

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
                        populateStih(stih, bpv, glava, kniga);
                        glava.getStihovi().add(stih);
                    }
                }
            }

            mapper.writeValue(new File(PATH + "Trim1.json"), biblija);
            bookWriter(biblija);
            pisalko.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void populateStih(Stih stih, BBLParsVerse bpv, Glava glava, Kniga kniga) {
        Podnaslov podnaslov = new Podnaslov();
        podnaslov.setPodnaslov(lineMelter(bpv.getPodnaslov(), null));
        podnaslov.setMegunaslov(lineMelter(bpv.getMegunaslov(), null));
        podnaslov.setReferenci(lineMelter(bpv.getPodnaslovReferenca(), null));
        stih.setPodnaslov(podnaslov);
        Map<Integer, Futnota> futnoti = stih.getFutnoti();
        stih.setTekst(lineMelter(bpv.getText(), futnoti));
        stih.setRedenBroj(bpv.getVerseNo());
        if (bpv.getVerseFlag() != null && bpv.getVerseFlag().contains(")"))
            stih.setDodatok(bpv.getVerseFlag().trim());
        populateFutnota(stih, futnoti, glava, kniga);

        populateReferenci(stih, bpv.getReferences());
    }

    private static void populateReferenci(Stih stih, List<Reference> references) {
        for (Reference r : references) {
            Referenca referenca = new Referenca();
            if (r.getRefNum().size() > 0) {
                System.out.println(r.getRefNum());
            }
            referenca.setOriginalenStih(r.getRefNum().get(0).getValue());
            List<Linija> li = r.getRefs();
            lineSort(li);
            for (Linija l : li) {
                referenca.getReferenci().add(l.getValue());
            }
            stih.getReferenci().add(referenca);
        }
    }

    private static void populateFutnota(Stih stih, Map<Integer, Futnota> futnoti, Glava glava, Kniga kniga) {
        for (Integer key : futnoti.keySet()) {
            futnoti.get(key).setStih(kniga.getKratenka() + " " + glava.getRedenBroj() + "," + stih.getRedenBroj());
        }
        stih.setFutnoti(futnoti);
    }

    private static String lineMelter(List<Linija> li, Map<Integer, Futnota> futnoti) {
        if (li != null && li.size() > 0) {
            lineSort(li);
            StringBuilder sb = new StringBuilder();
            for (Linija l : li) {
                boolean nextAlphanumeric = getIfNextAlphanumeric(li.indexOf(l), li);
                sb.append(lineBinder(cleanMeta(l.getValue()), li.indexOf(l), li));
                if (l.getFutnota() != null && futnoti != null) {
                    Futnota futnota = new Futnota();
                    futnota.setOriginalBroj(cleanMeta(l.getFutnota().getValue()));
                    futnota.setTekst(lineMelter(l.getFuttext(), null));
                    futnotaCounter++;
                    futnota.setRedenBroj(futnotaCounter);
                    if (nextAlphanumeric) {
                        futnoti.put(sb.length() - 1, futnota);
                    } else {
                        futnoti.put(sb.length(), futnota);
                    }
                }
            }
            return stringCleaner(sb.toString().trim());
        }
        return null;
    }

    private static String stringCleaner(String s) {
        s = s.replace(" " + crta + " ", "~~==~~");
        s = s.replace(" " + crta, "~~==~~");
        s = s.replace(crta + " ", "~~==~~");
        s = s.replace("~~==~~", " " + tire + " ");
        s = s.replace(",,", "„");
        s = s.replace("\" ", "“");
        s = s.replace(" \"", "„");
        //s = s.replace("  ", " " );
        return s;
    }

    private static boolean getIfNextAlphanumeric(Integer i, List<Linija> li) {
        if (li != null && i != null && li.size() > i + 1) {
            if (StringUtils.isAlphanumericSpace("" + cleanMeta(getNextLine(i, li)).charAt(0))) {
                return true;
            }
        }
        return false;
    }

    private static String getNextLine(Integer i, List<Linija> li) {
        if (li != null && i != null && li.size() > i + 1) {
            return li.get(i + 1).getValue();
        }
        return null;
    }


    private static void lineSort(List<Linija> li) {
        Collections.sort(li, new Comparator<Linija>() {
            @Override
            public int compare(final Linija lhs, Linija rhs) {
                //TODO return 1 if rhs should be before lhs
                //     return -1 if lhs should be before rhs
                //     return 0 otherwise (meaning the order stays the same)

                if (lhs.getPage() != rhs.getPage()) {
                    return lhs.getPage() - rhs.getPage() < 0 ? -1 : 1;
                }
                if (lhs.getLeft() < 360 && rhs.getLeft() > 360) {
                    return -1;
                }
                if (lhs.getLeft() > 360 && rhs.getLeft() < 360) {
                    return 1;
                }
                if (lhs.getTop() == rhs.getTop()) {
                    if((lhs.getLeft() < 360 && rhs.getLeft() < 360) || (lhs.getLeft() > 360 && rhs.getLeft() > 360)) {
                        return lhs.getLeft() - rhs.getLeft() < 0 ? -1 : 1;
                    } else {
                        return lhs.getLeft() - rhs.getLeft() < 0 ? -1 : 1;
                    }
                }
                if((lhs.getLeft() < 360 && rhs.getLeft() < 360) || (lhs.getLeft() > 360 && rhs.getLeft() > 360)) {
                    return lhs.getTop() - rhs.getTop() < 0 ? -1 : 1;
                }
                return lhs.getTop() - rhs.getTop() < 0 ? -1 : 1;
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
                sb.append(lineBinder(cleanMeta(l.getValue()), null, null));
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
                    sb.append(lineBinder(cleanMeta(l.getValue()), iz.indexOf(l), iz));
                } else {
                    if (sb.length() > 0)
                        v.getParagrafi().add(sb.toString().trim());
                    sb = new StringBuilder();
                    sb.append(lineBinder(cleanMeta(l.getValue()), iz.indexOf(l), iz));
                }
            }
            v.getParagrafi().add(sb.toString().trim());
            return v;
        }
        return null;
    }

    private static String lineBinder(String str, Integer i, List<Linija> li) {
        str = str.replace(tire, crta);
        if (str.contains("  "))
            str = str.replace("  ", " ");
        if (str.contains("'")) {
            str = str.replace("'", "‘");
        }
        /*if(str.startsWith(crta+" "))
            str=str.replace(crta+" ", " "+crta+" ");
        /*if(str.startsWith(crta))
            str=str.replace(crta, " "+crta+" ");*/
        if (str.endsWith(" " + crta + " "))
            return str;
        if (str.endsWith(" " + crta))
            return str + " ";
        if (str.endsWith(crta) || str.endsWith(crta + " "))
            return str.substring(0, str.lastIndexOf(crta)).trim();
        if (str.endsWith(" "))
            return str;
        if (getNextLine(i, li) != null && (getNextLine(i, li).startsWith(crta) || getNextLine(i, li).startsWith(crta + " "))) {
            return str.endsWith(" ") ? str : str + " ";
        }
        if (!getIfNextAlphanumeric(i, li)) {
            return str.trim();
        }

        /*} else if (li != null && i != null && li.size() <= i + 1){
            return str.trim();
        }*/
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
                    if (stih.getFutnoti().size() > 0) {
                        printer.println("Stih" + stih.getRedenBroj() + " | " + stihWithFutnota(stih));
                        for (Integer key : stih.getFutnoti().keySet()) {
                            Futnota futnota = stih.getFutnoti().get(key);
                            printer.println("Futnota" + futnota.getRedenBroj() + " | " + futnota.getTekst());
                        }
                        printReferenci(printer, stih);
                    } else {
                        if (stih.getDodatok() != null) {
                            //pisalko.println(stih.getRedenBroj() + stih.getDodatok());
                        }
                        printer.println("Stih" + stih.getRedenBroj() + " | " + stih.getTekst());
                        printReferenci(printer, stih);
                    }
                }
            }
            printer.close();
        }
    }

    private static void printReferenci(PrintWriter printer, Stih stih) {
        for (Referenca r : stih.getReferenci()) {
            StringBuilder sb = new StringBuilder();
            for (String s : r.getReferenci()) {
                sb.append(s.trim() + " ");
            }
            pisalko.println("Referenca: " + cleanMeta(r.getOriginalenStih()).trim() + " | referenci: " + sb.toString().trim());
            printer.println("Referenca: " + cleanMeta(r.getOriginalenStih()).trim() + " | referenci: " + sb.toString().trim());
        }
    }

    private static String stihWithFutnota(Stih stih) {
        StringBuilder sb = new StringBuilder();
        int last = 0;
        for (Integer key : stih.getFutnoti().keySet()) {
            Futnota futnota = stih.getFutnoti().get(key);
            //if (key > last)
            //System.out.println(futnota);
            sb.append(stih.getTekst(), last, key);
            sb.append(futnota.getRedenBroj());
            last = key;
        }
        sb.append(stih.getTekst().substring(last));
        return sb.toString();
    }

}
