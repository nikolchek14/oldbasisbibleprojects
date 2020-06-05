package biblija;

import java.util.ArrayList;
import java.util.List;

public class Biblija {
    List<Kniga> knigi = new ArrayList<>();

    public List<Kniga> getKnigi() {
        return knigi;
    }

    public void setKnigi(List<Kniga> knigi) {
        this.knigi = knigi;
    }
}
