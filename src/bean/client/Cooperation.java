package bean.client;

//合作客户
public class Cooperation extends Client{

    private long did;//派遣方id
    /**
     * 0  合作客户
     * 1  潜在客户
     * 2  流失客户
     */
    private byte type;
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
    private float per1;//工伤结算比例
    private float per2;//工伤缴纳比例

    public Cooperation() {
    }

    public Cooperation(long id, long aid, String rid, String name, String nickname, String address, String contact, String phone, String wx, String qq, String mail, String intro, long did, byte type, byte category) {
        super(id, aid, rid, name, nickname, address, contact, phone, wx, qq, mail, intro);
        this.did = did;
        this.type = type;
        this.category = category;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getCategory() {
        return category;
    }

    public void setCategory(byte category) {
        this.category = category;
    }

    public float getPer1() {
        return per1;
    }

    public void setPer1(float per) {
        this.per1 = per;
    }

    public float getPer2() {
        return per2;
    }

    public void setPer2(float per) {
        this.per2 = per;
    }

    @Override
    public String toString() {
        return "Cooperation{" +
                "did=" + did +
                ", type=" + type +
                ", category=" + category +
                '}';
    }
}
