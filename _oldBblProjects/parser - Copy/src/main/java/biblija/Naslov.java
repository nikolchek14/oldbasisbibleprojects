package biblija;

public class Naslov {
    String naslov;
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

    @Override
    public String toString() {
        return "Naslov{" +
                "naslov='" + naslov + '\'' +
                ", podnaslov='" + podnaslov + '\'' +
                '}';
    }
}
