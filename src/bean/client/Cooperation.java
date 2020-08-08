package bean.client;

//合作客户
public class Cooperation extends Client{
    private Client client;
    /**
     * 0  潜在客户
     * 1  合作客户
     */
    private byte status;
    /**客户性质
     0_政府部门
     1_事业单位
     2_人才市场、
     3_学校
     4_内资企业
     5_外资企业
     6_港澳台企业
     7_内资工厂
     8_外资工厂
     9_港澳台工厂
     10_其它。
     *
     */
    private byte category;

    public Cooperation() {
    }

    public Cooperation(long id, String rid, String name, String nickname, String address, String contact, String phone, String wx, String qq, String intro, Client client, byte status, byte category) {
        super(id, rid, name, nickname, address, contact, phone, wx, qq, intro);
        this.client = client;
        this.status = status;
        this.category = category;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getCategory() {
        return category;
    }

    public void setCategory(byte category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Cooperation{" +
                "client=" + client +
                ", status=" + status +
                ", category=" + category +
                '}';
    }
}
