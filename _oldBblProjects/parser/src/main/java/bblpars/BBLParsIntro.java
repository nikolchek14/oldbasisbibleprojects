package bblpars;

import java.util.ArrayList;
import java.util.List;

public class BBLParsIntro {
    List<Linija> voved = new ArrayList<>();
    List<Linija> predgovor = new ArrayList<>();

    public List<Linija> getVoved() {
        return voved;
    }

    public void setVoved(List<Linija> voved) {
        this.voved = voved;
    }

    public List<Linija> getPredgovor() {
        return predgovor;
    }

    public void setPredgovor(List<Linija> predgovor) {
        this.predgovor = predgovor;
    }

    @Override
    public String toString() {
        return "BBLParsIntro{" +
                "voved=" + voved +
                ", predgovor=" + predgovor +
                '}';
    }
}
