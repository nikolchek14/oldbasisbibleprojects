package biblija;

import java.util.Objects;

public class Naslov {
    String naslov;
    Futnota futnota;
    String podnaslov;

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getPodnaslov() {
        return podnaslov;
    }

    public void setPodnaslov(String podnaslov) {
        this.podnaslov = podnaslov;
    }

    public Futnota getFutnota() {
        return futnota;
    }

    public void setFutnota(Futnota futnota) {
        this.futnota = futnota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Naslov naslov1 = (Naslov) o;
        return Objects.equals(naslov, naslov1.naslov) &&
                Objects.equals(futnota, naslov1.futnota) &&
                Objects.equals(podnaslov, naslov1.podnaslov);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naslov, futnota, podnaslov);
    }

    @Override
    public String toString() {
        return "Naslov{" +
                "naslov='" + naslov + '\'' +
                ", futnota=" + futnota +
                ", podnaslov='" + podnaslov + '\'' +
                '}';
    }
}
