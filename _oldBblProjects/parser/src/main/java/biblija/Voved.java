package biblija;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voved voved = (Voved) o;
        return Objects.equals(paragrafi, voved.paragrafi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paragrafi);
    }
}
