import bblmid.BBLMid;
import bblmid.BBLMidPage;
import bblmid.Linija;
import bblpars.BBLPars;
import bblpars.BBLParsBook;
import bblpars.BBLParsChapter;
import bblpars.BBLParsVerse;
import bblxml.BBL;
import bblxml.BBLPage;
import bblxml.BBLText;
import bblxml.Fontspec;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Parser2 {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> mapa = new HashMap<>();
        Map<Integer, BBLText> nasapa = new HashMap<>();

        try {
            // JSON file to Java object
            BBL bbl1 = mapper.readValue(new File("C:\\_UtilProjects\\parser\\src\\main\\resources\\BblKol1b.json"), BBL.class);
            BBL bbl2 = mapper.readValue(new File("C:\\_UtilProjects\\parser\\src\\main\\resources\\BblKol2b.json"), BBL.class);
            BBL bblm1 = mapper.readValue(new File("C:\\_UtilProjects\\parser\\src\\main\\resources\\BblMegunaslovKol1.json"), BBL.class);
            BBL bblm2 = mapper.readValue(new File("C:\\_UtilProjects\\parser\\src\\main\\resources\\BblMegunaslovKol2.json"), BBL.class);
            BBL bblp1 = mapper.readValue(new File("C:\\_UtilProjects\\parser\\src\\main\\resources\\BblPodnaslovKol1.json"), BBL.class);
            BBL bblp2 = mapper.readValue(new File("C:\\_UtilProjects\\parser\\src\\main\\resources\\BblPodnaslovKol2.json"), BBL.class);
            BBL bblNaslovi = mapper.readValue(new File("C:\\_UtilProjects\\parser\\src\\main\\resources\\BblNaslovi.json"), BBL.class);
            PrintWriter writer = new PrintWriter("C:\\_UtilProjects\\parser\\src\\main\\resources\\_BibleTEXT_MAKEDONCKI.txt", "UTF-8");
            PrintWriter pisalko = new PrintWriter("C:\\_UtilProjects\\parser\\src\\main\\resources\\_Pisalko.txt", "UTF-8");

            // BBLMid bblmid = new BBLMid();
            for(int i=0; i<bbl1.getPage().size(); i++){
                if(bbl1.getPage().get(i).getText()!=null && bbl2.getPage().get(i).getText()!=null) {
                    bbl1.getPage().get(i).getText().addAll(bbl2.getPage().get(i).getText());
                }else if(bbl2.getPage().get(i).getText()!=null && bbl1.getPage().get(i).getText()==null){
                    bbl1.getPage().get(i).setText(new ArrayList<BBLText>());
                    bbl1.getPage().get(i).getText().addAll(bbl2.getPage().get(i).getText());
                }

                if(bblm1.getPage().get(i).getText()!=null && bblm2.getPage().get(i).getText()!=null) {
                    bblm1.getPage().get(i).getText().addAll(bblm2.getPage().get(i).getText());
                }else if(bblm2.getPage().get(i).getText()!=null && bblm1.getPage().get(i).getText()==null){
                    bblm1.getPage().get(i).setText(new ArrayList<BBLText>());
                    bblm1.getPage().get(i).getText().addAll(bblm2.getPage().get(i).getText());
                }
            }

            BBLPars pars = new BBLPars();
            BBLParsBook book = new BBLParsBook();
            BBLParsChapter chapter = null;
            BBLParsVerse verse = new BBLParsVerse();
            List<String> verseList = new ArrayList<>();
            for (BBLPage p : bbl1.getPage()) {
                int index = bbl1.getPage().indexOf(p);
                List<BBLText> naslov = bblNaslovi.getPage().get(index).getText();
                List<BBLText> megunaslov = bblm1.getPage().get(index).getText();

                if (naslov != null && naslov.size() > 0) {
                    book = new BBLParsBook();
                    book.setTitle(naslov.get(0).getValue());
                    pars.getBblBooks().add(book);
                }

                if (megunaslov != null && megunaslov.size() > 0) {
                    for(BBLText t : megunaslov){
                        if(t.getWidth()/2+t.getLeft() > 360 && t.getWidth()/2+t.getLeft() < 380){
                            nasapa.put(index,t);
                        }
                        //pisalko.println(t.toString() + (t.getWidth()/2+t.getLeft()));
                    }
                }
                //System.out.println(((List<BBLText>) nasapa.get(index)).size());
                //BBLMidPage bblMidPage = new BBLMidPage();
                //bblMidPage.setPageNo(Integer.parseInt(p.getNumber()));
                if (p.getText() != null) {
                    if (nasapa.containsKey(index)) {
                        List<BBLText> seg1 = new ArrayList<>();
                        List<BBLText> seg2 = new ArrayList<>();
                        for (BBLText t : p.getText()) {
                            if(t.getTop() <nasapa.get(index).getTop()){
                                seg1.add(t);
                            } else {
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
                            if (chapter != null) {
                                verse.setText(verseList);
                                chapter.getBblVerses().add(verse);
                            }
                            chapter = new BBLParsChapter();

                            writer.println(verse);
                            verse = new BBLParsVerse();
                            verse.setVerseNo(1);
                            verseList = new ArrayList<String>();
                            chapter.setChapterNo(t.getValue());
                        }
                        if (t.getFont() == 7 && t.getHeight() == 8 && t.getDiff() > 5) {
                            verse.setText(verseList);
                            chapter.getBblVerses().add(verse);

                            writer.println(verse);
                            verse = new BBLParsVerse();
                            if (t.getValue().matches("-?\\d+")) {
                                verse.setVerseNo(Integer.parseInt(t.getValue()));
                            }else{
                                pisalko.println(t.getValue() + p.getNumber());
                                String broj = "";
                                String flagche = "";
                                for(int z=0; z< t.getValue().length() ; z++){
                                    if(StringUtils.isNumeric(""+t.getValue().charAt(z))){
                                        broj+=t.getValue().charAt(z);
                                    }else{
                                        flagche+=t.getValue().charAt(z);
                                    }
                                }
                                verse.setVerseNo(Integer.parseInt(broj));
                                verse.setVerseFlag(flagche);
                            }
                            verseList = new ArrayList<>();
                        } else if (t.getFont() == 7 && t.getHeight() == 8 && t.getDiff() <= 5) {
                            String first = verseList.get(verseList.size() - 1);
                            verseList.remove(verseList.size() - 1);
                            verse.setText(verseList);
                            chapter.getBblVerses().add(verse);

                            writer.println(verse);
                            verse = new BBLParsVerse();
                            if (t.getValue().matches("-?\\d+")) {
                                verse.setVerseNo(Integer.parseInt(t.getValue()));
                            }else{
                                pisalko.println(t.getValue() + p.getNumber());
                                String broj = "";
                                String flagche = "";
                                for(int z=0; z< t.getValue().length() ; z++){
                                    if(StringUtils.isNumeric(""+t.getValue().charAt(z))){
                                        broj+=t.getValue().charAt(z);
                                    }else{
                                        flagche+=t.getValue().charAt(z);
                                    }
                                }
                                verse.setVerseNo(Integer.parseInt(broj));
                                verse.setVerseFlag(flagche);
                            }
                            verseList = new ArrayList<>();
                            verseList.add(first);
                        }
                        if ((t.getFont() == 6 && t.getHeight() == 17) || (t.getFont() == 23 && t.getHeight() == 12)) {
                            verseList.add(t.getValue());
                            if (p.getNumber().equals("1500")) {
                                //"top":257,"left":379,"width":254,"height":17,"font":6,"diff":12
                                if (t.getTop()==257 && t.getLeft()==379) {
                                    verse.setText(verseList);
                                    chapter.getBblVerses().add(verse);
                                    writer.println(verse);
                                }
                            }
                        }
                    }
                }

                //bblmid.getBblMidPages().add(bblMidPage);
            }

            System.out.println(mapa);
            writer.close();
            pisalko.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}