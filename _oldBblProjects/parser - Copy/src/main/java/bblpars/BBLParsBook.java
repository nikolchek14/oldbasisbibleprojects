package bblpars;

import java.util.ArrayList;
import java.util.List;

public class BBLParsBook {
    List<Linija> title = new ArrayList<>();
    String naslov;
    String kratenka;
    List<BBLParsChapter> bblChapters = new ArrayList<>();
    BBLParsIntro bblIntro = new BBLParsIntro();

    public List<BBLParsChapter> getBblChapters() {
        return bblChapters;
    }

    public void setBblChapters(List<BBLParsChapter> bblChapters) {
        this.bblChapters = bblChapters;
    }

    public BBLParsIntro getBblIntro() {
        return bblIntro;
    }

    public void setBblIntro(BBLParsIntro bblIntro) {
        this.bblIntro = bblIntro;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getKratenka() {
        return kratenka;
    }

    public void setKratenka(String kratenka) {
        this.kratenka = kratenka;
    }

    public List<Linija> getTitle() {
        return title;
    }

    public void setTitle(List<Linija> title) {
        this.title = title;
    }
}
