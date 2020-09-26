package bean.contract;

import java.sql.Date;

public class ViewContractDispatch extends Contract{
    private String name;//派遣方名字

    public ViewContractDispatch() {
    }

    public ViewContractDispatch(String id, long aid, long bid, String type, Date start, Date end, byte status, String comments, byte invoice, String project, float per, byte times, String name) {
        super(id, aid, bid, type, start, end, status, comments, invoice, project, per, times);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ViewContractDispatch{" +
                "name='" + name + '\'' +
                '}';
    }
}
