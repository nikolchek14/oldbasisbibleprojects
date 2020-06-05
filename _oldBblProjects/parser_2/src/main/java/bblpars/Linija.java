package bblpars;

public class Linija {
    private int top;
    private int mid;
    private String value;

    public Linija(int top, int mid, String value) {
        this.top = top;
        this.mid = mid;
        this.value = value;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Linija{" +
                "top=" + top +
                ", mid=" + mid +
                ", value='" + value + '\'' +
                '}';
    }
}
