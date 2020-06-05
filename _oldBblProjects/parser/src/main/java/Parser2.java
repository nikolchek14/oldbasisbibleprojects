import bblpars.*;
import bblxml.BBL;
import bblxml.BBLPage;
import bblxml.BBLText;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser2 {
    public static final String PATH = "C:\\_bblProjects\\parser\\src\\main\\resources\\";
    public static final String triseska = "„Двајца ќе бидат на нива: едниот ќе го земат, а другиот ќе го остават.“";

    static PrintWriter writer;
    static PrintWriter pisalko;

    static Map<String, Integer> mapa = new HashMap<String, Integer>();
    static Map<Integer, BBLText> nasapa = new HashMap<>();
    static Map<BBLText, BBLText> megumapa = new HashMap<>();

    static Map<BBLText, Integer> podmapa0 = new HashMap<>();
    static Map<BBLText, BBLText> podmapa = new HashMap<>();

    static Map<BBLText, Integer> psalmapa0 = new HashMap<>();
    static Map<BBLText, BBLText> psalmapa = new HashMap<>();


    static Map<BBLText, Integer> refmapa0 = new HashMap<>();
    static Map<BBLText, BBLText> refmapa = new HashMap<>();

    static Map<BBLText, BBLText> futmapa = new HashMap<>();

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        try {
            writer = new PrintWriter(PATH + "_BibleTEXT_MAKEDONCKI.txt", "UTF-8");
            pisalko = new PrintWriter(PATH + "_Pisalko.txt", "UTF-8");
            // JSON file to Java object
            BBL bbl1 = mapper.readValue(new File(PATH + "BblKol1b.json"), BBL.class);
            BBL bbl2 = mapper.readValue(new File(PATH + "BblKol2b.json"), BBL.class);
            BBL bblm1 = mapper.readValue(new File(PATH + "BblMegunaslovKol1.json"), BBL.class);
            BBL bblm2 = mapper.readValue(new File(PATH + "BblMegunaslovKol2.json"), BBL.class);
            BBL bblp1 = mapper.readValue(new File(PATH + "BblPodnaslovKol1.json"), BBL.class);
            BBL bblp2 = mapper.readValue(new File(PATH + "BblPodnaslovKol2.json"), BBL.class);
            BBL bblNaslovi = mapper.readValue(new File(PATH + "BblNaslovi.json"), BBL.class);

            BBL bblfut = mapper.readValue(new File(PATH + "BblFutnoti.json"), BBL.class);

            // BBLMid bblmid = new BBLMid();
            for (int i = 0; i < bbl1.getPage().size(); i++) {
                if (bbl1.getPage().get(i).getText() != null && bbl2.getPage().get(i).getText() != null) {
                    bbl1.getPage().get(i).getText().addAll(bbl2.getPage().get(i).getText());
                } else if (bbl2.getPage().get(i).getText() != null && bbl1.getPage().get(i).getText() == null) {
                    bbl1.getPage().get(i).setText(new ArrayList<BBLText>());
                    bbl1.getPage().get(i).getText().addAll(bbl2.getPage().get(i).getText());
                }

                if (bblm1.getPage().get(i).getText() != null && bblm2.getPage().get(i).getText() != null) {
                    bblm1.getPage().get(i).getText().addAll(bblm2.getPage().get(i).getText());
                } else if (bblm2.getPage().get(i).getText() != null && bblm1.getPage().get(i).getText() == null) {
                    bblm1.getPage().get(i).setText(new ArrayList<BBLText>());
                    bblm1.getPage().get(i).getText().addAll(bblm2.getPage().get(i).getText());
                }
            }

            BBLPars pars = new BBLPars();
            BBLParsBook book = null;
            BBLParsChapter chapter = null;
            BBLParsVerse verse = new BBLParsVerse();
            List<Linija> verseList = new ArrayList<>();
            for (BBLPage p : bbl1.getPage()) {
                int index = bbl1.getPage().indexOf(p);
                List<BBLText> naslov = bblNaslovi.getPage().get(index).getText();
                List<BBLText> megunaslov = bblm1.getPage().get(index).getText();

                //FUTONTI
                // 16h14=76, futnota
                // 18h10=15} futnota
                if (p.getText() != null && p.getText().size() > 0) {
                    for (BBLText t : p.getText()) {
                        if ((t.getFont() == 16 && t.getHeight() == 14) || (t.getFont() == 18 && t.getHeight() == 10)) {
                            futmapa.put(t, findFutText(t, p, bblNaslovi, bblp2, index));
                        /*if (!mapa.containsKey(t.getFont()+"h"+t.getHeight()+"d"+t.getDiff())) {
                            mapa.put(t.getFont()+"h"+t.getHeight()+"d"+t.getDiff(), t.getDiff());
                        }*/
                        }
                    }
                }

                if (naslov != null && naslov.size() > 0) {
                    if (book != null) {
                        verse.setText(verseList);
                        chapter.getBblVerses().add(verse);
                        if (!chapter.getChapterNo().equals("151"))
                            book.getBblChapters().add(chapter);
                    }
                    book = new BBLParsBook();
                    verse = new BBLParsVerse();
                    verseList = new ArrayList<>();
                    chapter = new BBLParsChapter();

                    Linija l = new Linija(naslov.get(0).getTop(), naslov.get(0).getWidth() / 2 + naslov.get(0).getLeft(), naslov.get(0).getValue(), naslov.get(0).getDiff(), index, naslov.get(0).getLeft());
                    addFootnote(l, naslov.get(0), index);
                    book.getTitle().add(l);
                    if (naslov.size() > 1) {
                        for (int i = 1; i < naslov.size(); i++) {
                            book.getTitle().add(new Linija(naslov.get(i).getTop(), naslov.get(i).getWidth() / 2 + naslov.get(i).getLeft(), naslov.get(i).getValue(), naslov.get(i).getDiff(), index, naslov.get(i).getLeft()));
                        }
                    }
                    pars.getBblBooks().add(book);
                }

                if (megunaslov != null && megunaslov.size() > 0) {
                    for (BBLText t : megunaslov) {
                        if (t.getWidth() / 2 + t.getLeft() > 360 && t.getWidth() / 2 + t.getLeft() < 380) {
                            nasapa.put(index, t);
                        }
                        //pisalko.println(t.toString() + (t.getWidth()/2+t.getLeft()));
                    }
                }

                List<BBLText> pod1 = bblp1.getPage().get(index).getText();
                List<BBLText> pod2 = bblp2.getPage().get(index).getText();

                populirajPodnaslovi(pod1, pod2, podmapa0, podmapa, index, bbl1);
                populirajPodnasloviRef(pod1, pod2, refmapa0, refmapa, index, bbl1);
                populirajPslami(bblm1.getPage().get(index).getText(), bblm2.getPage().get(index).getText(), psalmapa0, psalmapa, index, bbl1);


                //System.out.println(((List<BBLText>) nasapa.get(index)).size());
                //BBLMidPage bblMidPage = new BBLMidPage();
                //bblMidPage.setPageNo(Integer.parseInt(p.getNumber()));
                if (p.getText() != null) {
                    if (nasapa.containsKey(index)) {
                        boolean first = true;
                        List<BBLText> seg1 = new ArrayList<>();
                        List<BBLText> seg2 = new ArrayList<>();
                        for (BBLText t : p.getText()) {
                            if (t.getTop() < nasapa.get(index).getTop()) {
                                seg1.add(t);
                            } else {
                                if (first) {
                                    first = false;
                                    megumapa.put(nasapa.get(index), t);
                                }
                                seg2.add(t);
                            }
                        }
                        seg1.addAll(seg2);
                        p.setText(seg1);
                    }

                    for (BBLText t : p.getText()) {
                        // {23h12=30219, tekst
                        // 6h17=38173, tekst
                        // 7h8=17871, stih
                        // 16h14=76, futnota
                        // 18h10=15} futnota

                        if (t.getHeight() > 30) {
                            if (chapter != null && chapter.getChapterNo() != null) {
                                verse.setText(verseList);
                                chapter.getBblVerses().add(verse);
                                book.getBblChapters().add(chapter);
                            }

                            chapter = new BBLParsChapter();
                            writer.println(verse);
                            verse = new BBLParsVerse();
                            verse.setVerseNo(1);
                            verseList = new ArrayList<Linija>();
                            chapter.setChapterNum(Integer.parseInt(cleanMeta(t.getValue())));
                            chapter.setChapterNo(t.getValue());
                            verseMetadatas(verse, t, null, index);
                        }
                        if (t.getFont() == 7 && t.getHeight() == 8 && t.getDiff() > 5) {
                            verse.setText(verseList);
                            if (chapter != null) {
                                chapter.getBblVerses().add(verse);
                            }

                            writer.println(verse);
                            verse = new BBLParsVerse();
                            if (t.getValue().matches("-?\\d+")) {
                                verse.setVerseNo(Integer.parseInt(t.getValue()));
                            } else {
                                //pisalko.println(t.getValue() + p.getNumber());
                                String broj = "";
                                String flagche = "";
                                for (int z = 0; z < t.getValue().length(); z++) {
                                    if (StringUtils.isNumeric("" + t.getValue().charAt(z))) {
                                        broj += t.getValue().charAt(z);
                                    } else {
                                        flagche += t.getValue().charAt(z);
                                    }
                                }
                                verse.setVerseNo(Integer.parseInt(broj));
                                verse.setVerseFlag(flagche);
                            }
                            verseList = new ArrayList<>();
                            verseMetadatas(verse, t, null, index);
                            if (psalmNaslov(verse)) {
                                chapter = new BBLParsChapter();
                                chapter.setChapterNo(getPsalmNo(verse));
                                chapter.setChapterNum(Integer.parseInt(getPsalmNo(verse)));
                                if (verse.getVerseNo() == 1 && chapter.getBblVerses().size() != 0)
                                    chapter.getBblVerses().add(verse);
                                book.getBblChapters().add(chapter);
                            }
                        } else if (t.getFont() == 7 && t.getHeight() == 8 && t.getDiff() <= 5) {
                            if (p.getNumber().equals("1282") && t.getValue().equals("36")) {
                                verse.setText(verseList);
                                if (chapter != null) {
                                    chapter.getBblVerses().add(verse);
                                }

                                writer.println(verse);
                                verse = new BBLParsVerse();
                                verseList = new ArrayList<>();
                                Linija l = new Linija(9999, 9999, triseska, 9999, index, 9999);
                                addFootnote(l, null, index);
                                verseList.add(l);
                                verse.setVerseNo(Integer.parseInt(t.getValue()));
                            } else {
                                Linija first = verseList.get(verseList.size() - 1);
                                List<Linija> prev = new ArrayList<>();
                                if (first.getDif() == 0) {
                                    prev.add(first);
                                    Linija prev2 = verseList.get(verseList.size() - 2);
                                    if (first.getTop() == prev2.getTop()) {
                                        prev.add(prev2);
                                    }
                                    first = prev2;
                                    verseList.remove(verseList.size() - 2);
                                }
                                verseList.remove(verseList.size() - 1);
                                verse.setText(verseList);
                                chapter.getBblVerses().add(verse);

                                writer.println(verse);
                                verse = new BBLParsVerse();
                                if (t.getValue().matches("-?\\d+")) {
                                    verse.setVerseNo(Integer.parseInt(t.getValue()));
                                } else {
                                    //pisalko.println(t.getValue() + p.getNumber());
                                    String broj = "";
                                    String flagche = "";
                                    for (int z = 0; z < t.getValue().length(); z++) {
                                        if (StringUtils.isNumeric("" + t.getValue().charAt(z))) {
                                            broj += t.getValue().charAt(z);
                                        } else {
                                            flagche += t.getValue().charAt(z);
                                        }
                                    }
                                    verse.setVerseNo(Integer.parseInt(broj));
                                    verse.setVerseFlag(flagche);
                                }
                                verseList = new ArrayList<>();
                                if (prev.size() > 0) {
                                    verseList.addAll(prev);
                                } else {
                                    verseList.add(first);
                                }
                                verseMetadatas(verse, t, first, index);
                                if (psalmNaslov(verse)) {
                                    chapter = new BBLParsChapter();
                                    chapter.setChapterNo(getPsalmNo(verse));
                                    chapter.setChapterNum(Integer.parseInt(getPsalmNo(verse)));
                                    if (verse.getVerseNo() == 1 && chapter.getBblVerses().size() != 0)
                                        chapter.getBblVerses().add(verse);
                                    book.getBblChapters().add(chapter);
                                }
                            }
                        }
                        if ((t.getFont() == 6 && t.getHeight() == 17) || (t.getFont() == 23 && t.getHeight() == 12)) {
                            Linija l = new Linija(t.getTop(), t.getWidth() / 2 + t.getLeft(), t.getValue(), t.getDiff(), index, t.getLeft());
                            addFootnote(l, t, index);
                            verseList.add(l);
                            if (p.getNumber().equals("1500")) {
                                //"top":257,"left":379,"width":254,"height":17,"font":6,"diff":12
                                if (t.getTop() == 257 && t.getLeft() == 379) {
                                    verse.setText(verseList);
                                    chapter.getBblVerses().add(verse);
                                    writer.println(verse);
                                    book.getBblChapters().add(chapter);
                                }
                            }
                        }
                    }
                }
            }
            /*JAXBContext jaxbContext = JAXBContext.newInstance(BBLPars.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //Marshal the employees list in file
            jaxbMarshaller.marshal(pars, new File(PATH + "parsXML.xml"));*/

            // Java objects to JSON file
            mapper.writeValue(new File(PATH + "parsJSON.json"), pars);

            // Java objects to JSON string - compact-print
            //String jsonString = mapper.writeValueAsString(pars);

            //System.out.println(jsonString);

            // Java objects to JSON string - pretty-print
            //String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pars);
            for (BBLParsBook bpb : pars.getBblBooks()) {
                //System.out.println(bpb.getTitle());
                //if(bpb.getTitle().get(0).getFutnota() != null){
                //System.out.println(bpb.getTitle().getFutnota().getValue());
                //}
                for (BBLParsChapter bpc : bpb.getBblChapters()) {
                    for (BBLParsVerse bpv : bpc.getBblVerses()) {
                        pisalko.println(bpv);
                        for (Linija l : bpv.getText()) {
                            //if(l.getFutnota() != null){
                            //pisalko.println(l);
                            //}
                        }
                        /*for (Linija l : bpv.getPodnaslov()) {
                            if(l.getFutnota() != null){
                                System.out.println(l.getFutnota().getValue());
                            }
                        }*/
                    }
                }
            }

            pisalko.println(futmapa);
            System.out.println(futmapa.size());
            writer.close();
            pisalko.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getPsalmNo(BBLParsVerse verse) {
        for (Linija l : verse.getPodnaslov()) {
            if (l.getValue().contains("ПСАЛМ")) {
                return l.getValue().replace("ПСАЛМ", "").trim();
            }
        }
        return null;
    }

    private static boolean psalmNaslov(BBLParsVerse verse) {
        for (Linija l : verse.getPodnaslov()) {
            System.out.println(l.getValue());
            if (l.getValue().contains("ПСАЛМ")) {
                return true;
            }
        }
        return false;
    }

    private static void addFootnote(Linija l, BBLText tt, int index) {
        for (BBLText key : futmapa.keySet()) {
            if (tt == null && l.getValue().equals(futmapa.get(key).getValue())) {
                l.setFutnota(new Linija(key.getTop(), key.getWidth() / 2 + key.getLeft(), key.getValue(), key.getDiff(), index, key.getLeft()));
            }
            if (tt != null && tt.equals(futmapa.get(key))) {
                l.setFutnota(new Linija(key.getTop(), key.getWidth() / 2 + key.getLeft(), key.getValue(), key.getDiff(), index, key.getLeft()));
            }
        }
    }

    private static BBLText findFutText(BBLText f, BBLPage p, BBL bblNaslovi, BBL bblp2, int index) {
        boolean found = false;
        if (p.getNumber().equals("1282") && f.getValue().equals("***+++11*+*+*+")) {
            BBLText bbt = new BBLText();
            bbt.setValue(triseska);
            return bbt;
        }
        if (p.getNumber().equals("1224") && f.getValue().equals("***+++17*+*+*+")) {
            BBLPage bblPage = bblp2.getPage().get(index);
            for (BBLText t : bblPage.getText()) {
                if (t.getTop() == 794 && f.getLeft() == t.getLeft() + t.getWidth()) {
                    return t;
                }
            }
        }
        if (f.getTop() == 146) {
            BBLPage bblPage = bblNaslovi.getPage().get(index);
            //System.out.println(bblPage.getText().size());
            return bblPage.getText().get(0);
        }
        for (BBLText t : p.getText()) {
            int i = t.getLeft() + t.getWidth() - f.getLeft();
            if (f.getLeft() < 370 && t.getLeft() < 370) {
                if ((t.getTop() - f.getTop()) < 5 && (t.getTop() - f.getTop()) > -1 && i < 2 && i > -2) {
                    return t;
                }
            } else if (f.getLeft() > 370 && t.getLeft() > 370) {
                if ((t.getTop() - f.getTop()) < 5 && (t.getTop() - f.getTop()) > -1 && i < 2 && i > -2) {
                    return t;
                }
            }
        }

        return null;
    }

    private static void populirajPodnaslovi(
            List<BBLText> pod1,
            List<BBLText> pod2,
            Map<BBLText, Integer> apa0,
            Map<BBLText, BBLText> apa,
            int index, BBL bbl) {
        if (pod1 != null && pod1.size() > 0)
            for (BBLText t : pod1) {
                if (t != null && (t.getHeight() == 18 && t.getFont() == 9) || (t.getHeight() == 11 && t.getFont() == 23)) {
                    //pisalko.println(t.getValue()+"|"+t.getDiff());
                    if (!checkIfRef(t)) {
                        apa0.put(t, index);
                        populateMapa(t, bbl, apa, index, 1);
                    }
                }
            }
        if (pod2 != null && pod2.size() > 0)
            for (BBLText t : pod2) {
                if (t != null && (t.getHeight() == 18 && t.getFont() == 9) || (t.getHeight() == 11 && t.getFont() == 23)) {
                    //pisalko.println(t.getValue()+"|"+t.getDiff());
                    if (!checkIfRef(t)) {
                        apa0.put(t, index);
                        populateMapa(t, bbl, apa, index, 2);
                    }
                }
            }
    }

    private static boolean checkIfRef(BBLText t) {
        if (t.getHeight() == 11 && t.getFont() == 23) {
            String test = t.getValue().replace("(", "");
            test = test.replace(")", "");
            test = test.replace(":", "");
            if (t.getValue().contains("(") && t.getValue().contains(")") && !StringUtils.isAlphaSpace(test)) {
                //pisalko.println(t.getValue());
                return true;
            }
        }
        return false;
    }

    private static void populirajPodnasloviRef(
            List<BBLText> pod1,
            List<BBLText> pod2,
            Map<BBLText, Integer> apa0,
            Map<BBLText, BBLText> apa,
            int index, BBL bbl) {

        if (pod1 != null && pod1.size() > 0)
            for (BBLText t : pod1) {
                if (t != null && (t.getHeight() == 18 && t.getFont() == 14) || (t.getHeight() == 14 && t.getFont() == 19) || checkIfRef(t)) {
                    apa0.put(t, index);
                    populateMapa(t, bbl, apa, index, 1);
                }
            }
        if (pod2 != null && pod2.size() > 0)
            for (BBLText t : pod2) {
                if (t != null && (t.getHeight() == 18 && t.getFont() == 14) || (t.getHeight() == 14 && t.getFont() == 19) || checkIfRef(t)) {
                    apa0.put(t, index);
                    populateMapa(t, bbl, apa, index, 2);
                }
            }
    }

    private static void populirajPslami(
            List<BBLText> pod1,
            List<BBLText> pod2,
            Map<BBLText, Integer> apa0,
            Map<BBLText, BBLText> apa,
            int index, BBL bbl) {

        if (pod1 != null && pod1.size() > 0)
            for (BBLText t : pod1) {
                if (t != null && t.getWidth() / 2 + t.getLeft() < 360) {
                    apa0.put(t, index);
                    populateMapa(t, bbl, apa, index, 1);
                }
            }

        if (pod2 != null && pod2.size() > 0)
            for (BBLText t : pod2) {
                if (t != null) {
                    apa0.put(t, index);
                    populateMapa(t, bbl, apa, index, 2);
                }
            }
    }

    private static void populateMapa(BBLText t, BBL bbl, Map<BBLText, BBLText> apa, int index, int kol) {
        List<BBLText> ttt = bbl.getPage().get(index).getText();
        boolean found = false;
        if (ttt != null && ttt.size() > 0) {
            for (BBLText tt : ttt) {
                if (!(tt.getFont() == 16 && tt.getHeight() == 14) && !(tt.getFont() == 18 && tt.getHeight() == 10)) {
                    if (found == false && tt.getTop() > t.getTop() && (kol == 1 && tt.getLeft() < 360)) {
                        found = true;
                        apa.put(t, tt);
                    }
                    if (found == false && tt.getTop() > t.getTop() && (kol == 2 && tt.getLeft() > 360)) {
                        found = true;
                        apa.put(t, tt);
                    }
                }
            }
        }
    }

    private static void verseMetadatas(BBLParsVerse verse, BBLText t, Linija first, Integer index) {
        if (nasapa.get(index) != null) {
            for (BBLText key : megumapa.keySet()) {
                if (megumapa.get(key).equals(t)) {
                    Linija l = new Linija(key.getTop(), key.getWidth() / 2 + key.getLeft(), key.getValue(), key.getDiff(), index, key.getLeft());
                    addFootnote(l, key, index);
                    verse.getMegunaslov().add(l);
                    //pisalko.println(verse);
                } else if (first != null && megumapa.get(key).getValue().equals(first.getValue())) {
                    Linija l = new Linija(key.getTop(), key.getWidth() / 2 + key.getLeft(), key.getValue(), key.getDiff(), index, key.getLeft());
                    addFootnote(l, key, index);
                    verse.getMegunaslov().add(l);
                    // pisalko.println(verse);
                }

            }
        }
        for (BBLText key : podmapa0.keySet()) {
            if (index.equals(podmapa0.get(key))) {
                if (podmapa.get(key).equals(t)) {
                    Linija l = new Linija(key.getTop(), key.getWidth() / 2 + key.getLeft(), key.getValue(), key.getDiff(), index, key.getLeft());
                    addFootnote(l, key, index);
                    verse.getPodnaslov().add(l);

                    //pisalko.println(verse);
                } else if (first != null && podmapa.get(key).getValue().equals(first.getValue())) {
                    Linija l = new Linija(key.getTop(), key.getWidth() / 2 + key.getLeft(), key.getValue(), key.getDiff(), index, key.getLeft());
                    addFootnote(l, key, index);
                    verse.getPodnaslov().add(l);

                    //pisalko.println(verse);
                }
            }
        }
        for (BBLText key : psalmapa0.keySet()) {
            if (index.equals(psalmapa0.get(key))) {
                if (psalmapa.get(key).equals(t)) {
                    Linija l = new Linija(key.getTop(), key.getWidth() / 2 + key.getLeft(), key.getValue(), key.getDiff(), index, key.getLeft());
                    addFootnote(l, key, index);
                    verse.getPodnaslov().add(l);

                    //pisalko.println(verse);
                }
                if (first != null && psalmapa.get(key).getValue().equals(first.getValue())) {
                    Linija l = new Linija(key.getTop(), key.getWidth() / 2 + key.getLeft(), key.getValue(), key.getDiff(), index, key.getLeft());
                    addFootnote(l, key, index);
                    verse.getPodnaslov().add(l);

                    //pisalko.println(verse);
                }
            }
        }
        for (BBLText key : refmapa0.keySet()) {
            if (index.equals(refmapa0.get(key))) {
                if (refmapa.get(key).equals(t)) {
                    Linija l = new Linija(key.getTop(), key.getWidth() / 2 + key.getLeft(), key.getValue(), key.getDiff(), index, key.getLeft());
                    addFootnote(l, key, index);
                    verse.getPodnaslovReferenca().add(l);

                    //pisalko.println(verse);
                }
                if (first != null && refmapa.get(key).getValue().equals(first.getValue())) {
                    Linija l = new Linija(key.getTop(), key.getWidth() / 2 + key.getLeft(), key.getValue(), key.getDiff(), index, key.getLeft());
                    addFootnote(l, key, index);
                    verse.getPodnaslovReferenca().add(l);

                    //pisalko.println(verse);
                }
            }
        }
    }

    private static String cleanMeta(String value) {
        if (value != null) {
            String v = value.replace("***+++", "");
            v = v.replace("*+*+*+", "");
            return v.trim();
        }
        return null;
    }
}