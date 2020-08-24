package bean.insurance;

import java.sql.Date;

//参保单视图
public class ViewInsurance extends Insurance{
    private String name;//员工姓名
    private String cardId;//员工身份证号

    public ViewInsurance() {
    }

    public ViewInsurance(long eid, byte type, String code, Date start, float money, byte status, String reason, String name, String cardId) {
        super(eid, type, code, start, money, status, reason);
        this.name = name;
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "ViewInsurance{" +
                "name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}
