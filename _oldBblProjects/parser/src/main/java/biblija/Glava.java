package biblija;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Glava {
    int redenBroj;
    List<Stih> stihovi = new ArrayList<>();

    public int getRedenBroj() {
        return redenBroj;
    }

    public void setRedenBroj(int redenBroj) {
        this.redenBroj = redenBroj;
    }

    public List<Stih> getStihovi() {
        return stihovi;
    }

    public void setStihovi(List<Stih> stihovi) {
        this.stihovi = stihovi;
    }

    @Override
    public String toString() {
        return "Glava{" +
                "redenBroj=" + redenBroj +
                //", stihovi=" + stihovi +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Glava glava = (Glava) o;
        return redenBroj == glava.redenBroj /*&&
                Objects.equals(stihovi, glava.stihovi)*/;
    }

    @Override
    public int hashCode() {
        return Objects.hash(redenBroj, stihovi);
    }
}
