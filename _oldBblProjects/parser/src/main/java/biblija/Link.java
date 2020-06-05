package biblija;

public class Link {
    private String kniga;
    private String tekst;
    private int glava;
    private int stih;

    public String getKniga() {
        return kniga;
    }

    public void setKniga(String kniga) {
        this.kniga = kniga;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public int getGlava() {
        return glava;
    }

    public void setGlava(int glava) {
        this.glava = glava;
    }

    public int getStih() {
        return stih;
    }

    public void setStih(int stih) {
        this.stih = stih;
    }

    @Override
    public String toString() {
        return "Link{" +
                "kniga='" + kniga + '\'' +
                ", tekst='" + tekst + '\'' +
                ", glava=" + glava +
                ", stih=" + stih +
                '}';
    }
}
