package biblija;

import java.util.HashMap;
import java.util.Map;

public class Stih {
    int redenBroj;
    Podnaslov podnaslov;
    String tekst;
    Map<Integer, Futnota> futnoti = new HashMap<>();
    Referenca referenca;

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

    public Referenca getReferenca() {
        return referenca;
    }

    public void setReferenca(Referenca referenca) {
        this.referenca = referenca;
    }

    public int getRedenBroj() {
        return redenBroj;
    }

    public void setRedenBroj(int redenBroj) {
        this.redenBroj = redenBroj;
    }
}
