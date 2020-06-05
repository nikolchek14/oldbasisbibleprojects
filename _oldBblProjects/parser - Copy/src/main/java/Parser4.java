import bblpars.*;
import bblxml.BBL;
import bblxml.BBLPage;
import bblxml.BBLText;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser4 {
    public static final String PATH = "C:\\_bblProjects\\parser\\src\\main\\resources\\";
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
            BBLPars bbl = mapper.readValue(new File(PATH + "parsJSON2.json"), BBLPars.class);
            BBL bblref = mapper.readValue(new File(PATH + "BblRef.json"), BBL.class);


            Linija li = null;
            for (BBLPage p : bblref.getPage()) {
                int index = bblref.getPage().indexOf(p);
                li = pageHasMegunaslov(index, bbl);
                if (p.getText() != null) {
                    List<BBLText> seg1 = new ArrayList<>();
                    List<BBLText> seg2 = new ArrayList<>();
                    for (BBLText t : p.getText()) {
                        if (t.getLeft() < 360) {
                            seg1.add(t);
                        } else {
                            seg2.add(t);
                        }
                    }
                    seg1.addAll(seg2);
                    p.setText(seg1);
                }
                if (li != null) {
                    boolean first = true;
                    List<BBLText> seg1 = new ArrayList<>();
                    List<BBLText> seg2 = new ArrayList<>();
                    for (BBLText t : p.getText()) {
                        if (t.getTop() < li.getTop()) {
                            seg1.add(t);
                        } else {
                            if (first) {
                                first = false;
                            }
                            seg2.add(t);
                        }
                    }
                    seg1.addAll(seg2);
                    p.setText(seg1);
                    //pisalko.println(seg1);
                }

                /*if (p.getText() != null && p.getText().size() > 0) {
                    for (BBLText t : p.getText()) {
                        if (!mapa.containsKey(t.getFont() + "h" + t.getHeight())) {
                            mapa.put(t.getFont() + "h" + t.getHeight(), 1);
                        } else {
                            int c = mapa.get(t.getFont() + "h" + t.getHeight());
                            mapa.put(t.getFont() + "h" + t.getHeight(), ++c);
                        }
                    }
                }*/
            }

            //{15h16=1411, strana ignore!!!
            // 7h7=23345, referenci
            // 7h10=31805, referenci
            // 8h16=11089, boldref
            // 28h13=8780} boldref
            String glava = null;
            for (BBLPage p : bblref.getPage()) {
                int index = bblref.getPage().indexOf(p);
                if (p.getText() != null && p.getText().size() > 0) {
                    Referenca ref = null;
                    for (BBLText t : p.getText()) {
                        if ((t.getFont() == 8 && t.getHeight() == 16) || (t.getFont() == 28 && t.getHeight() == 13)) {
                            ref = new Referenca();
                            ref.setPage(index);
                            if (t.getValue().contains(",")) {
                                glava = t.getValue().split(",")[0];
                                ref.setStih(cleanMeta(t.getValue()));
                            } else {
                                ref.setStih(cleanMeta(glava) + "," + cleanMeta(t.getValue()));
                            }
                            ref.getRefNum().add(new Linija(t.getTop(), t.getWidth() / 2 + t.getLeft(), t.getValue(), t.getDiff(), index, t.getLeft()));
                            refs.add(ref);
                        } else if ((t.getFont() == 7 && t.getHeight() == 7) || (t.getFont() == 7 && t.getHeight() == 10)) {
                            ref.getRefs().add(new Linija(t.getTop(), t.getWidth() / 2 + t.getLeft(), t.getValue(), t.getDiff(), index, t.getLeft()));
                        }
                    }
                }
            }

            for (Referenca r : refs) {
                if (!refMapa.containsKey(r.getPage())) {
                    List<Referenca> refList = new ArrayList<>();
                    refList.add(r);
                    refMapa.put(r.getPage(), refList);
                } else {
                    List<Referenca> refList = refMapa.get(r.getPage());
                    refList.add(r);
                    refMapa.put(r.getPage(), refList);
                }
            }

            int count = 0;
            for (BBLParsBook bpb : bbl.getBblBooks()) {
                for (BBLParsChapter bpc : bpb.getBblChapters()) {
                    for (BBLParsVerse bpv : bpc.getBblVerses()) {
                        //count++;
                        if (bpv.getText().size() > 0) {
                            Linija l = bpv.getText().get(0);
                            List<Referenca> refList = new ArrayList<>();
                            //pisalko.println(l.getPage() + " ||  ||" + cleanMeta(bpc.getChapterNo()) + "," + bpv.getVerseNo() + "***" + " || || "+ (bpb.getBblChapters().indexOf(bpc)+1) + "," + bpv.getVerseNo() );

                            if (l != null && refMapa.containsKey(l.getPage())) {
                                refList.addAll(refMapa.get(l.getPage()));
                                if(refMapa.containsKey(l.getPage()+1))
                                    refList.addAll(refMapa.get(l.getPage()+1));
                                if(refMapa.containsKey(l.getPage()+2))
                                    refList.addAll(refMapa.get(l.getPage()+2));
                                /*if(refMapa.containsKey(l.getPage()+3))
                                    refList.addAll(refMapa.get(l.getPage()+3));
                                /*if(refMapa.containsKey(l.getPage()+4))
                                    refList.addAll(refMapa.get(l.getPage()+4));*/

                                //pisalko.println(l.getPage() + " ||  ||" + cleanMeta(bpc.getChapterNo()) + "," + bpv.getVerseNo() + "***");

                                for (Referenca r : refList) {
                                    //if (r.getPage() - l.getPage() < 3 && r.getPage() - l.getPage() > -1) {
                                    //System.out.println(cleanMeta(bpc.getChapterNo()) + "," + bpv.getVerseNo() + "***");
                                    if (r.getStih().equals(cleanMeta(bpc.getChapterNo()).trim() + "," + bpv.getVerseNo())) {
                                        bpv.getReferences().add(r);
                                        refs.remove(r);
                                        count++;
                                        //System.out.println("TUKA SUM");
                                    } else if (r.getStih().split("-")[0].equals(cleanMeta(bpc.getChapterNo()).trim() + "," + bpv.getVerseNo())) {
                                        bpv.getReferences().add(r);
                                        refs.remove(r);
                                        count++;

                                    }
                                    // }
                                }
                            }
                        }
                    }
                }
            }

            /*for (BBLParsBook bpb : bbl.getBblBooks()) {
                for (BBLParsChapter bpc : bpb.getBblChapters()) {
                    for (BBLParsVerse bpv : bpc.getBblVerses()) {
                        if (bpv.getReference() != null) {
                            pisalko.println(bpv);
                        }
                    }
                }
            }*/
            for (Referenca r : refs) {
                pisalko.println(r.getStih() + "|" + r.getPage());
            }

            mapper.writeValue(new File(PATH + "parsJSON3.json"), bbl);
            System.out.println(refs.size());
            System.out.println("Stihoj: " + count);
            pisalko.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Linija pageHasMegunaslov(int index, BBLPars bbl) {
        for (BBLParsBook bpb : bbl.getBblBooks()) {
            for (BBLParsChapter bpc : bpb.getBblChapters()) {
                for (BBLParsVerse bpv : bpc.getBblVerses()) {
                    for (Linija l : bpv.getText()) {
                        if (l.getPage() == index && bpv.getMegunaslov() != null && bpv.getMegunaslov().size() > 0) {
                            return l;
                        }
                    }
                }
            }
        }
        return null;
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
