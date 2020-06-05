import bblpars.*;
import bblxml.BBL;
import bblxml.BBLPage;
import bblxml.BBLText;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

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
    static List<Reference> refs = new ArrayList<>();
    static Map<Integer, List<Reference>> refMapa = new HashMap<Integer, List<Reference>>();
    static List<Integer> refz = new ArrayList<>();

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
                    refz.add(li.getPage());

                    /*boolean first = true;
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
                    p.setText(seg1);*/
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
                    Reference ref = null;
                    for (BBLText t : p.getText()) {
                        if ((t.getFont() == 8 && t.getHeight() == 16) || (t.getFont() == 28 && t.getHeight() == 13)) {
                            if(ref!=null)
                                pisalko.println(ref.getRefs());
                            ref = new Reference();
                            ref.setPage(index);
                            if (t.getValue().contains(",")) {
                                String oldGlava = glava;
                                glava = t.getValue().split(",")[0];
                                if (glava.equals("")) {
                                    //System.out.println(cleanMeta(t.getValue().split(",")[1]).trim() + " | " + index);
                                    refs.get(refs.size() - 1).setStih(refs.get(refs.size() - 1).getStih() + "," + t.getValue().split(",")[1].trim());
                                    glava = oldGlava;
                                } else {
                                    String val = t.getValue().split(",")[1];
                                    ref.setStih(cleanMeta(glava).trim() + "," + cleanMeta(val).trim());
                                }
                            } else {
                                String value = cleanMeta(t.getValue()).trim();
                                if (index == 18 && (value.equals("29") || value.equals("30") || value.equals("31"))) {
                                    ref.setStih(11 + "," + value);
                                } else if (index == 47 && (value.equals("40-43"))) {
                                    ref.setStih(36 + "," + value);
                                } else if (index == 753 && (value.equals("47") || value.equals("48") || value.equals("49") || value.equals("52"))) {
                                    ref.setStih(88 + "," + value);
                                } else {
                                    ref.setStih(cleanMeta(glava).trim() + "," + value);
                                }
                            }
                            ref.getRefNum().add(new Linija(t.getTop(), t.getWidth() / 2 + t.getLeft(), t.getValue(), t.getDiff(), index, t.getLeft()));
                            //pisalko.println(ref.getRefNum());
                            if(ref.getStih() != null)
                                refs.add(ref);
                        } else if ((t.getFont() == 7 && t.getHeight() == 7) || (t.getFont() == 7 && t.getHeight() == 10)) {
                            ref.getRefs().add(new Linija(t.getTop(), t.getWidth() / 2 + t.getLeft(), t.getValue(), t.getDiff(), index, t.getLeft()));
                        }
                    }
                }
            }

            for (Reference r : refs) {
                if (!refMapa.containsKey(r.getPage())) {
                    List<Reference> refList = new ArrayList<>();
                    refList.add(r);
                    refMapa.put(r.getPage(), refList);
                } else {
                    List<Reference> refList = refMapa.get(r.getPage());
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
                            List<Reference> refList = new ArrayList<>();
                            //pisalko.println(l.getPage() + " ||  ||" + cleanMeta(bpc.getChapterNo()) + "," + bpv.getVerseNo() + "***" + " || || "+ (bpb.getBblChapters().indexOf(bpc)+1) + "," + bpv.getVerseNo() );

                            if (l != null && refMapa.containsKey(l.getPage())) {
                                refList.addAll(refMapa.get(l.getPage()));

                                if (refMapa.containsKey(l.getPage() - 1))
                                    refList.addAll(refMapa.get(l.getPage() - 1));
                                if (refMapa.containsKey(l.getPage() + 1))
                                    refList.addAll(refMapa.get(l.getPage() + 1));
                                if (refMapa.containsKey(l.getPage() + 2))
                                    refList.addAll(refMapa.get(l.getPage() + 2));
                                /*if(refMapa.containsKey(l.getPage()+3))
                                    refList.addAll(refMapa.get(l.getPage()+3));
                                /*if(refMapa.containsKey(l.getPage()+4))
                                    refList.addAll(refMapa.get(l.getPage()+4));*/

                                //pisalko.println(l.getPage() + " ||  ||" + cleanMeta(bpc.getChapterNo()) + "," + bpv.getVerseNo() + "***");

                                for (Reference r : refList) {
                                    //if (r.getPage() - l.getPage() < 3 && r.getPage() - l.getPage() > -1) {
                                    //System.out.println(cleanMeta(bpc.getChapterNo()) + "," + bpv.getVerseNo() + "***");
                                    //System.out.println(r.getStih() + " | " + l.getPage());
                                    if (StringUtils.countMatches(r.getStih(), ",") > 1) {

                                    } else if (r.getStih().equals(cleanMeta(bpc.getChapterNo()).trim() + "," + bpv.getVerseNo())) {
                                        bpv.getReferences().add(r);
                                        refs.remove(r);
                                        count++;
                                        //System.out.println("TUKA SUM");
                                    } else if (r.getStih().split("-")[0].equals(cleanMeta(bpc.getChapterNo()).trim() + "," + bpv.getVerseNo())) {
                                        bpv.getReferences().add(r);
                                        refs.remove(r);
                                        count++;
                                    } else if (r.getStih().contains(".")) {
                                        if (r.getStih().split("\\.")[0].equals(cleanMeta(bpc.getChapterNo()).trim() + "," + bpv.getVerseNo())) {
                                            bpv.getReferences().add(r);
                                            refs.remove(r);
                                            count++;
                                        }
                                    } else if (r.getStih().contains(bpv.getVerseNo()+bpv.getVerseFlag()) ) {
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
            for (Reference r : refs) {
                pisalko.println(r.getStih() + "|" + r.getPage());
            }

            mapper.writeValue(new File(PATH + "parsJSON3.json"), bbl);
            //System.out.println(refz.size());
            //System.out.println(refz);
            //System.out.println(refs.size());
            //System.out.println("Stihoj: " + count);
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
