package bean.insurance;



//参保单视图
public class ViewInsured extends Insured{
    private String cname;//合作单位名称

    public ViewInsured() {
    }

    public ViewInsured(long id, long cid, String cardId, String name, String place, String post, byte category, String cname) {
        super(id, cid, cardId, name, place, post, category);
        this.cname = cname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
