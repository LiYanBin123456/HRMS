package bean.client;

public class ViewFinanceCooperation  extends Cooperation{
    private float balance;//公司余额

    public ViewFinanceCooperation() {
    }

    public ViewFinanceCooperation(long id, long aid, String rid, String name, String nickname, String address, String contact, String phone, String wx, String qq, String mail, String intro, long did, byte type, byte category, float balance) {
        super(id, aid, rid, name, nickname, address, contact, phone, wx, qq, mail, intro, did, type, category);
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "ViewFinanceCooperation{" +
                "balance=" + balance +
                '}';
    }
}
