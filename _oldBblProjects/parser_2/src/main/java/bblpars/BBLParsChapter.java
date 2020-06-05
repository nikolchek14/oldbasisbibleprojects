package bblpars;

import java.util.ArrayList;
import java.util.List;

public class BBLParsChapter {
    List<BBLParsVerse> bblVerses = new ArrayList<>();
    private String chapterNo;

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
}
