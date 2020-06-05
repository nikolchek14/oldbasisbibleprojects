package biblija;

import java.util.Objects;

public class Podnaslov {
    String podnaslov;
    Futnota futnota;
    String megunaslov;
    String psalm;
    String referenci;

    public String getPodnaslov() {
        return podnaslov;
    }

    public void setPodnaslov(String podnaslov) {
        this.podnaslov = podnaslov;
    }

    public String getMegunaslov() {
        return megunaslov;
    }

    public void setMegunaslov(String megunaslov) {
        this.megunaslov = megunaslov;
    }

    public String getReferenci() {
        return referenci;
    }

    public void setReferenci(String referenci) {
        this.referenci = referenci;
    }

    public Futnota getFutnota() {
        return futnota;
    }

    public void setFutnota(Futnota futnota) {
        this.futnota = futnota;
    }

    public String getPsalm() {
        return psalm;
    }

    public void setPsalm(String psalm) {
        this.psalm = psalm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Podnaslov podnaslov1 = (Podnaslov) o;
        return Objects.equals(podnaslov, podnaslov1.podnaslov) &&
                Objects.equals(futnota, podnaslov1.futnota) &&
                Objects.equals(megunaslov, podnaslov1.megunaslov) &&
                Objects.equals(psalm, podnaslov1.psalm) &&
                Objects.equals(referenci, podnaslov1.referenci);
    }

    @Override
    public int hashCode() {
        return Objects.hash(podnaslov, futnota, megunaslov, psalm, referenci);
    }

    @Override
    public String toString() {
        return "Podnaslov{" +
                "podnaslov='" + podnaslov + '\'' +
                ", futnota=" + futnota +
                ", megunaslov='" + megunaslov + '\'' +
                ", psalm='" + psalm + '\'' +
                ", referenci='" + referenci + '\'' +
                '}';
    }

}
