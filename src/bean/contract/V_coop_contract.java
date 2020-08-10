package bean.contract;

import java.sql.Date;

//合作客户合同视图
public class V_coop_contract extends Contract{
    private String name;//客户名称
    private byte stype;//类型 0_劳务外包派遣 1_人事代理 2小时工 3商业保险

    public V_coop_contract() {
    }

    public V_coop_contract(String id, long aid, long bid, String type, Date start, Date end, byte status, String comments, byte invoice, String project, byte times, String name, byte stype) {
        super(id, aid, bid, type, start, end, status, comments, invoice, project, times);
        this.name = name;
        this.stype = stype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getStype() {
        return stype;
    }

    public void setStype(byte stype) {
        this.stype = stype;
    }

    @Override
    public String toString() {
        return "V_coop_contract{" +
                "name='" + name + '\'' +
                ", stype=" + stype +
                '}';
    }
}
