package bean.client;

//派遣方客户
public class Dispatch extends  Client{

    private byte type;//是否为合作客户  0_ 潜在客户 1_合作客户

    public Dispatch() {
    }

    public Dispatch(long id, long aid, String rid, String name, String nickname, String address, String contact, String phone, String wx, String qq, String mail, String intro,  byte type) {
        super(id, aid, rid, name, nickname, address, contact, phone, wx, qq, mail, intro);

        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Dispatch{" +

                ", type=" + type +
                '}';
    }
}
