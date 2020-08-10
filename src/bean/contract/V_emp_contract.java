package bean.contract;

import java.sql.Date;

//员工合同视图
public class V_emp_contract extends Contract{
    private String cardId;//员工身份证
    private String name;//员工姓名

    public V_emp_contract() {
    }

    public V_emp_contract(String id, long aid, long bid, String type, Date start, Date end, byte status, String comments, byte invoice, String project, byte times, String cardId, String name) {
        super(id, aid, bid, type, start, end, status, comments, invoice, project, times);
        this.cardId = cardId;
        this.name = name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "V_emp_contract{" +
                "cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
