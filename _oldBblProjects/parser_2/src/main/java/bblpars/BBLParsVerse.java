package bblpars;

import java.util.ArrayList;
import java.util.List;

public class BBLParsVerse {
    List<Linija> text = new ArrayList<>();
    int verseNo;
    String verseFlag;
    List<Linija> footnote = new ArrayList<>();
    List<Linija> references = new ArrayList<>();
    List<Linija> megunaslov = new ArrayList<>();
    List<Linija> mininaslov = new ArrayList<>();
    List<Linija> podnaslov = new ArrayList<>();
    List<Linija> podnaslovReferenca = new ArrayList<>();

    public int getVerseNo() {
        return verseNo;
    }

    public void setVerseNo(int verseNo) {
        this.verseNo = verseNo;
    }

    public List<Linija> getText() {
        return text;
    }

    public void setText(List<Linija> text) {
        this.text = text;
    }

    public String getVerseFlag() {
        return verseFlag;
    }

    public List<Linija> getFootnote() {
        return footnote;
    }

    public void setFootnote(List<Linija> footnote) {
        this.footnote = footnote;
    }

    public List<Linija> getReferences() {
        return references;
    }

    public void setReferences(List<Linija> references) {
        this.references = references;
    }

    public List<Linija> getMegunaslov() {
        return megunaslov;
    }

    public void setMegunaslov(List<Linija> megunaslov) {
        this.megunaslov = megunaslov;
    }

    public List<Linija> getMininaslov() {
        return mininaslov;
    }

    public void setMininaslov(List<Linija> mininaslov) {
        this.mininaslov = mininaslov;
    }

    public List<Linija> getPodnaslov() {
        return podnaslov;
    }

    public void setPodnaslov(List<Linija> podnaslov) {
        this.podnaslov = podnaslov;
    }

    public List<Linija> getPodnaslovReferenca() {
        return podnaslovReferenca;
    }

    public void setPodnaslovReferenca(List<Linija> podnaslovReferenca) {
        this.podnaslovReferenca = podnaslovReferenca;
    }

    public void setVerseFlag(String verseFlag) {
        this.verseFlag = verseFlag;
    }

    public String toString2() {
        return "BBLParsVerse{" +
                " |No: " + verseNo +
                " |Flag: " + verseFlag +
                " |Text: " + getTextList() +
                '}';
    }

    @Override
    public String toString() {
        return "BBLParsVerse{" +
                //"text=" + text +
                ", verseNo=" + verseNo +
                ", verseFlag='" + verseFlag + '\'' +
                ", footnote=" + footnote +
                ", references=" + references +
                ", megunaslov=" + megunaslov +
                ", mininaslov=" + mininaslov +
                ", podnaslov=" + podnaslov +
                ", podnaslovReferenca=" + podnaslovReferenca +
                '}';
    }

    private String getTextList() {
        StringBuilder sb = new StringBuilder();
        for(Linija l : text){
            /*String n = s.replace(" -", "%^TIRE^%");
            n = n.replace(" –", "%^TIRE^%");*/
            sb.append(l.getValue()).append("|");
        }
        return sb.toString();
    }
}
