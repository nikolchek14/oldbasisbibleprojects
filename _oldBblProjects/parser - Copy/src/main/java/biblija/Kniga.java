package biblija;

import java.util.ArrayList;
import java.util.List;

public class Kniga {
    List<Glava> glavi = new ArrayList<>();
    Naslov naslov;
    String ime;
    String kratenka;
    Voved voved;
    Voved predgovor;

    public List<Glava> getGlavi() {
        return glavi;
    }

    public void setGlavi(List<Glava> glavi) {
        this.glavi = glavi;
    }

    public Naslov getNaslov() {
        return naslov;
    }

    public void setNaslov(Naslov naslov) {
        this.naslov = naslov;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getKratenka() {
        return kratenka;
    }

    public void setKratenka(String kratenka) {
        this.kratenka = kratenka;
    }

    public Voved getVoved() {
        return voved;
    }

    public void setVoved(Voved voved) {
        this.voved = voved;
    }

    public Voved getPredgovor() {
        return predgovor;
    }

    public void setPredgovor(Voved predgovor) {
        this.predgovor = predgovor;
    }

    @Override
    public String toString() {
        return "Kniga{" +
                //"glavi=" + glavi +
                ", naslov=" + naslov +
                ", ime='" + ime + '\'' +
                ", kratenka='" + kratenka + '\'' +
                ", voved=" + voved +
                ", predgovor=" + predgovor +
                '}';
    }
}
