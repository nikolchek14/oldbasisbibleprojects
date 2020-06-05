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
import java.util.HashMap;
import java.util.Map;

public class Parser3 {
    public static final String PATH = "C:\\_bblProjects\\parser\\src\\main\\resources\\";
    public static final String triseska = "„Двајца ќе бидат на нива: едниот ќе го земат, а другиот ќе го остават.“";

    static PrintWriter writer;
    static PrintWriter pisalko;

    static Map<BBLText, Integer> mapa = new HashMap<BBLText, Integer>();
    static Map<String, Integer> kratenki = new HashMap<String, Integer>();

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        try {
            //writer = new PrintWriter(PATH + "_BibleTEXT_MAKEDONCKI.txt", "UTF-8");
            pisalko = new PrintWriter(PATH + "_Pisalko.txt", "UTF-8");
            // JSON file to Java object
            BBLPars bbl = mapper.readValue(new File(PATH + "parsJSON.json"), BBLPars.class);
            BBL bblfut = mapper.readValue(new File(PATH + "BblFutnoti.json"), BBL.class);
            BBL bblvov = mapper.readValue(new File(PATH + "BblVoved.json"), BBL.class);
            BBL bblkat = mapper.readValue(new File(PATH + "BblFiller.json"), BBL.class);

            int traker = 0;
            for (BBLPage p : bblkat.getPage()) {
                int index = bblkat.getPage().indexOf(p);
                if ((index==1187 || index==6 || index==7) && p.getText() != null) {
                    for(int j = 0; j<p.getText().size();j++){
                        BBLText t = p.getText().get(j);
                        if(t.getFont()==6 && t.getHeight()==17 && t.getLeft()<360){
                            if(p.getText().size()>= j+1) {
                                BBLParsBook book = bbl.getBblBooks().get(traker);
                                book.setNaslov(t.getValue().trim());
                                book.setKratenka(p.getText().get(j + 1).getValue().trim());
                                traker++;

                                //System.out.println(book.getTitle()+"|"+book.getNaslov()+"|"+book.getKratenka());
                                kratenki.put(p.getText().get(j + 1).getValue(), traker);
                            }
                        }
                    }
                }
            }

            //font="13" height="18"
            //font="23" height="12"
            traker = -1;
            boolean pred = false;
            for (BBLPage p : bblvov.getPage()) {
                int index = bblvov.getPage().indexOf(p);
                if(p.getText()!=null)
                for(int j = 0; j<p.getText().size();j++){
                    BBLParsBook book = null;
                    if(traker>-1)
                        book = bbl.getBblBooks().get(traker);
                    BBLText t = p.getText().get(j);
                    if((t.getFont()==13 && t.getHeight()==18) || (t.getFont()==23 && t.getHeight()==12)){
                        if(!t.getValue().contains("ПРЕДГОВОР")){
                            if(book != null)
                                //pisalko.println(book.getBblIntro());
                            pred = false;
                            traker++;
                        } else {
                            //pisalko.println(book.getBblIntro());
                            pred = true;
                        }
                    }else{
                        if(pred){
                            book.getBblIntro().getPredgovor().add(new Linija(t.getTop(),t.getWidth()/2+t.getLeft(),t.getValue(),t.getDiff(),index,t.getLeft()));
                        }else{
                            book.getBblIntro().getVoved().add(new Linija(t.getTop(),t.getWidth()/2+t.getLeft(),t.getValue(),t.getDiff(),index,t.getLeft()));
                        }
                    }
                }
            }
            //pisalko.println(bbl.getBblBooks().get(76).getBblIntro());
            //{16h18=155, left 110; main line (ex: 969 996
            // 18h14=21, left 97; second line (ex: 651 777
            // 18h11=27, left 113; main line altnumber
            // 18h10=28} left 123 | 128 | 129; main line || left 100; second line
            for (BBLPage p : bblfut.getPage()) {
                int index = bblfut.getPage().indexOf(p);
                if (p.getText() != null) {
                    for(BBLText t : p.getText()){
                        if ((t.getFont()==16 && t.getHeight()==18 && t.getLeft()==110) || (t.getFont()==18 && t.getHeight()==11 && t.getLeft()==113)) {
                            //pisalko.println(t);
                            mapa.put(t,index);
                            addToLinija(index, t, bbl, 1);
                            //pisalko.println(t+"p:"+index);
                        } else if ((t.getFont()==18 && t.getHeight()==14 && t.getLeft()==97) || (t.getFont()==18 && t.getHeight()==10 && t.getLeft()==100)) {
                            System.out.println(t);
                            mapa.put(t,index);
                            addToLinija(index, t, bbl, 2);
                        } else {
                            mapa.put(t,index);
                        }
                        /*if (!mapa.containsKey(t.getFont()+"h"+t.getHeight())) {
                            mapa.put(t.getFont()+"h"+t.getHeight(), 1);
                        }else{
                            Integer c = mapa.get(t.getFont()+"h"+t.getHeight());
                            mapa.put(t.getFont()+"h"+t.getHeight(), ++c);
                        }*/
                    }
                }
            }


            mapper.writeValue(new File(PATH + "parsJSON2.json"), bbl);

            System.out.println(mapa);
            //writer.close();
            pisalko.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addToLinija(int index, BBLText t, BBLPars bbl, int i) {
        for(BBLParsBook bpb : bbl.getBblBooks()){
            if(bpb.getTitle().get(0).getPage() == index){
                checkIfFut(bpb.getTitle().get(0), t, i,index);
            }
            for(BBLParsChapter bpc : bpb.getBblChapters()) {
                for (BBLParsVerse bpv : bpc.getBblVerses()) {
                    for (Linija l : bpv.getText()) {
                        if(l.getPage() == index){
                            checkIfFut(l, t, i,index);
                        }
                    }
                    for (Linija l : bpv.getPodnaslov()) {
                        if(l.getPage() == index){
                            checkIfFut(l, t, i,index);
                        }
                    }
                }
            }
        }
    }

    private static void checkIfFut(Linija l, BBLText t, int i,int index) {
        if(l.getFutnota()!=null && t!=null){
            if(i==1){
            if(StringUtils.isNumeric(t.getValue())){
                String s = cleanMeta(l.getFutnota().getValue());
                if(s.equals(t.getValue())){
                    mapa.remove(t);
                    l.getFuttext().add(new Linija(t.getTop(),t.getWidth()/2+t.getLeft(),t.getValue(),t.getDiff(),index, t.getLeft()));
                    //pisalko.println(s+"|linija:"+l+"|fut"+t);
                }
            } else {
                String num = getFutNumber(t.getValue());
                String s = cleanMeta(l.getFutnota().getValue());
                if (s.equals(num)) {
                    mapa.remove(t);
                    l.getFuttext().add(new Linija(t.getTop(), t.getWidth() / 2 + t.getLeft(), t.getValue(), t.getDiff(), index, t.getLeft()));
                    //pisalko.println(s+"|linija:"+l+"|fut"+t);
                }
            }
            }else if (i==2){
                if(l.getFuttext()!=null && l.getFuttext().size()>0){
                    if(t.getTop()-l.getFuttext().get(l.getFuttext().size()-1).getTop()==t.getDiff()){
                        mapa.remove(t);
                        l.getFuttext().add(new Linija(t.getTop(), t.getWidth() / 2 + t.getLeft(), t.getValue(), t.getDiff(), index, t.getLeft()));
                        pisalko.println("|linija:"+l+" |fut"+t);
                    }
                }
            }
        }
    }

    private static String getFutNumber(String value) {
        if(value.contains("*+*+*+")){
            String num = value.substring(value.indexOf("***+++")+"***+++".length(), value.indexOf("*+*+*+"));
            num = num.trim();
            return num;
        }
        return "";
    }

    private static String cleanMeta(String value) {
        String v = value.replace("***+++","");
        v = v.replace("*+*+*+","");
        return v;
    }
}
