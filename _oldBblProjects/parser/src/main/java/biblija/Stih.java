package biblija;

import java.util.*;

public class Stih {
    int redenBroj;
    Podnaslov podnaslov;
    String tekst;
    Map<Integer, Futnota> futnoti = new TreeMap<>();
    List<Referenca> referenci = new ArrayList<>();
    private String dodatok;

    public Podnaslov getPodnaslov() {
        return podnaslov;
    }

    public void setPodnaslov(Podnaslov podnaslov) {
        this.podnaslov = podnaslov;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public Map<Integer, Futnota> getFutnoti() {
        return futnoti;
    }

    public void setFutnoti(Map<Integer, Futnota> futnoti) {
        this.futnoti = futnoti;
    }

    public List<Referenca> getReferenci() {
        return referenci;
    }

    public void setReferenci(List<Referenca> referenci) {
        this.referenci = referenci;
    }

    public int getRedenBroj() {
        return redenBroj;
    }

    public void setRedenBroj(int redenBroj) {
        this.redenBroj = redenBroj;
    }

    public void setDodatok(String dodatok) {
        this.dodatok = dodatok;
    }

    public String getDodatok() {
        return dodatok;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stih stih = (Stih) o;
        return redenBroj == stih.redenBroj &&
                Objects.equals(podnaslov, stih.podnaslov) &&
                Objects.equals(tekst, stih.tekst) &&
                //Objects.equals(futnoti, stih.futnoti) &&
                //Objects.equals(referenci, stih.referenci) &&
                Objects.equals(dodatok, stih.dodatok);
    }

    @Override
    public int hashCode() {
        return Objects.hash(redenBroj, podnaslov, tekst, futnoti, referenci, dodatok);
    }

    @Override
    public String toString() {
        return "Stih{" +
                "redenBroj=" + redenBroj +
                ", podnaslov=" + podnaslov +
                ", tekst='" + tekst + '\'' +
                ", futnoti=" + futnoti +
                ", referenci=" + referenci +
                ", dodatok='" + dodatok + '\'' +
                '}';
    }
}
