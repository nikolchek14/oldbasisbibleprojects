package bblpars;

import java.util.ArrayList;
import java.util.List;

public class BBLParsBook {
    String title;
    List<BBLParsChapter> bblChapters = new ArrayList<>();
    List<BBLParsIntro> bblIntro = new ArrayList<>();

    public List<BBLParsChapter> getBblChapters() {
        return bblChapters;
    }

    public void setBblChapters(List<BBLParsChapter> bblChapters) {
        this.bblChapters = bblChapters;
    }

    public List<BBLParsIntro> getBblIntro() {
        return bblIntro;
    }

    public void setBblIntro(List<BBLParsIntro> bblIntro) {
        this.bblIntro = bblIntro;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
