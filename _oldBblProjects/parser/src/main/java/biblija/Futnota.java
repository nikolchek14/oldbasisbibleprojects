package biblija;

import java.util.Objects;

public class Futnota {
    String originalBroj;
    int redenBroj;
    String stih;
    String tekst;

    public String getOriginalBroj() {
        return originalBroj;
    }

    public void setOriginalBroj(String originalBroj) {
        this.originalBroj = originalBroj;
    }

    public int getRedenBroj() {
        return redenBroj;
    }

    public void setRedenBroj(int redenBroj) {
        this.redenBroj = redenBroj;
    }

    public String getStih() {
        return stih;
    }

    public void setStih(String stih) {
        this.stih = stih;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    @Override
    public String toString() {
        return "Futnota{" +
                "redenBroj=" + redenBroj +
                ", stih='" + stih + '\'' +
                ", tekst='" + tekst + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Futnota futnota = (Futnota) o;
        return redenBroj == futnota.redenBroj &&
                Objects.equals(originalBroj, futnota.originalBroj) &&
                Objects.equals(stih, futnota.stih) &&
                tekst.contains(futnota.tekst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalBroj, redenBroj, stih, tekst);
    }
}
