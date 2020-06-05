import bblmid.BBLMid;
import bblmid.BBLMidPage;
import bblmid.Linija;
import bblxml.BBL;
import bblxml.BBLPage;
import bblxml.BBLText;
import bblxml.Fontspec;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Parser {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> mapa = new HashMap<>();

        try {
            // JSON file to Java object
            BBL bbl = mapper.readValue(new File("C:\\_bblProjects\\parser\\src\\main\\resources\\BblKol1b.json"), BBL.class);
            PrintWriter writer = new PrintWriter("C:\\_bblProjects\\parser\\src\\main\\resources\\_BibleTEXT_MAKEDONCKI.txt", "UTF-8");


            BBLMid bblmid = new BBLMid();
            for (BBLPage p : bbl.getPage()) {
                BBLMidPage bblMidPage = new BBLMidPage();
                bblMidPage.setPageNo(Integer.parseInt(p.getNumber()));
                if (p.getText() != null) {
                    for (BBLText t : p.getText()) {
                        /* [9h18,	 podnaslovi
                        6h17,	tekst
                        7h8,	stih
                        16h14,	futnota
                        16h18,	futtekst
                        18h14,	futtekst
                        14h18,	podnaslov ref
                        23h11,	podnaslov
                        23h12,	tekst
                        18h10,	futtekst
                        18h11,	futmark
                        19h14]	podnaslov ref
                        */
                        //{23h12=30219, 6h17=38173, 7h8=17871, 16h14=76, 18h10=15}
                        /****************/
                        //12h19=796 *
                        if (t.getFont() == 25 && t.getHeight() == 22) {
                            writer.println(t.getValue());
                        }


                        if (t.getHeight() < 30 && t.getFont() > 5) {

                            if (!mapa.containsKey(t.getFont() + "h" + t.getHeight())) {
                                mapa.put(t.getFont() + "h" + t.getHeight(), 1);
                            } else {
                                int c = mapa.get(t.getFont() + "h" + t.getHeight());
                                mapa.put(t.getFont() + "h" + t.getHeight(), ++c);
                            }
                        }

                    }

                    for (BBLText t : p.getText()) {
                        Linija l = new Linija();
                        l.setFont(t.getFont());
                        l.setHeight(t.getHeight());
                        l.setWidth(t.getWidth());
                        l.setLeft(t.getLeft());
                        l.setTop(t.getTop());
                        l.setValue(t.getValue());
                        //7 (h10) - референца (l >650 <90) alt 7(h7)
                        if (t.getFont() == 7 && (t.getHeight() == 7 || t.getHeight() == 10)) {
                            l.setFlag("referenca");
                            if (t.getLeft() < 90) {
                                bblMidPage.getReferenci1().add(l);
                            } else if (t.getLeft() > 650) {
                                bblMidPage.getReferenci2().add(l);
                            }
                        }
                        //8 (h16) - boldref 28(h13)
                        if ((t.getFont() == 28 && t.getHeight() == 13) || (t.getFont() == 8 && t.getHeight() == 16)) {
                            l.setFlag("boldreferenca");
                            if (t.getLeft() < 90) {
                                bblMidPage.getReferenci1().add(l);
                            } else if (t.getLeft() > 650) {
                                bblMidPage.getReferenci2().add(l);
                            }
                        }

                        //6 (h17) - текст alt 23(h12) (90-200 ; 300-630)
                        if ((t.getFont() == 6 && t.getHeight() == 17) || (t.getFont() == 23 && t.getHeight() == 12)) {
                            l.setFlag("tekst");
                            if (t.getLeft() < 360 && t.getLeft() > 90) {
                                bblMidPage.getKolona1().add(l);
                            } else if (t.getLeft() > 360 && t.getLeft() < 630) {
                                bblMidPage.getKolona2().add(l);
                            }
                        }
                        //7 (h8 w5-10) - стих
                        if (t.getFont() == 7 && t.getHeight() == 8) {
                            l.setFlag("stih");
                            if (t.getLeft() < 360 && t.getLeft() > 90) {
                                bblMidPage.getKolona1().add(l);
                            } else if (t.getLeft() > 360 && t.getLeft() < 630) {
                                bblMidPage.getKolona2().add(l);
                            }
                        }
                        //9 (h18) podnaslov alt 23(h11)
                        if ((t.getFont() == 9 && t.getHeight() == 18) || (t.getFont() == 23 && t.getHeight() == 11)) {
                            l.setFlag("podnaslov");
                            if (t.getWidth() > 280) {
                                l.setFlag("voved tekst");
                                bblMidPage.getVoved().add(l);
                            } else {
                                if (t.getLeft() < 360 && t.getLeft() > 90) {
                                    bblMidPage.getKolona1().add(l);
                                } else if (t.getLeft() > 360 && t.getLeft() < 630) {
                                    bblMidPage.getKolona2().add(l);
                                }
                            }
                        }

                        //11 (h32) kniga naslov alt 25(h22)
                        if ((t.getFont() == 11 && t.getHeight() == 32) || (t.getFont() == 25 && t.getHeight() == 22)) {
                            l.setFlag("naslov");
                            bblMidPage.getTitle().add(l);
                        }
                        //13 (h18) voved 23(h12)
                        if ((t.getFont() == 13 && t.getHeight() == 18)) {
                            l.setFlag("voved");
                            bblMidPage.getVoved().add(l);
                        }
                        //14 (h18) voved tekst 9(h18) 23(h11) 23(h14)
                        if (t.getFont() == 14 && t.getHeight() == 18) {
                            l.setFlag("voved");
                            bblMidPage.getVoved().add(l);
                            //System.out.println(l.getValue());
                        }
                        if (t.getFont() == 9 && t.getHeight() == 18) {
                            bblMidPage.getVoved().add(l);
                            //System.out.println(l.getValue());
                        }
                        if (t.getFont() == 23 && t.getHeight() == 11) {
                            l.setFlag("voved");
                            bblMidPage.getVoved().add(l);
                            //System.out.println(l.getValue());
                        }
                        if (t.getFont() == 23 && t.getHeight() == 14) {
                            l.setFlag("voved");
                            bblMidPage.getVoved().add(l);
                            //System.out.println(l.getValue());
                        }

                        //10 (h45) glava alt 17 (h43) 17 (h42) 20 (h46) 21(h44) 22(h47) 24(h36) 26(h36) 27(h35) 29(h37) 30(h34) 31(h33) 32(h42)
                        if (t.getFont() == 10 ||
                                t.getFont() == 17 ||
                                t.getFont() == 20 ||
                                t.getFont() == 21 ||
                                t.getFont() == 22 ||
                                t.getFont() == 24 ||
                                t.getFont() == 26 ||
                                t.getFont() == 27 ||
                                t.getFont() == 29 ||
                                t.getFont() == 30 ||
                                t.getFont() == 31 ||
                                t.getFont() == 32) {
                            if (t.getHeight() > 30 && t.getHeight() < 50) {
                                l.setFlag("glava");
                                if (t.getLeft() < 200 && t.getLeft() > 90) {
                                    bblMidPage.getKolona1().add(l);
                                } else if (t.getLeft() > 360 && t.getLeft() < 630) {
                                    bblMidPage.getKolona2().add(l);
                                }
                            }
                        }

                        //16 (h14) futnota alt 18(h10) DONE
                        if (t.getFont() == 16 && t.getHeight() == 14) {
                            l.setFlag("futnota");
                            if (t.getLeft() < 360 && t.getLeft() > 90) {
                                bblMidPage.getKolona1().add(l);
                            } else if (t.getLeft() > 360 && t.getLeft() < 630) {
                                bblMidPage.getKolona2().add(l);
                            }
                            //System.out.println(l.getValue());
                        }
                        if (t.getFont() == 18 && t.getHeight() == 10) {
                            l.setFlag("futnota");
                            if (t.getLeft() < 360 && t.getLeft() > 90 && t.getWidth() < 12) {
                                bblMidPage.getKolona1().add(l);
                            } else if (t.getLeft() > 360 && t.getLeft() < 630 && t.getWidth() < 12) {
                                bblMidPage.getKolona2().add(l);
                            }
                            if (t.getWidth() > 12) {
                                l.setFlag("futnota tekst");
                                bblMidPage.getFootnoti().add(l);
                            }
                        }
                        //16 (h18) futnota tekst
                        if (t.getFont() == 16 && t.getHeight() == 18) {
                            l.setFlag("futnota tekst");
                            bblMidPage.getFootnoti().add(l);

                        }
                        //18 (h11) futnota markacija
                        if (t.getFont() == 18 && t.getHeight() == 11) {
                            l.setFlag("futnota markacija");
                            bblMidPage.getFootnoti().add(l);
                        }
                        //18 (h14) futnota tekst vtora linija alt 18h13 ??
                        if ((t.getFont() == 18 && t.getHeight() == 14) || (t.getFont() == 18 && t.getHeight() == 13)) {
                            l.setFlag("vtora linija futnota");
                            bblMidPage.getFootnoti().add(l);
                        }
                        //19 (h14) referenci na podnaslov
                        if (t.getFont() == 19 && t.getHeight() == 14) {
                            l.setFlag("podnaslov referenci");
                            if (t.getLeft() < 360 && t.getLeft() > 90) {
                                bblMidPage.getKolona1().add(l);
                            } else if (t.getLeft() > 360 && t.getLeft() < 630) {
                                bblMidPage.getKolona2().add(l);
                            }
                        }
                        //23 (h17) tekst so defekti
                        if (t.getFont() == 23 && t.getHeight() == 17) {
                            l.setFlag("tekst so defekt");
                            if (t.getLeft() < 360 && t.getLeft() > 90) {
                                bblMidPage.getKolona1().add(l);
                            } else if (t.getLeft() > 360 && t.getLeft() < 630) {
                                bblMidPage.getKolona2().add(l);
                            }
                        }
                    }
                }
                bblmid.getBblMidPages().add(bblMidPage);
            }


            System.out.println(mapa);
            writer.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }
}