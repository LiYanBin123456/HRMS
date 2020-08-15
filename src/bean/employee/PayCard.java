package bean.employee;
//工资卡
public class PayCard {
    private long eid;//员工id 外键
    private String bank1;//银行总称
    private String bank2;//开户银行的详细名称
    private String bankNo;//行号  银行的编号
    private String cardNo;//账号 银行卡号

    public PayCard() {
    }

    public PayCard(long eid, String bank1, String bank2, String bankNo, String cardNo) {
        this.eid = eid;
        this.bank1 = bank1;
        this.bank2 = bank2;
        this.bankNo = bankNo;
        this.cardNo = cardNo;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public String getBank1() {
        return bank1;
    }

    public void setBank1(String bank1) {
        this.bank1 = bank1;
    }

    public String getBank2() {
        return bank2;
    }

    public void setBank2(String bank2) {
        this.bank2 = bank2;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public String toString() {
        return "PayCard{" +
                "eid=" + eid +
                ", bank1='" + bank1 + '\'' +
                ", bank2='" + bank2 + '\'' +
                ", bankNo='" + bankNo + '\'' +
                ", cardNo='" + cardNo + '\'' +
                '}';
    }
}
