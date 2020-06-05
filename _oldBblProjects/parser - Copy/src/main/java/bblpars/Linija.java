package bblpars;

import java.util.ArrayList;
import java.util.List;

public class Linija {
    private int top;
    private int mid;
    private int left;
    private int dif;
    private int page;
    private String value;
    private Linija futnota;
    private List<Linija> futtext = new ArrayList<>();

    public Linija() {
    }

    public Linija(int top, int mid, String value, int dif, int page, int left) {
        this.top = top;
        this.mid = mid;
        this.value = value;
        this.dif = dif;
        this.page = page;
        this.left = left;
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

    public Linija getFutnota() {
        return futnota;
    }

    public void setFutnota(Linija futnota) {
        this.futnota = futnota;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getDif() {
        return dif;
    }

    public void setDif(int dif) {
        this.dif = dif;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Linija> getFuttext() {
        return futtext;
    }

    public void setFuttext(List<Linija> futtext) {
        this.futtext = futtext;
    }

    @Override
    public String toString() {
        return "Linija{" +
                "top=" + top +
                ", futnota=" + futnota +
                ", futtext=" + futtext +
                ", value='" + value + '\'' +
                '}';
    }
}
