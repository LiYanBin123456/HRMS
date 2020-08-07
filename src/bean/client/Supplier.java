package bean.client;
//供应商
public class Supplier extends Client {
    private Client client;
    /**业务类型
     0_顶岗实习
     1_就业安置
     2_其他
     *
     */
    private byte business;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public byte getBusiness() {
        return business;
    }

    public void setBusiness(byte business) {
        this.business = business;
    }

    public Supplier() {
    }

    public Supplier(long id, String rid, String name, String nickname, String address, String contact, String phone, String wx, String qq, String intro, Client client, byte business) {
        super(id, rid, name, nickname, address, contact, phone, wx, qq, intro);
        this.client = client;
        this.business = business;
    }
}
