package bean.admin;

public class Finance {
    //客户编号
    private long cid;
    //统一社会信用代码
    private String credit_code;
    //开户的银行
    private String bank;
    //银行卡号
    private String cardNo;
    //联系人
    private String contact;
    //电话
    private String phone;
    //公司地址
    private String address;

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getCredit_code() {
        return credit_code;
    }

    public void setCredit_code(String credit_code) {
        this.credit_code = credit_code;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Finance() {
    }

    public Finance(long cid, String credit_code, String bank, String cardNo, String contact, String phone, String address) {
        this.cid = cid;
        this.credit_code = credit_code;
        this.bank = bank;
        this.cardNo = cardNo;
        this.contact = contact;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Finance{" +
                "cid=" + cid +
                ", credit_code='" + credit_code + '\'' +
                ", bank='" + bank + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
