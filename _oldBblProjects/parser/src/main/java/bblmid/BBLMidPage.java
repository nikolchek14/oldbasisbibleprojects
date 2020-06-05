package bblmid;

import java.util.ArrayList;
import java.util.List;

public class BBLMidPage {
    int pageNo;
    List<MidLinija> voved = new ArrayList<>();
    List<MidLinija> kolona1 = new ArrayList<>();
    List<MidLinija> kolona2 = new ArrayList<>();
    List<MidLinija> referenci1 = new ArrayList<>();
    List<MidLinija> referenci2 = new ArrayList<>();
    List<MidLinija> footnoti = new ArrayList<>();
    List<MidLinija> title = new ArrayList<>();


    public void setVoved(List<MidLinija> voved) {
        this.voved = voved;
    }

    public void setKolona1(List<MidLinija> kolona1) {
        this.kolona1 = kolona1;
    }

    public void setKolona2(List<MidLinija> kolona2) {
        this.kolona2 = kolona2;
    }

    public void setReferenci1(List<MidLinija> referenci1) {
        this.referenci1 = referenci1;
    }

    public void setReferenci2(List<MidLinija> referenci2) {
        this.referenci2 = referenci2;
    }

    public List<MidLinija> getFootnoti() {
        return footnoti;
    }

    public void setFootnoti(List<MidLinija> footnoti) {
        this.footnoti = footnoti;
    }

    public int getPageNo() {
        return pageNo;
    }

    public List<MidLinija> getVoved() {
        return voved;
    }

    public List<MidLinija> getKolona1() {
        return kolona1;
    }

    public List<MidLinija> getKolona2() {
        return kolona2;
    }

    public List<MidLinija> getReferenci1() {
        return referenci1;
    }

    public List<MidLinija> getReferenci2() {
        return referenci2;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<MidLinija> getTitle() {
        return title;
    }

    public void setTitle(List<MidLinija> title) {
        this.title = title;
    }
}
