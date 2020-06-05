import bblpars.*;
import biblija.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Melt1 {
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
            BBLPars refs = mapper.readValue(new File(PATH + "parsJSON3.json"), BBLPars.class);
            Biblija bbl = mapper.readValue(new File(PATH + "prototype1.json"), Biblija.class);

            for (Kniga k : bbl.getKnigi()) {
                kratenki.put(k.getKratenka().trim(), bbl.getKnigi().indexOf(k));
            }

            for (Kniga k : bbl.getKnigi()) {
                int kind = bbl.getKnigi().indexOf(k);
                BBLParsBook rKniga = refs.getBblBooks().get(kind);
                System.out.println(k.getKratenka());
                for (Glava g : k.getGlavi()) {
                    int gind = k.getGlavi().indexOf(g);
                    BBLParsChapter rGlava = rKniga.getBblChapters().get(gind);
                    for (Stih s : g.getStihovi()) {
                        int sind = g.getStihovi().indexOf(s);
                        BBLParsVerse rStih = rGlava.getBblVerses().get(sind);
                        s.setReferenci(null);
                        List<Referenca> refz = new ArrayList<>();
                        String krat = k.getKratenka();
                        for (Reference r : rStih.getReferences()) {
                            Referenca ref = new Referenca();
                            ref.setOriginalenStih(k.getKratenka() + " " + g.getRedenBroj() + "," + s.getRedenBroj());
                            List<Link> links = extractLinks(r, krat);
                            ref.setLinkoj(links);
                            pisalko.println("*** *** *** *** ***");
                            for (Link l : links) {
                                pisalko.println(l);
                            }
                            refz.add(ref);
                            //linkoj.add(l);
                            //pisalko.println(l);
                            //pisalko.println(r.getRefs());
                        }

                        s.setReferenci(refz);

                    }
                }
            }

            mapper.writeValue(new File(PATH + "prototypePrime.json"), bbl);
            //System.out.println(refz.size());
            //System.out.println(refz);
            //System.out.println(refs.size());
            //System.out.println("Stihoj: " + count);
            pisalko.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Link> extractLinks(Reference r, String krat) {
        List<Link> linkoj = new ArrayList<>();
        boolean newLink = true;
        Link li = new Link();
        for (Linija l : r.getRefs()) {
            if (newLink)
                li = new Link();
            for (String k : kratenki.keySet()) {
                if (l.getValue().contains(k)) {
                    krat = k;
                }
                if (l.getValue().contains("Езд")) {
                    krat = "1 Езд";
                }
                if (l.getValue().contains("1 Јн")) {
                    krat = "1 Јн";
                }
                if (l.getValue().contains("2 Јн")) {
                    krat = "2 Јн";
                }
                if (l.getValue().contains("3 Јн")) {
                    krat = "3 Јн";
                }
                if (l.getValue().contains("1 Царн")) {
                    krat = "1 Цар";
                }
                if (l.getValue().contains("2 Царн")) {
                    krat = "2 Цар";
                }
                if (l.getValue().contains("3 Птр")) {
                    krat = "3 Птр";
                }
                if (l.getValue().contains("Јоб")) {
                    krat = "Јов";
                }
                if (l.getValue().contains("1 Сам")) {
                    krat = "1 Сам";
                }
                if (l.getValue().contains("2 Сам")) {
                    krat = "2 Сам";
                }
                if (l.getValue().contains("Посл.")) {
                    krat = "Посл";
                }
            }
            li.setKniga(krat);
            if (!newLink) {
                li.setTekst(li.getTekst() + l.getValue().replace(krat, ""));
            } else {
                li.setTekst(l.getValue().replace(krat, ""));
                if (l.getValue().contains("Езд"))
                    li.setTekst(l.getValue().replace("Езд", ""));
                if (l.getValue().contains("Јоб"))
                    li.setTekst(l.getValue().replace("Јоб", ""));
                if (l.getValue().contains("1 Царн"))
                    li.setTekst(l.getValue().replace("1 Царн", ""));
                if (l.getValue().contains("2 Царн"))
                    li.setTekst(l.getValue().replace("2 Царн", ""));
                if (l.getValue().contains("Посл."))
                    li.setTekst(l.getValue().replace("Посл.", ""));
            }
            li.setTekst(li.getTekst().trim());
            newLink = extractMeta(li, newLink);
            if (!li.getTekst().trim().isEmpty() && li.getStih() == 0 && li.getGlava() == 0)
                pisalko.println(li + "" + l.getPage());
            /*if (StringUtils.isAlphanumericSpace(li.getTekst().trim()) && !li.getTekst().isEmpty())
                pisalko.println(li);*/
            if (newLink && !li.getTekst().trim().isEmpty())
                linkoj.add(li);
        }
        return linkoj;
    }

    private static boolean extractMeta(Link li, boolean newLink) {
        if (newLink == false) {
            String mani = li.getTekst().substring(li.getTekst().indexOf(",") + 1);
            if (mani.contains("-")) {
                String[] split1 = mani.split("-");
                if (StringUtils.isNumeric(split1[0]))
                    li.setStih(Integer.parseInt(split1[0]));
            }
            if (mani.contains(".")) {
                String[] split = mani.trim().split("\\.");
                if (split.length > 0 && StringUtils.isNumeric(split[0].trim()))
                    li.setStih(Integer.parseInt(split[0].trim()));
            }
            if (StringUtils.isNumeric(mani.trim())) {
                li.setStih(Integer.parseInt(mani.trim()));
            }
            if (li.getTekst().endsWith("-")) {
                return false;
            }
            if (li.getTekst().endsWith(".")) {
                return false;
            }
            return true;
        }
        if (!li.getTekst().contains(",") && li.getTekst().endsWith("-")) {
            System.out.println(li.getTekst());
        }
        if (li.getTekst().contains(",")) {
            String[] split = li.getTekst().split(",");
            if (li.getTekst().endsWith("-") && StringUtils.isNumeric(split[0].trim())) {
                li.setGlava(Integer.parseInt(split[0].trim()));
                return false;
            }
            if (li.getTekst().endsWith(".") && StringUtils.isNumeric(split[0].trim())) {
                li.setGlava(Integer.parseInt(split[0].trim()));
                return false;
            }
            if (split.length == 1 && StringUtils.isNumeric(split[0].trim())) {
                li.setGlava(Integer.parseInt(split[0].trim()));
                return false;
            }
            if (split.length > 1 && StringUtils.isNumeric(split[0].trim()))
                li.setGlava(Integer.parseInt(split[0].trim()));
            if (split.length > 1 && StringUtils.isNumeric(split[1].trim()))
                li.setStih(Integer.parseInt(split[1].trim()));
            if (split.length > 1 && split[1].contains("-")) {
                String[] split1 = split[1].split("-");
                if (StringUtils.isNumeric(split1[0].trim()))
                    li.setStih(Integer.parseInt(split1[0].trim()));
            }
            if (split.length > 1 && split[1].contains(".")) {
                String[] split1 = split[1].split("\\.");
                if (split1.length > 0 && StringUtils.isNumeric(split1[0].trim()))
                    li.setStih(Integer.parseInt(split1[0].trim()));
            }
        } else {
            if (li.getTekst().contains("-")) {
                String[] split = li.getTekst().trim().split("-");
                if (split.length > 0 && StringUtils.isNumeric(split[0].trim()))
                    li.setGlava(Integer.parseInt(split[0].trim()));
            }
            if (li.getTekst().contains(".")) {
                String[] split = li.getTekst().trim().split("\\.");
                if (split.length > 0 && StringUtils.isNumeric(split[0].trim()))
                    li.setGlava(Integer.parseInt(split[0].trim()));
            }
        }
        if (StringUtils.isNumeric(li.getTekst().trim())) {
            li.setGlava(Integer.parseInt(li.getTekst().trim()));
        }
        return true;
    }

}
