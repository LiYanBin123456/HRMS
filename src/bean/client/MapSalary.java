package bean.client;

import java.sql.Date;

//客户自定义工资
public class MapSalary {
    private long cid;//合作客户id
    private String items;//自定义名称集
    private Date date;//更新时间

    public MapSalary() {
    }

    public MapSalary(long cid, String items, Date date) {
        this.cid = cid;
        this.items = items;
        this.date = date;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
