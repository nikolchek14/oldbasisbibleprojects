package bblpars;

import java.util.ArrayList;
import java.util.List;

public class BBLParsChapter {
    List<BBLParsVerse> bblVerses = new ArrayList<>();
    private String chapterNo;
    private int broj;

    public List<BBLParsVerse> getBblVerses() {
        return bblVerses;
    }

    public void setBblVerses(List<BBLParsVerse> bblVerses) {
        this.bblVerses = bblVerses;
    }

    public void setChapterNo(String chapterNo) {
        this.chapterNo = chapterNo;
    }

    public String getChapterNo() {
        return chapterNo;
    }

    public void setChapterNum(int parseInt) {
        this.broj = parseInt;
    }

    public int getBroj() {
        return broj;
    }
}
