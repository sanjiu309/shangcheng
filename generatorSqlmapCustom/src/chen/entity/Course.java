package chen.entity;

public class Course {
    private Integer cid;

    private String cname;

    private Integer chour;

    private Integer cscore;

    private String cdate;

    private String place;

    private Integer tid;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public Integer getChour() {
        return chour;
    }

    public void setChour(Integer chour) {
        this.chour = chour;
    }

    public Integer getCscore() {
        return cscore;
    }

    public void setCscore(Integer cscore) {
        this.cscore = cscore;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate == null ? null : cdate.trim();
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }
}