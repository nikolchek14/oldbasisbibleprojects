package biblija;

import java.util.ArrayList;
import java.util.List;

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
}
