package bean.client;

//派遣方客户
public class Dispatch extends  Client{

    private Client client;
    private byte status;//是否为合作客户  0_ 潜在客户 1_合作客户

    public Dispatch() {
    }

    public Dispatch(long id, String rid, String name, String nickname, String address, String contact, String phone, String wx, String qq, String intro, Client client, byte status) {
        super(id, rid, name, nickname, address, contact, phone, wx, qq, intro);
        this.client = client;
        this.status = status;
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

    @Override
    public String toString() {
        return "Dispatch{" +
                "client=" + client +
                ", status=" + status +
                '}';
    }
}
