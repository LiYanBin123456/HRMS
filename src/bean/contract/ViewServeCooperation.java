package bean.contract;

//合作客户有效服务项目视图
public class ViewServeCooperation extends Serve {
    private long id;//合作客户id
    private String name;//合作客户名称
    private byte status;//合同状态

    public ViewServeCooperation() {
    }

    public ViewServeCooperation(String cid, byte type, byte category, byte payment, byte settlement, byte receipt, long pid, float value, byte payoff, long id, String name, byte status) {
        super(cid, type, category, payment, settlement, receipt, pid, value, payoff);
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ViewServeCooperation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
