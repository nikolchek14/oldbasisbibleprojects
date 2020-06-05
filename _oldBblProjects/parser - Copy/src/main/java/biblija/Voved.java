package biblija;

import java.util.ArrayList;
import java.util.List;

public class Voved {
    List<String> paragrafi = new ArrayList<>();

    public List<String> getParagrafi() {
        return paragrafi;
    }

    public void setParagrafi(List<String> paragrafi) {
        this.paragrafi = paragrafi;
    }

    @Override
    public String toString() {
        return "Voved{" +
                "paragrafi=" + paragrafi +
                '}';
    }
}
