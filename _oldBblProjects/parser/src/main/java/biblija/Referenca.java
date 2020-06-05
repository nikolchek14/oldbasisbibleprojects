package biblija;

import java.util.ArrayList;
import java.util.List;

public class Referenca {
    String originalenStih;
    List<String> referenci = new ArrayList<>();
    List<Link> linkoj = new ArrayList<>();

    public List<String> getReferenci() {
        return referenci;
    }

    public void setReferenci(List<String> referenci) {
        this.referenci = referenci;
    }

    public String getOriginalenStih() {
        return originalenStih;
    }

    public void setOriginalenStih(String originalenStih) {
        this.originalenStih = originalenStih;
    }

    public List<Link> getLinkoj() {
        return linkoj;
    }

    public void setLinkoj(List<Link> linkoj) {
        this.linkoj = linkoj;
    }

    @Override
    public String toString() {
        return "Referenca{" +
                "originalenStih='" + originalenStih + '\'' +
                ", referenci=" + referenci +
                ", linkoj=" + linkoj +
                '}';
    }
}
