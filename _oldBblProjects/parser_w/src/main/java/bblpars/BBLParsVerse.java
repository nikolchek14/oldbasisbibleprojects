package bblpars;

import java.util.ArrayList;
import java.util.List;

public class BBLParsVerse {
    List<String> text = new ArrayList<>();
    int verseNo;
    String verseFlag;
    List<String> footnote;
    List<String> references;
    List<String> megunaslov;
    List<String> mininaslov;
    List<String> podnaslov;
    List<String> podnaslovReferenca;

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public int getVerseNo() {
        return verseNo;
    }

    public void setVerseNo(int verseNo) {
        this.verseNo = verseNo;
    }

    public List<String> getFootnote() {
        return footnote;
    }

    public void setFootnote(List<String> footnote) {
        this.footnote = footnote;
    }

    public List<String> getReferences() {
        return references;
    }

    public void setReferences(List<String> references) {
        this.references = references;
    }

    public String getVerseFlag() {
        return verseFlag;
    }

    public List<String> getMegunaslov() {
        return megunaslov;
    }

    public void setMegunaslov(List<String> megunaslov) {
        this.megunaslov = megunaslov;
    }

    public List<String> getMininaslov() {
        return mininaslov;
    }

    public void setMininaslov(List<String> mininaslov) {
        this.mininaslov = mininaslov;
    }

    public List<String> getPodnaslov() {
        return podnaslov;
    }

    public void setPodnaslov(List<String> podnaslov) {
        this.podnaslov = podnaslov;
    }

    public List<String> getPodnaslovReferenca() {
        return podnaslovReferenca;
    }

    public void setPodnaslovReferenca(List<String> podnaslovReferenca) {
        this.podnaslovReferenca = podnaslovReferenca;
    }

    public void setVerseFlag(String verseFlag) {
        this.verseFlag = verseFlag;
    }

    @Override
    public String toString() {
        return "BBLParsVerse{" +
                " |No: " + verseNo +
                " |Flag: " + verseFlag +
                " |Text: " + getTextList() +
                '}';
    }

    private String getTextList() {
        StringBuilder sb = new StringBuilder();
        for(String s : text){
            /*String n = s.replace(" -", "%^TIRE^%");
            n = n.replace(" –", "%^TIRE^%");*/
            sb.append(s).append("|");
        }
        return sb.toString();
    }
}
