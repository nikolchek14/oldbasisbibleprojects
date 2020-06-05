package bblpars;

import java.util.ArrayList;
import java.util.List;

public class Reference {
    int page;
    String stih;
    List<Linija> refNum = new ArrayList<>();
    List<Linija> refs = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Linija> getRefNum() {
        return refNum;
    }

    public void setRefNum(List<Linija> refNum) {
        this.refNum = refNum;
    }

    public List<Linija> getRefs() {
        return refs;
    }

    public void setRefs(List<Linija> refs) {
        this.refs = refs;
    }

    public String getStih() {
        return stih;
    }

    public void setStih(String stih) {
        this.stih = stih;
    }

    @Override
    public String toString() {
        return "Referenca{" +
                "page=" + page +
                ", refNum=" + refNum +
                ", refs=" + refs +
                ", stih=" + stih +
                '}';
    }
}
