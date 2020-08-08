package bean.client;
//供应商
public class Supplier extends Client {

    private long did;//派遣方客户id
    /**业务类型
     0_顶岗实习
     1_就业安置
     2_其他
     *
     */
    private byte business;

    public Supplier() {
    }

    public Supplier(long id, long aid, String rid, String name, String nickname, String address, String contact, String phone, String wx, String qq, String mail, String intro, long did, byte business) {
        super(id, aid, rid, name, nickname, address, contact, phone, wx, qq, mail, intro);
        this.did = did;
        this.business = business;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public byte getBusiness() {
        return business;
    }

    public void setBusiness(byte business) {
        this.business = business;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "did=" + did +
                ", business=" + business +
                '}';
    }
}
