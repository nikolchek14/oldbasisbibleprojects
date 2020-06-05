package bblmid;

import java.util.ArrayList;
import java.util.List;

public class BBLMidPage {
    int pageNo;
    List<Linija> voved = new ArrayList<>();
    List<Linija> kolona1 = new ArrayList<>();
    List<Linija> kolona2 = new ArrayList<>();
    List<Linija> referenci1 = new ArrayList<>();
    List<Linija> referenci2 = new ArrayList<>();
    List<Linija> footnoti = new ArrayList<>();
    List<Linija> title = new ArrayList<>();


    public void setVoved(List<Linija> voved) {
        this.voved = voved;
    }

    public void setKolona1(List<Linija> kolona1) {
        this.kolona1 = kolona1;
    }

    public void setKolona2(List<Linija> kolona2) {
        this.kolona2 = kolona2;
    }

    public void setReferenci1(List<Linija> referenci1) {
        this.referenci1 = referenci1;
    }

    public void setReferenci2(List<Linija> referenci2) {
        this.referenci2 = referenci2;
    }

    public List<Linija> getFootnoti() {
        return footnoti;
    }

    public void setFootnoti(List<Linija> footnoti) {
        this.footnoti = footnoti;
    }

    public int getPageNo() {
        return pageNo;
    }

    public List<Linija> getVoved() {
        return voved;
    }

    public List<Linija> getKolona1() {
        return kolona1;
    }

    public List<Linija> getKolona2() {
        return kolona2;
    }

    public List<Linija> getReferenci1() {
        return referenci1;
    }

    public List<Linija> getReferenci2() {
        return referenci2;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<Linija> getTitle() {
        return title;
    }

    public void setTitle(List<Linija> title) {
        this.title = title;
    }
}
